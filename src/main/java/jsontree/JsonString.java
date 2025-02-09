package jsontree;

/**
 * This is a concrete class that represents a Json string.
 * It extends and fully implements JsonNode.
 *
 * <p>The string is stored in a String object jsonString which is
 * final and is set in the constructor.
 *
 * <p>We have implemented a getter for this class as more other classes
 * might need access to the value of class attribute jsonString.
 * String is immutable so it doesn't cause any issues anyway.
 */
public class JsonString extends JsonNode {
  private final String jsonString;

  /**
   * This is a constructor for JsonString class to
   * initialize the jsonString passes to it.
   * @param jsonString Takes in the String value to be
   *                   initialized to the jsonString
   *                   attribute of the class.
   */
  public JsonString (String jsonString) {
    this.jsonString = jsonString;
  }

  /**
   * This method gets the string the class attribute
   * is storing.
   * @return the jsonString class attribute.
   */
  public String prettyPrint() {
    return "\"" + jsonString + "\"";
  }
}
