package jsontree;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a concrete class that extends the  IJsonArray
 * abstract class fro specific functionalities.
 *
 * <p>It has a jsonArray attribute that is of type List< JsonNode >
 * which stores JsonNode objects.
 *
 * <p>It overrides add, prettyPrint, equals and hashcode methods
 * to fit the needs of this class.
 */
public final class JsonArray extends IJsonArray {
  List<JsonNode> jsonArray;

  /**
   * This is the constructor for JsonArray class that initializes
   * the attribute jsonArray with an ArrayList that stores JsonNode
   * objects.
   */
  public JsonArray() {
    jsonArray = new ArrayList<JsonNode>();
  }

  /**
   * This method simply adds a JsonNode object to the
   * jsonArray List attribute.
   *
   * @param value the JsonNode object to be added to the IJsonArray
   *              type object.
   */
  @Override
  public void add(JsonNode value) {
    jsonArray.add(value);
  }

  /**
   * This method converts the jsonArray into a string
   * with correct formatting.
   *
   * <p>A for loop is used to get the elements in the array.
   * A ',' and next line escape character is added at the end
   * of each element. ',' is not added after the last element
   * of the array.
   *
   * @return A String object representing the pretty formatted
   *          jsonArray.
   */
  @Override
  public String prettyPrint() {
    if (jsonArray.isEmpty()) {
      return "[\n]";
    }

    StringBuilder prettyJson = new StringBuilder();

    prettyJson.append("[\n");
    for (int idx = 0; idx < jsonArray.size(); idx++) {


      if (!(jsonArray.get(idx) instanceof JsonString)) {
        prettyJson.append("  ");
        for (char c : jsonArray.get(idx).prettyPrint().toCharArray()) {
          prettyJson.append(c);
          if (c == '\n') {
            prettyJson.append("  ");
          }
        }
      }
      else {
        prettyJson.append("  ");
        prettyJson.append(jsonArray.get(idx).prettyPrint());
      }

      // Add ',' after all the elements except the last one.
      if (idx < jsonArray.size() - 1) {
        prettyJson.append(',');
      }

      prettyJson.append('\n');
    }
    prettyJson.append("]");

    return prettyJson.toString();
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
    if (!(o instanceof JsonArray)) {
      return false;
    }

    JsonArray other = (JsonArray) o;

    return this.jsonArray.equals(other.jsonArray);
  }

  /**
   * Hashcode method has to be replaced as well to work
   * properly with Hashing.
   *
   * @return An int denoting the hashcode.
   */
  @Override
  public int hashCode() {
    return jsonArray.hashCode();
  }
}
