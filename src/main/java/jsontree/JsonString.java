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
public final class JsonString extends JsonNode {
  private final String jsonString;

  /**
   * This is a constructor for JsonString class to
   * initialize the jsonString passes to it.
   * @param jsonString Takes in the String value to be
   *                   initialized to the jsonString
   *                   attribute of the class.
   */
  public JsonString(String jsonString) {
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


  /**
   * This method overrides the default equals method of object Class.
   *
   * @param o Takes in the object to be compared with.
   * @return a boolean value indicating the sameness.
   */
  @Override
  public boolean equals(Object o) {
    // Fast pointer equality.
    if (this == o) {
      return true;
    }
    // If o isn't the right class then it can't be equal.
    if (!(o instanceof JsonString)) {
      return false;
    }

    JsonString other = (JsonString) o;

    return this.jsonString.equals(other.jsonString);
  }

  /**
   * Hashcode method has to be replaced as well to work
   * properly with Hashing.
   *
   * @return An int denoting the hashcode.
   */
  @Override
  public int hashCode() {
    return jsonString.hashCode();
  }
}
