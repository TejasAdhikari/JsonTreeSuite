package jsontree;

/**
 * This as an abstract class that represents a specification
 * for a Json array. It extends JsonNode and declares (but does not implement)
 * a new public method void add(JsonNode value) that appends a new element to this array.
 */
public abstract class IJsonArray extends JsonNode {
  /**
   * This method appends a new JsonNode element to an IJsonArray object.
   *
   * @param value the JsonNode object to be added to the IJsonArray
   *              type object.
   */
  public abstract void add(JsonNode value);
}
