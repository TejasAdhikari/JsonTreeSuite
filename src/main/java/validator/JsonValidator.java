package validator;

import java.util.Objects;
import java.util.Stack;

import parser.InvalidJsonException;
import parser.JsonParser;


/**
 * This is a class that implements JsonParser interface as JsonParser\< String \>.
 *
 * <p>We have 'json' as a string that stores the characters that have been sent
 * into the input method.
 *
 * <p>The json String can be in one of the 4 status i.e. "Empty", "Valid",
 * "Incomplete", "Invalid".
 *
 * <p>This class implements the input() method in a way that the input is taken
 * character by character and the function returns the same object after
 * validation.
 *
 * <p>The output function just returns the current status of the json
 * developed by the given inputs.
 *
 */
public class JsonValidator implements JsonParser<String> {
  private String json;
  private final String[] status = { "Empty", "Valid", "Incomplete", "Invalid" };
  private String currentStatus;
  private int countBraces;
  private Stack<Character> allBrackets;
  private boolean inValue;
  private boolean inKey;
  private int separatedCount;
  private int keyCount;
  private int keyValueCount;

  /**
   * This is the constructor for JsonValidator class.
   * It initializes the 'json' string, 'currentStatus'  as 'Empty'
   * and the other private variables.
   */
  public JsonValidator() {
    this.json = "";
    this.currentStatus = status[0];
    this.countBraces = 0;
    this.allBrackets = new Stack<>();
    this.inValue = false;
    this.inKey = false;
    this.separatedCount = 0;
    this.keyCount = 0;
    this.keyValueCount = 0;
  }

  /**
   * Check if the json is already valid, if so no need to check validity.
   * Just check if the current character is ' ' or not.
   * ' ' -> allowed but anything else not allowed except escape characters.
   *
   * <p>We are prematurely checking for invalid states to same on runtime.
   *
   * @param c the input character
   * @return the current object
   * @throws InvalidJsonException defined in the parser package
   */
  @Override
  public JsonParser<String> input(char c) throws InvalidJsonException {
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

  @Override
  public String output() {
    return "Status:" + currentStatus;
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
   * This method checks the validity of the current character
   * wrt the stored json string if it is not inside a key or value string.
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
        if (json.isEmpty() || json.charAt(json.length() - 1) == '['
                || json.charAt(json.length() - 1) == ',' || json.charAt(json.length() - 1) == ':') {
          allBrackets.push(c);
          keyValueCount += 1;
          countBraces += 1;
          return true;
        }
        break;
      /* An object is allowed to be closed if all '[' and '"' are closed.
      This is checked before entering switch case.
      Should be preceded by '"' or ']'*/
      case '}' :
        if (!allBrackets.isEmpty() && allBrackets.peek() == '{'
                && (json.charAt(json.length() - 1) == '{' || json.charAt(json.length() - 1) == ']'
                || json.charAt(json.length() - 1) == '}' || (json.charAt(json.length() - 1) == '\"'
                // Check if ':' is present before '}' and after '{'
                && json.lastIndexOf(':') > json.lastIndexOf('{')))) {
          allBrackets.pop();
          return true;
        }
        break;
      // A new array is allowed only if '[' is preceded by ':' or '[' or ','
      // And it should be inside "{}".
      case '[' :
        if (countBraces >= 1 && (json.charAt(json.length() - 1) == '['
                || json.charAt(json.length() - 1) == ':' || (json.charAt(json.length() - 1) == ','
                && !allBrackets.isEmpty() && allBrackets.peek() == '['))) {
          allBrackets.push(c);
          return true;
        }
        break;
      //  A new array is allowed only if '[' is preceded by ':' or '[' or ','.
      case ']' :
        if (!allBrackets.isEmpty() && allBrackets.peek() == '['
                && (json.charAt(json.length() - 1) == '\"' || json.charAt(json.length() - 1) == ']'
                || json.charAt(json.length() - 1) == '}')) {
          allBrackets.pop();
          return true;
        }
        break;
      /* A new string is allowed only if '"' is preceded by ':',
      ',', '['.*/
      case '\"' :
        if (!allBrackets.isEmpty() && allBrackets.peek() == '{' && keyCount < keyValueCount
                && json.charAt(json.length() - 1) != ':') {
          keyCount += 1;
          inKey = true;
          return true;
        } else if (countBraces >= 1 && (json.charAt(json.length() - 1) == '['
                || json.charAt(json.length() - 1) == ':'
                || json.charAt(json.length() - 1) == ',')) {
          inValue = true;
          return true;
        }
        break;
      case ',' :
        if (separatedCount == keyCount && (json.charAt(json.length() - 1) == '}'
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
   * @param c Takes in a character to add to the json available.
   * @return Checks with the current json present and returns
   *         "True" if value is ending, and "False" otherwise.
   */
  private boolean checkValue(char c) {
    return c != '\"';
  }

  /**
   * This method checks if the key is valid wrt
   * the stored json.
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
      inKey = false;
      return true;
    }
    return false;
  }
}
