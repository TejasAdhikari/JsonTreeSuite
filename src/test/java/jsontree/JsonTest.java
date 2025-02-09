package jsontree;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

public class JsonTest {
//  JsonNode node;

  @Test
  public void testValidString() {
    JsonString node = new JsonString("Test");

    assertEquals("\"Test\"", node.prettyPrint());
  }
}