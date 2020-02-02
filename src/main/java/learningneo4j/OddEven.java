package learningneo4j;

import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

public class OddEven {
    @UserFunction
    @Description("Returns true if the given number is odd")
    public Boolean isOdd(@Name("number") Long number) {
        if (number == null) return false;
        return number % 2 == 1;
    }

    @UserFunction
    @Description("Returns true if the given number is even")
    public Boolean isEven(@Name("number") Long number) {
        if (number == null) return false;
        return number % 2 == 0;
    }
}
