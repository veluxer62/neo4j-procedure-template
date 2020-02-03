package learningneo4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class StringHackingTest {

    public static Stream<Arguments> initializeTestData() {
        return Stream.of(
                Arguments.arguments("John Smith", "Smith"),
                Arguments.arguments("Joe Smith", "Smith"),
                Arguments.arguments("Juan Carlos", "Carlos"),
                Arguments.arguments("Mike Jones", "Jones"),
                Arguments.arguments("Mike", "Mike"),
                Arguments.arguments("Juan", "Juan"),
                Arguments.arguments(null, null)
        );
    }

    @ParameterizedTest
    @MethodSource("initializeTestData")
    public void getLastWord_return_last_name_if_given_full_name(
            String fullName,
            String expected) {
        StringHacking sut = new StringHacking();
        String actual = sut.getLastWord(fullName);
        assertEquals(expected, actual);
    }

    @Test
    public void getLastWord_method_has_annotations_correctly() throws NoSuchMethodException {
        Class<StringHacking> clazz = StringHacking.class;
        Method method = clazz.getMethod("getLastWord", String.class);

        UserFunction userFunction = method.getAnnotation(UserFunction.class);
        Description description = method.getAnnotation(Description.class);

        assertNotNull(userFunction);
        assertNotNull(description);
        assertEquals("Returns the last word of a string", description.value());
    }

    @Test
    public void getLastWord_method_of_argument_has_Name_annotation() throws NoSuchMethodException {
        Class<StringHacking> clazz = StringHacking.class;
        Method method = clazz.getMethod("getLastWord", String.class);
        Parameter[] parameters = method.getParameters();
        Parameter parameter = parameters[0];

        Name actual = parameter.getAnnotation(Name.class);

        assertNotNull(actual);
        assertEquals("aStr", actual.value());
    }
}