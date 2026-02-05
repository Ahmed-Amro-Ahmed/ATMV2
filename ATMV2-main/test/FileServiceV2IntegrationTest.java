import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.jupiter.api.Test;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileServiceV2IntegrationTest {
    @Test
    public void appendTransactionCreatesValidJsonArray() throws Exception {
        Path path = Paths.get("transactions_v2_test.json");
        Files.deleteIfExists(path);

        try {
            FileServiceV2.appendTransactionToJsonArray(
                    "transactions_v2_test.json",
                    new JSONObject().put("type", "TEST").put("amount", 5).toString()
            );

            String content = Files.readString(path);
            JSONArray arr = new JSONArray(content);
            JSONObject obj = arr.getJSONObject(0);

            assertEquals(1, arr.length());
            assertEquals("TEST", obj.getString("type"));
            assertEquals(5, obj.getInt("amount"));
        } finally {
            Files.deleteIfExists(path);
        }
    }
}
