package jsontree;

/**
 * This is an abstract class that represents a node
 * in the Json hierarchy. It is initially empty.
 *
 * <p>Json Node might represent a Simple Json String,
 * a Json Array or a Json Object.
 */
public abstract class JsonNode {
  /**
   * This is a method that provides a properly-formatted
   * json string for the jsontree built.
   *
   * <p>This to be implemented by each concrete class.
   *
   * @return A string denoting the properly formatted json tree.
   */
  public abstract String prettyPrint();

  /**
   * This overrides the default equals method from the object
   * class.
   *
   * <p>This is defined here to enable checking equals on
   * all the objects that are of the concrete classes
   * extending JsonNode sub classes.
   * @param o the object to be checked for equality with the
   *          object from which this is called.
   * @return a boolean value representing the equality.
   */
  @Override
  public abstract boolean equals(Object o);

  /**
   * This overrides the default hashcode method from the object
   * class. To be defined in the sub classes.
   *
   * @return an integer representing the hashcode.
   */
  @Override
  public abstract int hashCode();
}
