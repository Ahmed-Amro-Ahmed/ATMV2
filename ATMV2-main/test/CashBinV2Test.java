import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class CashBinV2Test {
    @Test
    public void withdrawFindsExactCombinationWhenGreedyWouldFail() throws Exception {
        Path path = Paths.get("inventory_v2.json");
        byte[] original = Files.exists(path) ? Files.readAllBytes(path) : null;

        try {
            JSONObject obj = new JSONObject();
            obj.put("bill10Count", 0);
            obj.put("bill20Count", 4);
            obj.put("bill50Count", 1);
            obj.put("bill100Count", 0);
            obj.put("depositsHeld", 0);
            Files.writeString(path, obj.toString(2));

            CashBinV2 bin = new CashBinV2(0);

            boolean result = bin.withdraw(80);

            assertTrue(result);
            assertEquals(0, bin.getBill10Count());
            assertEquals(0, bin.getBill20Count());
            assertEquals(1, bin.getBill50Count());
            assertEquals(0, bin.getBill100Count());
        } finally {
            if (original != null) {
                Files.write(path, original);
            } else {
                Files.deleteIfExists(path);
            }
        }
    }
}
