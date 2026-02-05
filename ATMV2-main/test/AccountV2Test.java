import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class AccountV2Test {
    @Test
    public void transferMovesFundsWhenBalanceSufficient() {
        AccountV2 source = new AccountV2("alice", "1234", 500);
        AccountV2 target = new AccountV2("bob", "0000", 200);

        boolean result = source.transfer(150, target);

        assertTrue(result);
        assertEquals(350, source.getBalance());
        assertEquals(350, target.getBalance());
    }
}
