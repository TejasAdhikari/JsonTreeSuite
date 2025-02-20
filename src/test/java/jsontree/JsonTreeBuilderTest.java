package jsontree;

import org.junit.Test;

import parser.InvalidJsonException;
import parser.JsonParser;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertNull;

/**
 * This is a test class that tests the functionality of
 * the JsonTreeBuilder class.
 */
public class JsonTreeBuilderTest {
  JsonParser<JsonNode> jsonTree;

  /**
   * This is a private method that helps the tests to input the
   * JSON string character by character to the input() method
   * of JsonValidator class.
   *
   * @param js Takes in JsonParser Object.
   * @param s  Takes in the string to be passed to the object's input method.
   * @throws InvalidJsonException if the input method throws one.
   */
  private void stringStreamInput(JsonParser<JsonNode> js, String s) throws InvalidJsonException {
    for (int i = 0; i < s.length(); i++) {
      js.input(s.charAt(i));
    }
  }

  @Test
  public void testConstructor() {
    jsonTree = new JsonTreeBuilder();

    assertNull(jsonTree.output());
  }


  @Test
  public void testEmptyJsonOutput() {
    jsonTree = new JsonTreeBuilder();

    assertNull(jsonTree.output());
  }

  @Test
  public void testValidJsonOutput() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String validJson = "{\n  \"name\":\"Some Name\"\n}";
    stringStreamInput(jsonTree, validJson);
    assertEquals(validJson, jsonTree.output().prettyPrint());
  }

  @Test
  public void testIncompleteJsonOutput() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{\"nam");

    assertNull(jsonTree.output());
  }

  @Test
  public void testNullThenCompleteJsonTree() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{\"nam");
    assertNull(jsonTree.output());

    stringStreamInput(jsonTree, "e\":\"Some Name\"}");
    String expectedJson = "{\n  \"name\":\"Some Name\"\n}";
    assertEquals(expectedJson, jsonTree.output().prettyPrint());
  }

  @Test
  public void testValidInput() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{\"name1\":");

    assertNull(jsonTree.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey1() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{\"name:");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey2() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{name:\"");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey3() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{name:");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKeyBeginNotLetter() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{\"1name\":");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKeySpecialCharacters() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{\"n@me\":");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKeyValueSeparation() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    stringStreamInput(jsonTree, "{\"name\",\"Some Name\"");
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputObjectEnclosedMissing() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"details\" : {" +
            "                             \"semester\": \"Spring\"," +
            "                             \"year\" : \"2020\"," +
            "}";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputObjectEnclosedSquare() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"details\" : [" +
            "                             \"semester\": \"Spring\"," +
            "                             \"year\" : \"2020\" " +
            "                           ]" +
            "}";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputArrayEnclosedMissing() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"crn\" : [\"20123\",\"32135\",\"34444\"}";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputArrayEnclosedBraces() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"crn\" : {\"20123\",\"32135\",\"34444\"}}";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputObjectSeparatedSemiColon() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{" +
            "   \"name\" : \"CS 5010\";" +
            "   \"semester\" : \"Spring\";" +
            "   \"year\" : \"2020\"" +
            "}";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputArraySeparatedSemiColon() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"crn\" : [\"20123\";\"32135\";\"34444\"]}";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputImproperEnclosure() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"crn\" : [\"20123\";\"32135\";\"34444\"}]";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputContinuousStream() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    jsonTree.input('{').input('\"').input('n').input('a')
            .input('m').input('e').input(':').input('\"');

    assertNull(jsonTree.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidInput() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{\"name:\"cs5010\"}";
    stringStreamInput(jsonTree, inputJson);

    assertNull(jsonTree.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testValidThenInvalidInput() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{}";
    stringStreamInput(jsonTree, inputJson);
    assertEquals("{\n}", jsonTree.output().prettyPrint());

    jsonTree.input('{');
    assertNull(jsonTree.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInvalidKey() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{\"name\":\"cs5010\",\"9";
    stringStreamInput(jsonTree, inputJson);
  }

  @Test
  public void testValidInput1() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"name\":\"cs5010\",\"time\":{";
    stringStreamInput(jsonTree, inputJson);

    assertNull(jsonTree.output());
  }

  @Test
  public void testValidInputJsonLong() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\n  \"scene\":\n  {\n    \"instance\":\"\"," +
            "\n    \"instance\":\"\"," +
            "\n    \"instance\":\"\"," +
            "\n    \"instance\":\"\"," +
            "\n    \"light\":\n    {\n      \"ambient\":" +
            "\n      [\n        \"0.8\",\n        \"0.8\",\n        \"0.8\"\n      ]," +
            "\n      \"spotangle\":\"180\"\n    }\n  }\n}";
    stringStreamInput(jsonTree, inputJson);

    assertEquals(inputJson, jsonTree.output().prettyPrint());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInValidInputJson() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{\"name\"}";
    stringStreamInput(jsonTree, inputJson);

    assertNull(jsonTree.output());
  }

  @Test
  public void testValidInputJsonEscapeCharacter1() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\n";
    stringStreamInput(jsonTree, inputJson);

    assertNull(jsonTree.output());
  }

  @Test
  public void testValidInputJsonEscapeCharacter2() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    assertNotEquals(null, jsonTree.input('\n'));
    assertNull(jsonTree.output());
  }

  // This method tests if another character is added after a valid input
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputJsonExtraChar() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{}";
    stringStreamInput(jsonTree, inputJson);
    assertEquals("{\n}", jsonTree.output().prettyPrint());

    assertNotEquals(null, jsonTree.input('{'));
    assertNull(jsonTree.output());
  }

  // This method tests if '{' is added after a '['
  @Test
  public void testValidInputObjectInArray() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"details\" : [{}, {}, {}] }";
    stringStreamInput(jsonTree, inputJson);
    String expectedJson = "{\n  \"details\":\n  [\n    {\n    }," +
            "\n    {\n    },\n    {\n    }\n  ]\n}";
    assertEquals(expectedJson, jsonTree.output().prettyPrint());
  }

  // This method tests if '}' is added after a value
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputWithoutValue() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"name\"}";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  // This method tests invalid value boundary case of '0'
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey4() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"0";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  // This method tests invalid value boundary case of '9'
  @Test(expected = InvalidJsonException.class)
  public void testInvalidInputKey5() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"9";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  // This method tests if key value pairs are separated  correctly
  @Test(expected = InvalidJsonException.class)
  public void testValidInputKeyValueSeparation1() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"name\"\"";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  // This method tests if key value pairs are separated  correctly
  @Test(expected = InvalidJsonException.class)
  public void testValidInputKeyValueSeparation2() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"name\":\"cs5010\",\"time\":{\"semester\":\"spring\"\"";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  // This method tests if key value pairs are separated  correctly
  @Test(expected = InvalidJsonException.class)
  public void testValidInputKeyValueSeparation3() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = ":}";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  @Test
  public void testEmptyObject() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{}";
    stringStreamInput(jsonTree, inputJson);

    assertEquals("{\n}", jsonTree.output().prettyPrint());
  }

  @Test(expected = InvalidJsonException.class)
  public void testEmptyArray() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"a\":[]}";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  @Test
  public void testEmptyString() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();
    String inputJson = "{\"a\":\"\"}";
    stringStreamInput(jsonTree, inputJson);

    assertEquals("{\n  \"a\":\"\"\n}", jsonTree.output().prettyPrint());
  }

  @Test(expected = InvalidJsonException.class)
  public void testEmptyArrayOnly() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "[]";
    stringStreamInput(jsonTree, inputJson);

    assertNull(jsonTree.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testEmptyStringOnly() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "\"\"";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  @Test(expected = InvalidJsonException.class)
  public void testInValidBraces() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{\"name\":{\"First Name\"}}";
    stringStreamInput(jsonTree, inputJson);
    assertNull(jsonTree.output());
  }

  @Test
  public void testValidArrayInputs() throws InvalidJsonException {
    jsonTree = new JsonTreeBuilder();

    String inputJson = "{\"name\":[\"\", \"\"]}";
    stringStreamInput(jsonTree, inputJson);

    assertEquals("{\n  \"name\":\n  [\n    \"\",\n    \"\"\n  ]\n}",
            jsonTree.output().prettyPrint());
  }

  @Test
  public void testTreeBuilderOutput1() throws InvalidJsonException {
    JsonParser<JsonNode> jsonTreeBuild = new JsonTreeBuilder();
    String json = "{\"name\":\"some name\",\"age\":\"some age\"}";
    for (char c : json.toCharArray()) {
      jsonTreeBuild.input(c);
    }
    JsonNode result = jsonTreeBuild.output();

    IJsonObject node = new JsonObject();
    node.add("name", new JsonString("some name"));
    node.add("age", new JsonString("some age"));

    assertEquals(((IJsonObject) result), node);
  }

  @Test
  public void testTreeBuilderOutput2() throws InvalidJsonException {
    JsonParser<JsonNode> jsonTreeBuild = new JsonTreeBuilder();
    String json = "{\"name\":\"some name\",\"age\":[\"6\", \"9\"]}";
    for (char c : json.toCharArray()) {
      jsonTreeBuild.input(c);
    }
    JsonNode result = jsonTreeBuild.output();

    IJsonObject root = new JsonObject();
    root.add("name", new JsonString("some name"));
    IJsonArray node = new JsonArray();
    node.add(new JsonString("6"));
    node.add(new JsonString("9"));
    root.add("age", node);

    assertEquals(((IJsonObject) result), root);
  }

  @Test
  public void testTreeBuilderOutput3() throws InvalidJsonException {
    JsonParser<JsonNode> jsonTreeBuild = new JsonTreeBuilder();
    String json = "{\"name\":\"some name\"," +
            "\"age\":[{\"today\":\"9\"},{\"tomorrow\":\"10\"}]}";
    for (char c : json.toCharArray()) {
      jsonTreeBuild.input(c);
    }
    JsonNode result = jsonTreeBuild.output();

    System.out.println(result.prettyPrint());

    IJsonObject root = new JsonObject();
    root.add("name", new JsonString("some name"));

    IJsonArray node = new JsonArray();
    IJsonObject innerRoot1 = new JsonObject();
    innerRoot1.add("today", new JsonString("9"));
    IJsonObject innerRoot2 = new JsonObject();
    innerRoot2.add("tomorrow", new JsonString("10"));

    node.add(innerRoot1);
    node.add(innerRoot2);

    root.add("age", node);

    System.out.println(root.prettyPrint());

    assertEquals(((IJsonObject) result), root);
  }

  @Test
  public void testEquality() throws InvalidJsonException {
    IJsonObject root = new JsonObject();
    root.add("name", new JsonString("cs5010"));
    IJsonObject innerRoot1 = new JsonObject();
    innerRoot1.add("semester", new JsonString("spring"));
    innerRoot1.add("year", new JsonString("2024"));
    root.add("time", innerRoot1);

    IJsonObject root2 = new JsonObject();
    root2.add("time", innerRoot1);
    root2.add("name", new JsonString("cs5010"));

    assertEquals(root2, root);
  }

  @Test
  public void testTreeBuilderConstructor() {
    JsonParser<JsonNode> node = new JsonTreeBuilder();

    assertNull(node.output());
  }
}