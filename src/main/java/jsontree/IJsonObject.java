package jsontree;

/**
 * This as an abstract class that represents a specification
 * for a Json object. It extends JsonNode and declares (but does not implement)
 * a new public method void add(JsonNode value) that appends a new
 * element to this object.
 */
public abstract class IJsonObject extends JsonNode {
  /**
   * This method adds a new key value pair of a String and
   * JsonNode respectively, to an IJsonObject object.
   *
   * @param key The String to be used as the key for a key value pair.
   * @param value The value to be stored for a key in the pair.
   */
  public abstract void add(String key, JsonNode value);
}
