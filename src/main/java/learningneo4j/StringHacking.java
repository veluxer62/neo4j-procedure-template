package learningneo4j;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class StringHacking {
    @UserFunction
    @Description("Returns the last word of a string")
    public String getLastWord(@Name("aStr") String aStr) {
        if (aStr == null)
            return null;

        String[] wordSplit = aStr.split(" ");

        if (wordSplit.length == 1)
            return aStr;

        return wordSplit[1];
    }
}
