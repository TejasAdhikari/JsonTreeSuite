package parser;

import java.util.Objects;
import java.util.Stack;

/**
 * This is an Abstract class that implements JsonParser< T > with
 * some common methods.
 * All the necessary variables are declared and initialized in the
 * constructor.
 *
 * <p>input method and the checkValidity method can be used by the
 * sub classes directly.
 * output() method and other helper functions are to be defined in the
 * concrete classes.
 *
 * @param <T> Type of Object that the concrete class will be dealing with.
 */
public abstract class AbstractJsonParser<T> implements JsonParser<T> {
  protected String json;
  protected final String[] status;
  protected String currentStatus;
  protected int countBraces;
  protected Stack<Character> allBrackets;
  protected boolean inValue;
  protected boolean inKey;
  protected int separatedCount;
  protected int keyCount;
  protected int keyValueCount;

  /**
   * This is a constructor for the JsonTreeBuilder class.
   * It initializes the root attribute with a JsonObject object.
   * json attribute to empty string and the 'currentStatus'  as 'Empty'
   * and the other private variables.
   */
  public AbstractJsonParser() {
    this.status = new String[]{"Empty", "Valid", "Incomplete", "Invalid"};
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
  public AbstractJsonParser<T> input(char c) throws InvalidJsonException {
    // Check if the json is already valid, if so no need to check validity.
    // Just check if the current character is ' ' or not.
    // ' ' -> allowed but anything else not allowed.
    if (Objects.equals(currentStatus, status[1]) && c != ' ') {
      currentStatus = status[3];
      throw new InvalidJsonException("Invalid input making the JSON invalid");
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
   * This method checks the validity of the current character
   * wrt the stored json string if it is not inside a key or value string.
   * Call closeBraces method on encountering a '}' to handle it on the spot.
   * Call closeBracket method on encountering a ']' to handle it on the spot.
   *
   * @param c Takes in a character to add to the json available.
   * @return Checks with the current json present and returns
   *         "True" if valid, and "False" if invalid.
   */
  protected boolean checkValidity(char c) {
    switch (c) {
      case '{' :
        return checkBraceOpening();
      case '}' :
        return checkBraceClosing();
      case '[' :
        return checkArrayOpening();
      case ']' :
        return checkArrayClosing();
      case '\"' :
        return checkStringEntries();
      case ',' :
        return checkSeparation();
      case ':' :
        return checkColon();
      case ' ':
        return true;
      default:
        break;
    }
    return false;
  }

  /**
   * To be implemented in the subclasses as needed.
   *
   * @return T type representing the output of the operations.
   */
  @Override
  public abstract T output();

  /**
   * This method is the entry point to check if
   * the current object is valid or not.
   * If an array or a value is detected, the task is then
   * delegated to the respective methods.
   * The exact implementation is to be done in the concrete class.
   *
   * @param c Takes in a character to add to the json available.
   * @return Checks with the current json present and returns
   *         "True" if valid, and "False" if invalid.
   */
  protected abstract boolean isValidJson(char c);

  /**
   * A new object is allowed only if the json
   * string is empty or '{' is preceded by ':'.
   * The exact implementation is to be done in the concrete class.
   *
   * @return a boolean value indicating the validity of the opening brace.
   */
  protected abstract boolean checkBraceOpening();

  /**
   * An object is allowed to be closed if all '[' and '"' are closed.
   * This is checked before entering switch case.
   * Should be preceded by '"' or ']'.
   * The exact implementation is to be done in the concrete class.
   *
   * @return a boolean value indicating the validity of the closing brace.
   */
  protected abstract boolean checkBraceClosing();

  /**
   * A new array is allowed only if '[' is preceded by ':' or '[' or ','
   * and it should be inside "{}".
   * The exact implementation is to be done in the concrete class.
   *
   * @return a boolean value indicating the validity of the opening bracket.
   */
  protected abstract boolean checkArrayOpening();

  /**
   * An array is allowed to be closed if all the elements inside are
   * closed and separated properly.
   * The exact implementation is to be done in the concrete class.
   *
   * @return a boolean value indicating the validity of the closing Bracket.
   */
  protected abstract boolean checkArrayClosing();

  /**
   * A new string is allowed only if '"' is preceded by ':', ',', '['.
   *
   * <p>This method also checks if the current character goes in a key or
   * a value.
   * Adjusts the counters and index markers accordingly.
   * The exact implementation is to be done in the concrete class.
   *
   * @return a boolean value indicating the validity of '\"'.
   */
  protected abstract boolean checkStringEntries();

  /**
   * This method checks if the separation character is placed
   * correctly at the current position.
   * The exact implementation is to be done in the concrete class.
   *
   * @return a boolean value indicating the validity of the ',' separation.
   */
  protected abstract boolean checkSeparation();

  /**
   * This method checks is the colon s placed at the correct spot
   * in the json.
   * It should separate a key-value pair, if not in value string.
   *
   * @return a boolean value indicating the validity of the ':' separation.
   */
  protected abstract boolean checkColon();

}
