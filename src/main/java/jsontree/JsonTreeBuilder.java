package jsontree;

import java.util.Objects;
import java.util.Stack;

import parser.InvalidJsonException;
import parser.JsonParser;


/**
 * This is a class that implements JsonParser interface as JsonParser\< JsonNode \>.
 * It builds a tree representation of the input JSON data.
 *
 * <p>We have a JsonNode representing the root of the JsonNode object,
 * and 'json' as a string that stores the characters that have been sent
 * into the input method.
 *
 * <p>The json String can be in one of the 4 status i.e. "Empty", "Valid",
 * "Incomplete", "Invalid".
 *
 * <p>This class implements the input() method in a way that the input is taken
 * character by character and the function returns the same object after
 * validation.
 *
 * <p>The output function just returns the root JsonNode of the parsed tree
 * if parsing is complete and valid, and null if parsing is incomplete or invalid.
 *
 */
public class JsonTreeBuilder implements JsonParser<JsonNode> {
  private JsonNode root;
  private String json;
  private final String[] status;
  private String currentStatus;
  private int countBraces;
  private Stack<Character> allBrackets;
  private boolean inValue;
  private boolean inKey;
  private int separatedCount;
  private int keyCount;
  private int keyValueCount;
  private Stack<JsonNode> nodes;
  private Stack<String> keyStack;
  private int valueStartIndex; // Track start of current value
  private int keyStartIndex;   // Track start of current key


  /**
   * This is a constructor for the JsonTreeBuilder class.
   * It initializes the root attribute with a JsonObject object.
   * json attribute to empty string and the 'currentStatus'  as 'Empty'
   * and the other private variables.
   */
  public JsonTreeBuilder() {
    this.status = new String[]{"Empty", "Valid", "Incomplete", "Invalid"};
    this.root = new JsonObject();
    this.json = "";
    this.currentStatus = status[0];
    this.countBraces = 0;
    this.allBrackets = new Stack<>();
    this.inValue = false;
    this.inKey = false;
    this.separatedCount = 0;
    this.keyCount = 0;
    this.keyValueCount = 0;

    // For new tree building
    this.nodes = new Stack<>();
    this.keyStack = new Stack<>();
    this.valueStartIndex = -1;
    this.keyStartIndex = -1;
  }

  /**
   * Check if the json is already valid, if so no need to check validity.
   * Just check if the current character is ' ' or not.
   * ' ' -> allowed but anything else not allowed except escape characters.
   *
   * <p>We are checking for invalid states to save on runtime.
   *
   * <p>The character c is then sent to another method -> buildTree to
   * be built with correct hierarchy.
   *
   * @param c the input character
   * @return the current object
   * @throws InvalidJsonException defined in the parser package
   */
  @Override
  public JsonParser<JsonNode> input(char c) throws InvalidJsonException {
    // Check if the json is already valid, if so no need to check validity.
    // Just check if the current character is ' ' or not.
    // ' ' -> allowed but anything else not allowed.
    if (Objects.equals(currentStatus, status[1]) && c != ' ') {
      currentStatus = status[3];
      return this;
    }

    if (c == '\n' || c == '\t' || c == '\r' || c == '\f') {
      return this;
    }

    boolean validity = isValidJson(c);

    if (!Objects.equals(currentStatus, status[3])) {
      if (validity && (c != ' ' || (inKey || inValue))) {
        this.json += c;

        if (allBrackets.empty() && !Objects.equals(currentStatus, status[1])) {
          this.currentStatus = status[1];
        }
        else {
          this.currentStatus = status[2];
        }
      } else if (!validity) {
        this.currentStatus = status[3];
        throw new InvalidJsonException("Invalid input making the JSON invalid");
      }
    }
    return this;
  }

  /**
   * This method retrieves the root JsonNode of the parsed tree
   * if parsing is complete and valid, and null if parsing
   * is incomplete or invalid.
   *
   * @return root attribute if valid, null otherwise.
   */
  @Override
  public JsonNode output() {
    if (this.currentStatus.equals(status[1])) {
      buildTree();
      return this.root;
    }
    return null;
  }

  /**
   * This method constructs the final JSON tree by processing the stack of nodes
   * along with the keyStack.
   *
   * <p>Iteratively pops nodes from the stack and adds them to their parent nodes.
   * For array parents, adds the value directly to the array. For object parents,
   * pairs the value with a key from keyStack
   * This is repeated until only the root node remains.
   */
  private void buildTree() {
    while (nodes.size() > 1) {
      buildObjects();
    }

    this.root = nodes.peek();
  }
  
  /**
   * This method is the entry point to check if
   * the current object is valid or not.
   * If an array or a value is detected, the task is then
   * delegated to the respective methods.
   *
   *
   * @param c Takes in a character to add to the json available.
   * @return Checks with the current json present and returns
   *         "True" if valid, and "False" if invalid.
   */
  private boolean isValidJson(char c) {
    if (inValue) {
      inValue = checkValue(c);
      return true;
    } else if (inKey) {
      return isValidKey(c);
    }
    return checkValidity(c);
  }

  /**
   * This method handles a closing brace '}'.
   *
   * <p>Gather key-value pair or the object and add to the next
   * node in the nodes stack depending on it being a
   * JsonArray instance or a JsonObject Instance.
   */
  private void buildObjects() {
    JsonNode value = nodes.pop();
    JsonNode parent = nodes.peek();
    if (parent instanceof JsonArray) {
      ((IJsonArray) parent).add(value);
    } else if (!keyStack.isEmpty()) {
      String key = keyStack.pop();
      ((IJsonObject) parent).add(key, value);
    }
  }

  /**
   * This method handle a closing bracket ']'.
   *
   * <p>Add the top node to the JsonObject parent
   * that is next in the stack.
   */
  private void closeBracket() {
    JsonNode array = nodes.pop();
    JsonNode parent = nodes.peek();
    if (!keyStack.isEmpty()) {
      String key = keyStack.pop();
      ((IJsonObject) parent).add(key, array);
    }
  }

  /**
   * This method checks the validity of the current character
   * wrt the stored json string if it is not inside a key or value string.
   * Call closeBraces method on encountering a '}' to handle it on the spot.
   * Call closeBracket method on encountering a ']' to handle it on the spot.
   *
   * @param c Takes in a character to add to the json available.
   * @return Checks with the current json present and returns
   *         "True" if valid, and "False" if invalid.
   */
  private boolean checkValidity(char c) {
    switch (c) {
      /* A new object is allowed in an array only if the json
      string is empty or
      '{' is preceded by ':'.*/
      case '{' :
        if (json.isEmpty()
                || json.charAt(json.length() - 1) == '['
                || json.charAt(json.length() - 1) == ','
                || json.charAt(json.length() - 1) == ':') {
          allBrackets.push(c);
          keyValueCount += 1;
          countBraces += 1;
          // Push a new JsonObject node in the nodes stack to use later.
          nodes.push(new JsonObject());
          return true;
        }
        break;
      /* An object is allowed to be closed if all '[' and '"' are closed.
      This is checked before entering switch case.
      Should be preceded by '"' or ']'*/
      case '}' :
        if (!allBrackets.isEmpty() && allBrackets.peek() == '{'
                && (json.charAt(json.length() - 1) == '{'
                || json.charAt(json.length() - 1) == ']'
                || json.charAt(json.length() - 1) == '}'
                || (json.charAt(json.length() - 1) == '\"'
                // Check if ':' is present before '}' and after '{'
                && json.lastIndexOf(':') > json.lastIndexOf('{')))) {
          allBrackets.pop();
          if (nodes.size() > 1) {
            buildObjects();
          }
          return true;
        }
        break;
      // A new array is allowed only if '[' is preceded by ':' or '[' or ','
      // And it should be inside "{}".
      case '[' :
        if (countBraces >= 1 && (json.charAt(json.length() - 1) == '['
                || json.charAt(json.length() - 1) == ':'
                || (json.charAt(json.length() - 1) == ','
                && !allBrackets.isEmpty() && allBrackets.peek() == '['))) {
          allBrackets.push(c);
          // Push a new JsonArray node in the nodes stack to use later.
          nodes.push(new JsonArray());
          return true;
        }
        break;
      //  A new array is allowed only if '[' is preceded by ':' or '[' or ','.
      case ']' :
        if (!allBrackets.isEmpty() && allBrackets.peek() == '['
                && (json.charAt(json.length() - 1) == '\"'
                || json.charAt(json.length() - 1) == ']'
                || json.charAt(json.length() - 1) == '}')) {
          allBrackets.pop();
          if (nodes.size() > 1) {
            closeBracket();
          }
          return true;
        }
        break;
      /* A new string is allowed only if '"' is preceded by ':',
      ',', '['.*/
      case '\"' :
        if (!allBrackets.isEmpty() && allBrackets.peek() == '{'
                && keyCount < keyValueCount
                && json.charAt(json.length() - 1) != ':'
        ) {
          keyCount += 1;
          inKey = true;
          keyStartIndex = json.length(); // Mark start of key
          return true;
        } else if (countBraces >= 1 && (json.charAt(json.length() - 1) == '['
                || json.charAt(json.length() - 1) == ':'
                || json.charAt(json.length() - 1) == ',')) {
          inValue = true;
          valueStartIndex = json.length(); // Mark start of value
          return true;
        }
        break;
      case ',' :
        if (separatedCount == keyCount
                && (json.charAt(json.length() - 1) == '}'
                || json.charAt(json.length() - 1) == ']'
                || json.charAt(json.length() - 1) == '\"')) {
          if (!allBrackets.isEmpty() && allBrackets.peek() == '{') {
            keyValueCount += 1;
          }
          return true;
        }
        break;
      case ':' :
        if (!allBrackets.isEmpty() && allBrackets.peek() == '{'
                && json.charAt(json.length() - 1) == '\"') {
          separatedCount += 1;
          return true;
        }
        break;
      case ' ':
        return true;
      default:
        break;
    }
    return false;
  }

  /**
   * This method checks if the value string is ending.
   *
   * <p>It gets string value as a JsonString value to be stored in the
   * root's hierarchy.
   *
   * @param c Takes in a character to add to the json available.
   * @return Checks with the current json present and returns
   *         "True" if value is ending, and "False" otherwise.
   */
  private boolean checkValue(char c) {
    // Get the value as JsonString
    if (c == '\"') {
      String str = json.substring(valueStartIndex + 1);
      JsonString value = new JsonString(str);
      addToJsonNode(value);
      valueStartIndex = -1;
    }
    return c != '\"';
  }

  /**
   * This method adds a new JSON node to the tree being built
   * in the nodes stack.
   *
   * <p>Handles different cases based on the current parent node,
   * If stack is empty, pushes node as potential root. For array parents,
   * adds node as next array element. For object parents, If key is available,
   * it adds the node with the key and if no key, it pushes the node to stack
   *
   * @param node The JsonNode to be added to the nodes stack.
   */
  private void addToJsonNode(JsonNode node) {
    if (nodes.isEmpty()) {
      nodes.push(node);
      return;
    }

    JsonNode parent = nodes.peek();
    // parent can either be an JsonArray or an JsonObject, but not a JsonString
    if (parent instanceof JsonArray) {
      ((IJsonArray) parent).add(node);
    } else if (parent instanceof JsonObject) {
      if (!keyStack.isEmpty()) {
        String key = keyStack.pop();
        ((IJsonObject) parent).add(key, node);
      } else {
        nodes.push(node);
      }
    }
  }

  /**
   * This method checks if the key is valid wrt
   * the stored json.
   *
   * <p>It gets the String key to be stored in the root's
   * hierarchy.
   *
   * @param c Takes in a character to add to the json available.
   * @return Checks with the current json present and returns
   *         "True" if valid, and "False" if invalid.
   */
  private boolean isValidKey(char c) {
    if ((Character.isDigit(c) && json.charAt(json.length() - 1) != '\"')
            || Character.isLetter(c)) {
      return true;
    } else if (c == '\"') {
      String key = json.substring(keyStartIndex + 1, json.length());
      keyStack.push(key);
      keyStartIndex = -1; // Reset start index
      inKey = false;
      return true;
    }
    return false;
  }
}
