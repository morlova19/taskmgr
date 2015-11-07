package to;

/**
 * Generator of the identifier of the task.
 */
public class GeneratorID {
    /**
     * Generated identifier.
     */
    private static int generatedID;
    /**
     * Gets the generated identifier.
     * @return generated identifier
     */
    public static int getGeneratedID() {
        return generatedID++;
    }

    /**
     * Sets the starting value of the identifier.
     * @param generatedID the starting value.
     */
    public static void setGeneratedID(int generatedID) {
        GeneratorID.generatedID = generatedID;
    }
}
