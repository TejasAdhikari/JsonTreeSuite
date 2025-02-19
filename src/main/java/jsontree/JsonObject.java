package jsontree;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * This is a concrete class that extends the IJsonObject
 * abstract class for specific functionalities.
 *
 * <p>It has a jsonObject attribute that is of type List< Pair >
 * which stores Pair objects.
 *
 * <p>It overrides add, prettyPrint, equals and hashcode methods
 * to fit the needs of this class.
 */
public final class JsonObject extends IJsonObject {
  List<Pair> jsonObject;

  /**
   * This is the constructor for class JsonObject that
   * initializes the jsonObject Attribute with an ArrayList.
   */
  public JsonObject() {
    this.jsonObject = new ArrayList<Pair>();
  }

  /**
   * This Method adds a Pair instance to the
   * jsonObject List attribute.
   *
   * @param key Key to be stored in the key value pair.
   * @param value Value to be stored for the key.
   * @throws IllegalArgumentException if an invalid key is passed.
   */
  @Override
  public void add(String key, JsonNode value) throws IllegalArgumentException {
    // Send key through parser and return IllegalArgument exception
    if (!isValidKey(key)) {
      throw new IllegalArgumentException("The entered key is Invalid");
    }

    jsonObject.add(new Pair(key, value));
  }

  /**
   * This checks if a key is valid to be used in
   * a Json Object or not.
   *
   * <p>Has to a separate function to maintain independence
   * from JsonParser.
   *
   * @param key String value to be validated.
   * @return a boolean value indicating if the key is valid or not.
   */
  private boolean isValidKey(String key) {
    if (key == null || key.isEmpty() || !Character.isLetter(key.charAt(0))) {
      return false;
    }
    return key.chars().allMatch(c -> Character.isLetterOrDigit(c)) || key.contains(" ");
  }

  /**
   * This method overrides the prettyPrint method in the JsonNode.
   *
   * <p>Returns a nicely formatted string representation of this
   * JsonObject with proper indentation. Handles nested objects and
   * arrays with 2-space indentation.
   * Special handling for string values to keep them on the same line.
   *
   * @return A formatted JSON string with proper indentation and newlines
   */
  @Override
  public String prettyPrint() {
    if (jsonObject.isEmpty()) {
      return "{\n}";
    }

    StringBuilder prettyJson = new StringBuilder();

    prettyJson.append("{\n");
    for (int idx = 0; idx < jsonObject.size(); idx++) {
      Pair kv = jsonObject.get(idx);

      prettyJson.append("  ")
              .append("\"")
              .append(kv.getKey())
              .append("\":");
      if (!(kv.getValue() instanceof JsonString)) {
        prettyJson.append("\n  ");
        for (char c : kv.getValue().prettyPrint().toCharArray()) {
          prettyJson.append(c);
          if (c == '\n') {
            prettyJson.append("  ");
          }
        }
      }
      else {
        prettyJson.append(kv.getValue().prettyPrint());
      }

      if (idx < jsonObject.size() - 1) {
        prettyJson.append(',');
      }

      prettyJson.append("\n");
    }
    prettyJson.append("}");

    return prettyJson.toString();
  }

  /**
   * This method overrides the default equals method of JsonNode Class.
   *
   * <p>It checks if two JsonObjects are equal based on their content,
   * regardless of order. Objects are equal if they have the same keys
   * and for each key, the same set of values (including duplicates).
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
    if (!(o instanceof JsonObject)) {
      return false;
    }
    JsonObject other = (JsonObject) o;

    // Check sizes of the objects
    if (this.jsonObject.size() != other.jsonObject.size()) {
      return false;
    }

    // Count the frequencies of each key and each value for a key in this.jsonObject
    Map<String, Map<JsonNode, Integer>> thisCount = countKeyValueFreq(this.jsonObject);

    // Count the frequencies of each key and each value for a key in other.jsonObject
    Map<String, Map<JsonNode, Integer>> otherCount = countKeyValueFreq(other.jsonObject);

    // Comparing the two maps by checking for keys similarity
    if (!(thisCount.keySet().equals(otherCount.keySet()))) {
      return false;
    }

    // Comparing the two maps by checking value frequencies for each key.
    for (String key : thisCount.keySet()) {
      Map<JsonNode, Integer> thisInnerMap = thisCount.get(key);
      Map<JsonNode, Integer> otherInnerMap = otherCount.get(key);

      if (!(thisInnerMap.equals(otherInnerMap))) {
        return false;
      }
    }
    return true;
  }


  /**
   * Overriding hashcode method has as well to work
   * properly with Hashing.
   *
   *
   * @return An int denoting the hashcode.
   */
  @Override
  public int hashCode() {
    // Create a frequency map.
    Map<String, Map<JsonNode, Integer>> countMap = countKeyValueFreq(this.jsonObject);
    return countMap.hashCode();
  }


  /**
   * This is a private method that stores the key and their value anf its frequency
   * in a HashMap and return the computed map.
   *
   * @param jsonObj The Json Object List< Pair > on which counting is to be done.
   * @return a Map< String, Map< JsonNode, Integer>> that contains the count
   *          of each value for a key.
   */
  private Map<String, Map<JsonNode, Integer>> countKeyValueFreq(List<Pair> jsonObj) {
    // Create a frequency map for consistent hashing
    Map<String, Map<JsonNode, Integer>> countMap = new HashMap<>();
    for (Pair pair : jsonObj) {
      String key = pair.getKey();
      JsonNode value = pair.getValue();

      if (countMap.containsKey(key)) {
        Map<JsonNode, Integer> innerMap = countMap.get(key);
        if (innerMap.containsKey(value)) {
          innerMap.compute(value, (v, count) -> count + 1);
        } else {
          innerMap.put(value, 1);
        }
      } else {
        Map<JsonNode, Integer> innerMap = new HashMap<>();
        innerMap.put(value, 1);
        countMap.put(key, innerMap);
      }
    }

    return countMap;
  }
}
