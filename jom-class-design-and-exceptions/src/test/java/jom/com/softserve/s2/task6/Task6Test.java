package jom.com.softserve.s2.task6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.lang.reflect.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

public class Task6Test {

    private static final String PACKAGE = "jom.com.softserve.s2.task6.";

    @DisplayName("Check that Classes are present")
    @ParameterizedTest
    @MethodSource("listOfClasses")
    void isTypePresent(String cl) {
        try {
            assertNotNull(Class.forName(PACKAGE + cl));
            assertEquals(cl, Class.forName(PACKAGE + cl).getSimpleName());
        } catch (ClassNotFoundException e) {
            fail("There is no class " + cl);
        }
    }

    private static Stream<Arguments> listOfClasses() {
        return Stream.of(
                Arguments.of("MyUtils"),
                Arguments.of("Rectangle"),
                Arguments.of("Circle"),
                Arguments.of("Shape")
        );
    }

    @DisplayName("Check that classes are not abstract or interfaces")
    @ParameterizedTest
    @MethodSource("listOfClass")
    void isTypeClass(String cl) {
        try {
            Class<?> clazz = Class.forName(PACKAGE + cl);
            assertTrue(!Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers()));
        } catch (ClassNotFoundException e) {
            fail("There is no " + cl + " class");
        }
    }

    private static Stream<Arguments> listOfClass() {
        return Stream.of(
                Arguments.of("MyUtils"),
                Arguments.of("Rectangle"),
                Arguments.of("Circle")
        );
    }

    @DisplayName("Check that Shape class is abstract")
    @Test
    void isTypeAbstractClass() {
        try {
            Class<?> clazz = Class.forName(PACKAGE + "Shape");
            assertTrue(Modifier.isAbstract(clazz.getModifiers()) && !Modifier.isInterface(clazz.getModifiers()));
        } catch (ClassNotFoundException e) {
            fail("Shape is not an abstract class");
        }
    }

    @DisplayName("Check that Constructors are Public")
    @ParameterizedTest
    @MethodSource("listClassesAndConstructor")
    void isConstructorPublic(String clas, String[] parameterTypesName) {
        try {
            Class<?> clazz = Class.forName(PACKAGE + clas);
            Constructor<?>[] declaredConstructors = clazz.getDeclaredConstructors();
            boolean isConstructorCorrect = false;

            for (Constructor<?> constructor : declaredConstructors) {
                final Type[] types = constructor.getGenericParameterTypes();
                final String[] parameterTypes = new String[types.length];
                for (int i = 0; i < types.length; ++i) {
                    final String[] parts = types[i].getTypeName().split("\\.");
                    parameterTypes[i] = parts[parts.length - 1];
                }
                if (Arrays.equals(parameterTypes, parameterTypesName)) {
                    isConstructorCorrect = true;
                    assertTrue(Modifier.isPublic(constructor.getModifiers()));
                    break;
                }
            }
            assertTrue(isConstructorCorrect, "Constructor is not correct");
        } catch (ClassNotFoundException e) {
            fail("There is no class " + clas);
        }
    }

    private static Stream<Arguments> listClassesAndConstructor() {
        return Stream.of(
                Arguments.of("Shape", new String[]{"String"}),
                Arguments.of("Circle", new String[]{"String", "double"}),
                Arguments.of("Rectangle", new String[]{"String", "double", "double"})
        );
    }

    @DisplayName("Check that classes contain required methods")
    @ParameterizedTest
    @MethodSource("listClassesAndMethods")
    void isMethodPresent(String cl, String m) {
        try {
            Method[] methods = Class.forName(PACKAGE + cl).getDeclaredMethods();
            boolean isMethod = Arrays.stream(methods).anyMatch(method -> method.getName().equals(m));
            assertTrue(isMethod, "Class does not have method " + m);
        } catch (ClassNotFoundException e) {
            fail("There is no class " + cl);
        }
    }

    private static Stream<Arguments> listClassesAndMethods() {
        return Stream.of(
                Arguments.of("MyUtils", "maxAreas"),
                Arguments.of("Shape", "getName"),
                Arguments.of("Circle", "getRadius"),
                Arguments.of("Rectangle", "getHeight"),
                Arguments.of("Rectangle", "getWidth")
        );
    }

    @DisplayName("Check that child class extends Parent")
    @ParameterizedTest
    @MethodSource("listOfChildren")
    void extendsTypeClass(String parent, String child) {
        try {
            final Class<?> parentClazz = Class.forName(PACKAGE + parent);
            final Class<?> childClazz = Class.forName(PACKAGE + child);
            assertTrue(parentClazz.isAssignableFrom(childClazz));
        } catch (ClassNotFoundException e) {
            fail("There is no class " + child + " that extends " + parent);
        }
    }

    private static Stream<Arguments> listOfChildren() {
        return Stream.of(
                Arguments.of("Shape", "Circle"),
                Arguments.of("Shape", "Rectangle")
        );
    }

    @DisplayName("Check that fields are private")
    @ParameterizedTest
    @MethodSource("listPrivateFields")
    void isFieldPrivate(String clas, String fieldName) {
        try {
            Class<?> clazz = Class.forName(PACKAGE + clas);
            Field field = clazz.getDeclaredField(fieldName);
            assertTrue(Modifier.isPrivate(field.getModifiers()));
        } catch (ClassNotFoundException e) {
            fail("There is no class " + clas);
        } catch (NoSuchFieldException e) {
            fail("There is no field " + fieldName + " in " + clas);
        }
    }

    private static Stream<Arguments> listPrivateFields() {
        return Stream.of(
                Arguments.of("Shape", "name"),
                Arguments.of("Circle", "radius"),
                Arguments.of("Rectangle", "height"),
                Arguments.of("Rectangle", "width")
        );
    }

    @DisplayName("Check that the maxAreas method works correctly")
    @Test
    void checkMaxAreas() {
        final List<Shape> originList = new ArrayList<>();
        originList.add(new Circle("Circle1", 2.0));
        originList.add(new Rectangle("Rectangle1", 2.0, 3.0));
        originList.add(new Circle("Circle2", 1.0));
        originList.add(new Rectangle("Rectangle2", 1.0, 2.0));

        final List<Shape> expected = new ArrayList<>();
        expected.add(new Circle("Circle1", 2.0));
        expected.add(new Rectangle("Rectangle1", 2.0, 3.0));

        try {
            List<Shape> actual = new MyUtils().maxAreas(originList);
            assertEquals(new HashSet<>(expected), new HashSet<>(actual));
        } catch (Exception e) {
            fail("maxAreas method does not work correctly");
        }
    }

    @DisplayName("Check if the original list is unchanged in maxAreas method")
    @Test
    void checkOriginUnchanged() {
        final List<Shape> originList = new ArrayList<>();
        originList.add(new Circle("Circle", 2.0));
        originList.add(new Rectangle("Rectangle", 2.0, 3.0));

        final List<Shape> sendList = new ArrayList<>(originList);
        try {
            new MyUtils().maxAreas(sendList);
            assertEquals(originList, sendList);
        } catch (Exception e) {
            fail("Original parameters changed in the maxAreas method");
        }
    }

    @DisplayName("Check if maxAreas handles empty list correctly")
    @Test
    void checkEmptyList() {
        final List<Shape> originList = new ArrayList<>();
        final List<Shape> expected = new ArrayList<>();

        try {
            List<Shape> actual = new MyUtils().maxAreas(originList);
            assertEquals(expected, actual);
        } catch (Exception e) {
            fail("maxAreas method does not work with empty list");
        }
    }
}
