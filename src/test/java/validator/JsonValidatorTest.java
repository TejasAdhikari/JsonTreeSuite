package validator;

import org.junit.Test;

import parser.InvalidJsonException;
import parser.JsonParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;


/**
 * This is a JUnit test class for the validator.JsonValidator class.
 */
public class JsonValidatorTest {
  JsonParser<String> jsonString;

  /**
   * This is a private method that helps the tests to input the
   * JSON string character by character to the input() method
   * of JsonValidator class.
   *
   * @param js Takes in JsonParser Object.
   * @param s  Takes in the string to be passed to the object's input method.
   * @throws InvalidJsonException if the input method throws one.
   */
  private void stringStreamInput(JsonParser<String> js, String s) throws InvalidJsonException {
    for (int i = 0; i < s.length(); i++) {
      js.input(s.charAt(i));
    }
  }

  @Test
  public void testConstructor() {
    jsonString = new JsonValidator();

    assertEquals("Status:Empty", jsonString.output());
  }


  @Test
  public void testEmptyJsonOutput() {
    jsonString = new JsonValidator();
    String expectedOutput = "Status:Empty";
    assertEquals(expectedOutput, jsonString.output());
  }

  @Test
  public void testValidJsonOutput() throws InvalidJsonException {
    jsonString = new JsonValidator();
    String validJson = "{\"name\": \"Some Name\"}";
    stringStreamInput(jsonString, validJson);
    String expectedOutput = "Status:Valid";
    assertEquals(expectedOutput, jsonString.output());
  }

  @Test
  public void testIncompleteJsonOutput() throws InvalidJsonException {
    jsonString = new JsonValidator();
    stringStreamInput(jsonString, "{\"nam");
    String expectedOutput = "Status:Incomplete";
    assertEquals(expectedOutput, jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey1() throws InvalidJsonException {
    jsonString = new JsonValidator();
    stringStreamInput(jsonString, "{\"name:");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey2() throws InvalidJsonException {
    jsonString = new JsonValidator();
    stringStreamInput(jsonString, "{name:\"");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey3() throws InvalidJsonException {
    jsonString = new JsonValidator();
    stringStreamInput(jsonString, "{name:");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKeyBeginNotLetter() throws InvalidJsonException {
    jsonString = new JsonValidator();
    stringStreamInput(jsonString, "{\"1name\":");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKeySpecialCharacters() throws InvalidJsonException {
    jsonString = new JsonValidator();
    stringStreamInput(jsonString, "{\"n@me\":");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKeyValueSeparation() throws InvalidJsonException {
    jsonString = new JsonValidator();
    stringStreamInput(jsonString, "{\"name\",\"Some Name\"");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputObjectEnclosedMissing() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"details\" : {" +
            "                             \"semester\": \"Spring\"," +
            "                             \"year\" : \"2020\"," +
            "}";
    stringStreamInput(jsonString, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputObjectEnclosedSquare() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"details\" : [" +
            "                             \"semester\": \"Spring\"," +
            "                             \"year\" : \"2020\" " +
            "                           ]" +
            "}";
    stringStreamInput(jsonString, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputArrayEnclosedMissing() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"crn\" : [\"20123\",\"32135\",\"34444\"}";
    stringStreamInput(jsonString, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputArrayEnclosedBraces() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"crn\" : {\"20123\",\"32135\",\"34444\"}}";
    stringStreamInput(jsonString, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputObjectSeparatedSemiColon() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{" +
            "   \"name\" : \"CS 5010\";" +
            "   \"semester\" : \"Spring\";" +
            "   \"year\" : \"2020\"" +
            "}";
    stringStreamInput(jsonString, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputArraySeparatedSemiColon() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"crn\" : [\"20123\";\"32135\";\"34444\"]}";
    stringStreamInput(jsonString, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputImproperEnclosure() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"crn\" : [\"20123\";\"32135\";\"34444\"}]";
    stringStreamInput(jsonString, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputContinuousStream() throws InvalidJsonException {
    jsonString = new JsonValidator();

    jsonString.input('{').input('\"').input('n').input('a')
            .input('m').input('e').input(':').input('\"');

    assertEquals("Invalid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInput() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name:\"cs5010\"}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Invalid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidKey() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\":\"cs5010\",\"9";
    stringStreamInput(jsonString, inputJson);
  }

  @Test
  public void testValidInput1() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\":\"cs5010\",\"time\":{";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Incomplete", jsonString.output());
  }

  @Test
  public void testValidInputJsonLong() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{ \"scene\": { \"instance\":\"\" ," +
            "                           \"instance\":\"\" ," +
            "                           \"instance\":\"\" ," +
            "                           \"instance\":\"\" ," +
            "                           \"light\": { \"ambient\": [\"0.8\",\"0.8\",\"0.8\"] ," +
            "                                        \"diffuse\": [\"0.8\",\"0.8\",\"0.8\"] ," +
            "                                        \"specular\": [\"0.8\",\"0.8\",\"0.8\"] ," +
            "                                        \"spotangle\":\"180\" }}}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Valid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInValidInputJson() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\"}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Invalid", jsonString.output());
  }

  @Test
  public void testValidInputJsonEscapeCharacter1() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\n";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Incomplete", jsonString.output());
  }

  @Test
  public void testValidInputJsonEscapeCharacter2() throws InvalidJsonException {
    jsonString = new JsonValidator();

    assertNotEquals(null, jsonString.input('\n'));
    assertEquals("Status:Empty", jsonString.output());
  }

  // This method tests if another character is added after a valid input
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputJsonExtraChar() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{}";
    stringStreamInput(jsonString, inputJson);

    assertNotEquals(null, jsonString.input('{'));
    assertEquals("Status:Invalid", jsonString.output());
  }

  // This method tests if '{' is added after a '['
  @Test
  public void testValidInputObjectInArray() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"details\" : [{}, {}, {}] }";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Valid", jsonString.output());
  }

  // This method tests if '}' is added after a value
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputWithoutValue() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\"}";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Invalid", jsonString.output());
  }

  // This method tests invalid value
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputValue1() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"1name\" : \"Some Name\"}";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Invalid", jsonString.output());
  }

  // This method tests invalid value boundary case of '0'
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey4() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"0";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Invalid", jsonString.output());
  }

  // This method tests invalid value boundary case of '9'
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey5() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"9";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Invalid", jsonString.output());
  }

  // This method tests if ']' is added after a ']'
  @Test
  public void testValidInputArrayInArray() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"details\" : [[\"1\", \"2\"], " +
            "                           [\"1\", \"2\"], " +
            "                           [\"1\", \"2\"]] }";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Valid", jsonString.output());
  }

  // This method tests if key value pairs are separated  correctly
  @Test(expected = InvalidJsonException.class)
  public void testValidInputKeyValueSeparation1() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\"\"";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Invalid", jsonString.output());
  }

  // This method tests if key value pairs are separated  correctly
  @Test(expected = InvalidJsonException.class)
  public void testValidInputKeyValueSeparation2() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\":\"cs5010\",\"time\":{\"semester\":\"spring\"\"";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Invalid", jsonString.output());
  }

  // This method tests if key value pairs are separated  correctly
  @Test(expected = InvalidJsonException.class)
  public void testValidInputKeyValueSeparation3() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = ":}";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Invalid", jsonString.output());
  }

  @Test
  public void testEmptyObject() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Valid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testEmptyArray() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"a\":[]}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Invalid", jsonString.output());
  }

  @Test
  public void testEmptyString() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"a\":\"\"}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Valid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testEmptyArrayOnly() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "[]";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Invalid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testEmptyStringOnly() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "\"\"";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Invalid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInValidBraces() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\":{\"First Name\"}}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Invalid", jsonString.output());
  }

  @Test
  public void testValidArrayInputs() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{\"name\":[\"\", \"\"]}";
    stringStreamInput(jsonString, inputJson);

    assertEquals("Status:Valid", jsonString.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testValidThenInvalidInput() throws InvalidJsonException {
    jsonString = new JsonValidator();

    String inputJson = "{}";
    stringStreamInput(jsonString, inputJson);
    assertEquals("Status:Valid", jsonString.output());

    jsonString.input('{');
    assertEquals("Status:Invalid", jsonString.output());
  }
}