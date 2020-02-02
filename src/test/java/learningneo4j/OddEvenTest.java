package learningneo4j;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.neo4j.procedure.Description;
import org.neo4j.procedure.Name;
import org.neo4j.procedure.UserFunction;

import java.lang.reflect.Method;
import java.lang.reflect.Parameter;

import static org.junit.jupiter.api.Assertions.*;

class OddEvenTest {

    @ParameterizedTest
    @ValueSource(longs = {1, 3, 5, 7, 9, 15})
    public void isOdd_returns_true_if_the_given_number_is_odd(Long odd) {
        OddEven sut = new OddEven();
        Boolean actual = sut.isOdd(odd);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {2, 4, 6, 8, 12, 20})
    public void isOdd_returns_false_if_the_given_number_is_even(Long even) {
        OddEven sut = new OddEven();
        Boolean actual = sut.isOdd(even);
        assertFalse(actual);
    }

    @Test
    public void isOdd_returns_false_if_the_given_number_is_null() {
        OddEven sut = new OddEven();
        Boolean actual = sut.isOdd(null);
        assertFalse(actual);
    }

    @Test
    public void isOdd_method_has_annotations_correctly() throws NoSuchMethodException {
        Class<OddEven> clazz = OddEven.class;
        Method method = clazz.getMethod("isOdd", Long.class);

        UserFunction userFunction = method.getAnnotation(UserFunction.class);
        Description description = method.getAnnotation(Description.class);

        assertNotNull(userFunction);
        assertNotNull(description);
        assertEquals("Returns true if the given number is odd", description.value());
    }

    @Test
    public void isOdd_method_of_argument_has_Name_annotation() throws NoSuchMethodException {
        Class<OddEven> clazz = OddEven.class;
        Method method = clazz.getMethod("isOdd", Long.class);
        Parameter[] parameters = method.getParameters();
        Parameter parameter = parameters[0];

        Name actual = parameter.getAnnotation(Name.class);

        assertNotNull(actual);
        assertEquals("number", actual.value());
    }

    @ParameterizedTest
    @ValueSource(longs = {2, 4, 6, 8, 12, 20})
    public void isEven_returns_true_if_the_given_number_is_even(Long even) {
        OddEven sut = new OddEven();
        Boolean actual = sut.isEven(even);
        assertTrue(actual);
    }

    @ParameterizedTest
    @ValueSource(longs = {1, 3, 5, 7, 9, 15})
    public void isEven_returns_false_if_the_given_number_is_odd(Long odd) {
        OddEven sut = new OddEven();
        Boolean actual = sut.isEven(odd);
        assertFalse(actual);
    }

    @Test
    public void isEven_returns_false_if_the_given_number_is_null() {
        OddEven sut = new OddEven();
        Boolean actual = sut.isEven(null);
        assertFalse(actual);
    }

    @Test
    public void isEven_method_has_annotations_correctly() throws NoSuchMethodException {
        Class<OddEven> clazz = OddEven.class;
        Method method = clazz.getMethod("isEven", Long.class);

        UserFunction userFunction = method.getAnnotation(UserFunction.class);
        Description description = method.getAnnotation(Description.class);

        assertNotNull(userFunction);
        assertNotNull(description);
        assertEquals("Returns true if the given number is even", description.value());
    }

    @Test
    public void isEven_method_of_argument_has_Name_annotation() throws NoSuchMethodException {
        Class<OddEven> clazz = OddEven.class;
        Method method = clazz.getMethod("isEven", Long.class);
        Parameter[] parameters = method.getParameters();
        Parameter parameter = parameters[0];

        Name actual = parameter.getAnnotation(Name.class);

        assertNotNull(actual);
        assertEquals("number", actual.value());
    }
}