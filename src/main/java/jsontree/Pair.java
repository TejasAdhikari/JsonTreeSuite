package jsontree;

/**
 * This is a concrete class that stores a pair of values.
 * The Pair has to be of type String and JsonNode.
 * The key being String and the value being JsonNode.
 *
 * <p>This class just stores and retrieves the
 * attributes.
 */
public final class Pair {
  private final String key;
  private final JsonNode value;

  /**
   * This is the constructor for Pair class that initializes
   * the key and value attribute passed to it.
   *
   * @param key a string value denoting the key of the key-value pair.
   * @param value a JsonNode instance denoting the value for the Key-value pair.
   */
  public Pair(String key, JsonNode value) {
    this.key = key;
    this.value = value;
  }

  /**
   * Returns the string stored in the key attribute.
   *
   * @return a string denoting the key of the pair.
   */
  public String getKey() {
    return key;
  }

  /**
   * Returns the JsonNode Object in the value attribute.
   *
   * @return a JsonNode denoting the value of the pair.
   */
  public JsonNode getValue() {
    return value;
  }
}
