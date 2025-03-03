package jsontree;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;

/**
 * This is a test class to test the functionalities of
 * jsontree package.
 */
public class JsonTest {
  JsonNode node;

  @Test
  public void testJsonStringConstructor() {
    node = new JsonString("");

    assertNotEquals(null, node);
    assertEquals("\"\"", node.prettyPrint());
  }

  @Test
  public void testJsonArrayConstructor() {
    node = new JsonArray();
    assertEquals("[\n]", node.prettyPrint());
  }

  @Test
  public void testJsonObjectConstructor() {
    node = new JsonObject();
    assertEquals("{\n}", node.prettyPrint());
  }

  @Test
  public void testValidString() {
    node = new JsonString("Test");

    assertNotEquals(null, node);
    assertEquals("\"Test\"", node.prettyPrint());
  }

  @Test
  public void testValidSpecialCharactersString() {
    node = new JsonString("Speci@l Ch@racter$");
    assertEquals("\"Speci@l Ch@racter$\"", node.prettyPrint());
  }


  @Test
  public void testEmptyArray() {
    node = new JsonArray();
    assertEquals("[\n]", node.prettyPrint());
  }

  @Test
  public void testValidArraySingleEntry() {
    IJsonArray node = new JsonArray();
    node.add(new JsonString("Test"));

    assertEquals("[\n  \"Test\"\n]", node.prettyPrint());
  }

  @Test
  public void testValidArrayMultipleEntries() {
    IJsonArray node = new JsonArray();
    node.add(new JsonString("Test"));
    node.add(new JsonString("Array"));

    assertEquals("[\n  \"Test\",\n  \"Array\"\n]", node.prettyPrint());
  }

  @Test
  public void testValidNestedArray() {
    IJsonArray node = new JsonArray();
    node.add(new JsonString("Test"));

    IJsonArray node1 = new JsonArray();
    node1.add(node);
    node1.add(new JsonString("Test"));

    assertEquals("[\n  [\n    \"Test\"\n  ],\n  \"Test\"\n]", node1.prettyPrint());
  }

  @Test
  public void testEmptyObject() {
    IJsonObject node = new JsonObject();

    assertEquals("{\n}", node.prettyPrint());
  }

  @Test
  public void testValidObject1() {
    IJsonObject node = new JsonObject();
    node.add("Test", new JsonString("Object"));

    assertEquals("{\n  \"Test\":\"Object\"\n}", node.prettyPrint());
  }

  @Test
  public void testValidObject2() {
    IJsonObject node = new JsonObject();
    node.add("Test1", new JsonString("Object1"));
    node.add("Test2", new JsonString("Object2"));

    assertEquals("{\n  \"Test1\":\"Object1\",\n  \"Test2\":\"Object2\"\n}",
            node.prettyPrint());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidObjectKey1() {
    IJsonObject node = new JsonObject();
    node.add("1Test", new JsonString("Object"));

    assertNotEquals("{\n  \"1Test\":\"Object\"\n}", node.prettyPrint());
  }

  @Test(expected = IllegalArgumentException.class)
  public void testInvalidObjectKey2() {
    IJsonObject node = new JsonObject();
    node.add("T@st", new JsonString("Object"));

    assertNotEquals("{\n  \"T@st\":\"Object\"\n}", node.prettyPrint());
  }

  @Test
  public void testEqualStrings() {
    JsonString str1 = new JsonString("Test");
    JsonString str2 = new JsonString("Test");

    assertEquals(str1, str2);
  }

  @Test
  public void testUnequalStrings() {
    JsonString str1 = new JsonString("Test");
    JsonString str2 = new JsonString("Testq");

    assertNotEquals(str1, str2);
  }

  @Test
  public void testUnequalStringTypes() {
    JsonNode str1 = new JsonString("Test");
    String str2 = "Test";

    assertNotEquals(str1, str2);
  }

  @Test
  public void testUnequalTypesWithJsonString1() {
    JsonNode str = new JsonString("Test");
    JsonNode arr = new JsonArray();

    assertNotEquals(str, arr);
  }

  @Test
  public void testUnequalTypesWithJsonString2() {
    JsonNode str = new JsonString("Test");
    JsonNode obj = new JsonObject();

    assertNotEquals(str, obj);
  }

  @Test
  public void testEqualArrays1() {
    node = new JsonArray();
    JsonNode node1 = new JsonArray();

    assertEquals(node, node1);
  }

  @Test
  public void testEqualArrays2() {
    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test"));

    IJsonArray b = new JsonArray();
    b.add(new JsonString("This"));
    b.add(new JsonString("is"));
    b.add(new JsonString("a"));
    b.add(new JsonString("Test"));

    assertEquals(a, b);
  }

  @Test
  public void testUnequalArrays() {
    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test"));

    IJsonArray b = new JsonArray();
    b.add(new JsonString("a"));
    b.add(new JsonString("Test"));
    b.add(new JsonString("This"));
    b.add(new JsonString("is"));

    assertNotEquals(a, b);
  }

  @Test
  public void testUnequalTypesWithJsonArray() {
    JsonNode arr = new JsonArray();
    JsonNode obj = new JsonObject();

    assertNotEquals(obj, arr);
  }

  @Test
  public void testEqualNodes1() {
    JsonNode root1 = new JsonObject();
    JsonNode root2 = new JsonObject();

    assertEquals(root1, root2);
  }

  @Test
  public void testEqualNodes2() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    root1.add("Test", new JsonString("Equality"));
    root2.add("Test", new JsonString("Equality"));

    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test"));

    root1.add("What is this", a);
    root2.add("What is this", a);

    assertEquals(root1, root2);
  }

  @Test
  public void testEqualNodesWithKeyRepetition() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    root1.add("Test", new JsonString("Equality"));
    root2.add("Test", new JsonString("Repeat"));

    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test"));

    root1.add("Whatisthis", a);
    root2.add("Whatisthis", a);

    root1.add("Test", new JsonString("Repeat"));
    root2.add("Test", new JsonString("Equality"));

    assertEquals(root1, root2);
  }

  @Test
  public void testEqualNodesWithPairRepetition() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    root1.add("Test", new JsonString("Equality"));
    root2.add("Test", new JsonString("Equality"));

    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test"));

    root1.add("What is this?", a);
    root2.add("What is this?", a);

    root1.add("Test", new JsonString("Equality"));
    root2.add("Test", new JsonString("Equality"));

    assertEquals(root1, root2);
  }

  @Test
  public void testUnequalNodes() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    root1.add("Test1", new JsonString("Equality"));
    root2.add("Test2", new JsonString("Equality"));

    assertNotEquals(root1, root2);
  }

  @Test
  public void testUnequalNodesInArray() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test1"));

    IJsonArray b = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test2"));

    root1.add("What is this?", a);
    root2.add("What is this?", b);

    assertNotEquals(root1, root2);
  }

  @Test
  public void testEqualNodesDifferentOrder() {
    IJsonObject root1 = new JsonObject();

    root1.add("name", new JsonString("cs5010"));
    IJsonObject obj1 = new JsonObject();
    obj1.add("semester", new JsonString("spring"));
    obj1.add("year", new JsonString("2024"));
    root1.add("time", obj1);

    IJsonObject root2 = new JsonObject();

    root2.add("name", new JsonString("cs5010"));
    IJsonObject obj2 = new JsonObject();
    obj2.add("year", new JsonString("2024"));
    obj2.add("semester", new JsonString("spring"));

    root2.add("time", obj2);

    assertEquals(root1, root2);
  }

  @Test
  public void testSameNodes() {
    IJsonObject root1 = new JsonObject();

    assertEquals(root1, root1);
  }

  @Test
  public void testUnequalNodesInArray2() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test1"));


    root1.add("What is this?", a);

    assertNotEquals(root1, root2);
  }

  @Test
  public void testHashCode() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    root1.add("Test", new JsonString("Equality"));
    root2.add("Test", new JsonString("Equality"));

    IJsonArray a = new JsonArray();
    a.add(new JsonString("This"));
    a.add(new JsonString("is"));
    a.add(new JsonString("a"));
    a.add(new JsonString("Test"));

    root1.add("What is this", a);
    root2.add("What is this", a);

    assertEquals(root1, root2);
    assertEquals(root1.hashCode(), root2.hashCode());
  }

  @Test
  public void testUnequalHasCodes() {
    IJsonObject root1 = new JsonObject();
    IJsonObject root2 = new JsonObject();

    root1.add("Test1", new JsonString("Equality"));
    root2.add("Test2", new JsonString("Equality"));

    assertNotEquals(root1, root2);
    assertNotEquals(root1.hashCode(), root2.hashCode());
  }
}