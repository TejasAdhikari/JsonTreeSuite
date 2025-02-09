package jsontree;

/**
 * This is an abstract class that represents a node
 * in the Json hierarchy. It is initially empty.
 *
 * <p>Json Node might represent a Simple Json String,
 * a Json Array or a Json Object.
 */
abstract public class JsonNode {

  protected String prettyPrint() {
    return "";
  }

  public boolean isEqualsString(Object o) {
    return false;
  }
  public boolean isEqualsArray(Object o) {
    return false;
  }
  public boolean isEqualsObject(Object o) {
    return false;
  }
}
