import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PersistenceManagerV2IntegrationTest {
    @Test
    public void saveAndLoadAccountsRoundTrip() throws Exception {
        Path path = Paths.get("users_secure_v2.json");
        byte[] original = Files.exists(path) ? Files.readAllBytes(path) : null;

        try {
            List<AccountV2> accounts = Arrays.asList(
                    new AccountV2("user_a", "1111", 500),
                    new AccountV2("user_b", "2222", 1200)
            );

            PersistenceManagerV2.saveAccounts(accounts);
            List<AccountV2> loaded = PersistenceManagerV2.loadAccounts();

            assertEquals(2, loaded.size());
            assertEquals(500, findBalance(loaded, "user_a"));
            assertEquals(1200, findBalance(loaded, "user_b"));
        } finally {
            if (original != null) {
                Files.write(path, original);
            } else {
                Files.deleteIfExists(path);
            }
        }
    }

    private int findBalance(List<AccountV2> accounts, String username) {
        for (AccountV2 account : accounts) {
            if (account.getUsername().equals(username)) {
                return account.getBalance();
            }
        }
        return -1;
    }
}
