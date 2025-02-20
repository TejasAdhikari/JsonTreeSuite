package validator;

import parser.AbstractJsonParser;


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
public class JsonValidator extends AbstractJsonParser<String> {
  /**
   * This is the constructor for JsonValidator class.
   * It initializes the 'json' string, 'currentStatus'  as 'Empty'
   * and the other attributes in the super class using super.
   */
  public JsonValidator() {
    super();
  }

  /**
   * This method gives the output of the current status
   * of the json being built.
   *
   * @return a String value denoting the status.
   */
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
  @Override
  protected boolean isValidJson(char c) {
    if (inValue) {
      inValue = checkValue(c);
      return true;
    } else if (inKey) {
      return isValidKey(c);
    }
    return checkValidity(c);
  }

  /**
   * A new object is allowed in an array only if the json
   * string is empty or
   * '{' is preceded by ':'.
   *
   * @return a boolean value indicating the validity of the opening brace.
   */
  @Override
  protected boolean checkBraceOpening() {
    boolean flag = false;
    if (!json.isEmpty()) {
      char lastChar = json.charAt(json.length() - 1);
      if (lastChar == '[' || lastChar == ',' || lastChar == ':') {
        flag = true;
      }
    }
    if (flag || json.isEmpty()) {
      allBrackets.push('{');
      keyValueCount += 1;
      countBraces += 1;
      return true;
    }
    return false;
  }

  /**
   * An object is allowed to be closed if all '[' and '"' are closed.
   * This is checked before entering switch case.
   * Should be preceded by '"' or ']'.
   *
   * @return a boolean value indicating the validity of the closing brace.
   */
  @Override
  protected boolean checkBraceClosing() {
    if (!json.isEmpty()) {
      char lastChar = json.charAt(json.length() - 1);
      // Check if ':' is present before '}' and after '{'
      if (!allBrackets.isEmpty() && allBrackets.peek() == '{'
              && (lastChar == '{' || lastChar == ']' || lastChar == '}' || (lastChar == '\"'
              && json.lastIndexOf(':') > json.lastIndexOf('{')))) {
        allBrackets.pop();
        return true;
      }
    }
    return false;
  }

  /**
   * A new array is allowed only if '[' is preceded by ':' or '[' or ','
   * and it should be inside "{}".
   *
   * @return a boolean value indicating the validity of the opening bracket.
   */
  @Override
  protected boolean checkArrayOpening() {
    if (!json.isEmpty()) {
      char lastChar = json.charAt(json.length() - 1);
      if (countBraces >= 1 && (lastChar == '[' || lastChar == ':'
              || (lastChar == ',' && !allBrackets.isEmpty() && allBrackets.peek() == '['))) {
        allBrackets.push('[');
        return true;
      }
    }
    return false;
  }

  /**
   * An array is allowed to be closed if all the elements inside are
   * closed and separated properly.
   *
   * @return a boolean value indicating the validity of the closing Bracket.
   */
  @Override
  protected boolean checkArrayClosing() {
    if (!json.isEmpty()) {
      char lastChar = json.charAt(json.length() - 1);
      if (!allBrackets.isEmpty() && allBrackets.peek() == '['
              && (lastChar == '\"' || lastChar == ']' || lastChar == '}')) {
        allBrackets.pop();
        return true;
      }
    }
    return false;
  }

  /**
   * A new string is allowed only if '"' is preceded by ':', ',', '['.
   *
   * <p>This method also checks if the current character goes in a key or
   * a value.
   * Adjusts the counters and index markers accordingly
   *
   * @return a boolean value indicating the validity of '\"'.
   */
  @Override
  protected boolean checkStringEntries() {
    if (!json.isEmpty()) {
      char lastChar = json.charAt(json.length() - 1);
      if (!allBrackets.isEmpty() && allBrackets.peek() == '{'
              && keyCount < keyValueCount && lastChar != ':') {
        keyCount += 1;
        inKey = true;
        return true;
      } else if (countBraces >= 1 && (lastChar == '['
              || lastChar == ':' || lastChar == ',')) {
        inValue = true;
        return true;
      }
    }
    return false;
  }

  /**
   * This method checks if the separation character is placed
   * correctly at the current position.
   *
   * @return a boolean value indicating the validity of the ',' separation.
   */
  @Override
  protected boolean checkSeparation() {
    if (!json.isEmpty()) {
      char lastChar = json.charAt(json.length() - 1);
      if (separatedCount == keyCount && (lastChar == '}'
              || lastChar == ']' || lastChar == '\"')) {
        if (!allBrackets.isEmpty() && allBrackets.peek() == '{') {
          keyValueCount += 1;
        }
        return true;
      }
    }
    return false;
  }

  /**
   * This method checks is the colon s placed at the correct spot
   * in the json.
   * It should separate a key-value pair, if not in value string.
   *
   * @return a boolean value indicating the validity of the ':' separation.
   */
  @Override
  protected boolean checkColon() {
    if (!allBrackets.isEmpty() && allBrackets.peek() == '{'
            && json.charAt(json.length() - 1) == '\"') {
      separatedCount += 1;
      return true;
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
