package parser;

import de.geo2web.parser.Number;
import de.geo2web.parser.Operand;
import de.geo2web.parser.function.Function;
import org.junit.Test;

import java.util.Arrays;

import static org.junit.Assert.*;

public class FunctionsTest {

    @Test(expected = IllegalArgumentException.class)
    public void testFunctionNameNull() {
        Function f = new Function(null) {
            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFunctionNameEmpty() {
        Function f = new Function("") {
            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test
    public void testFunctionNameZeroArgs() {
        Function f = new Function("foo", 0) {
            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
        assertEquals(0f, ((Number)f.apply()).getValue(), 0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testFunctionNameNegativeArgs() {
        Function f = new Function("foo", -1) {
            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalFunctionName1() {
        Function f = new Function("1foo") {
            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalFunctionName2() {
        Function f = new Function("_&oo") {
            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test(expected = IllegalArgumentException.class)
    public void testIllegalFunctionName3() {
        Function f = new Function("o+o") {
            @Override
            public Operand apply(Operand... args) {
                return new Number(0);
            }
        };
    }

    @Test
    public void testGetAllowedFunctionChars() {
        char[] chars = Function.getAllowedFunctionCharacters();
        assertEquals(53, chars.length);
        Arrays.sort(chars);
        assertTrue(Arrays.binarySearch(chars, 'a') > -1);
        assertTrue(Arrays.binarySearch(chars, 'b') > -1);
        assertTrue(Arrays.binarySearch(chars, 'c') > -1);
        assertTrue(Arrays.binarySearch(chars, 'd') > -1);
        assertTrue(Arrays.binarySearch(chars, 'e') > -1);
        assertTrue(Arrays.binarySearch(chars, 'f') > -1);
        assertTrue(Arrays.binarySearch(chars, 'g') > -1);
        assertTrue(Arrays.binarySearch(chars, 'h') > -1);
        assertTrue(Arrays.binarySearch(chars, 'i') > -1);
        assertTrue(Arrays.binarySearch(chars, 'j') > -1);
        assertTrue(Arrays.binarySearch(chars, 'k') > -1);
        assertTrue(Arrays.binarySearch(chars, 'l') > -1);
        assertTrue(Arrays.binarySearch(chars, 'm') > -1);
        assertTrue(Arrays.binarySearch(chars, 'n') > -1);
        assertTrue(Arrays.binarySearch(chars, 'o') > -1);
        assertTrue(Arrays.binarySearch(chars, 'p') > -1);
        assertTrue(Arrays.binarySearch(chars, 'q') > -1);
        assertTrue(Arrays.binarySearch(chars, 'r') > -1);
        assertTrue(Arrays.binarySearch(chars, 's') > -1);
        assertTrue(Arrays.binarySearch(chars, 't') > -1);
        assertTrue(Arrays.binarySearch(chars, 'u') > -1);
        assertTrue(Arrays.binarySearch(chars, 'v') > -1);
        assertTrue(Arrays.binarySearch(chars, 'w') > -1);
        assertTrue(Arrays.binarySearch(chars, 'x') > -1);
        assertTrue(Arrays.binarySearch(chars, 'y') > -1);
        assertTrue(Arrays.binarySearch(chars, 'z') > -1);
        assertTrue(Arrays.binarySearch(chars, 'A') > -1);
        assertTrue(Arrays.binarySearch(chars, 'B') > -1);
        assertTrue(Arrays.binarySearch(chars, 'C') > -1);
        assertTrue(Arrays.binarySearch(chars, 'D') > -1);
        assertTrue(Arrays.binarySearch(chars, 'E') > -1);
        assertTrue(Arrays.binarySearch(chars, 'F') > -1);
        assertTrue(Arrays.binarySearch(chars, 'G') > -1);
        assertTrue(Arrays.binarySearch(chars, 'H') > -1);
        assertTrue(Arrays.binarySearch(chars, 'I') > -1);
        assertTrue(Arrays.binarySearch(chars, 'J') > -1);
        assertTrue(Arrays.binarySearch(chars, 'K') > -1);
        assertTrue(Arrays.binarySearch(chars, 'L') > -1);
        assertTrue(Arrays.binarySearch(chars, 'M') > -1);
        assertTrue(Arrays.binarySearch(chars, 'N') > -1);
        assertTrue(Arrays.binarySearch(chars, 'O') > -1);
        assertTrue(Arrays.binarySearch(chars, 'P') > -1);
        assertTrue(Arrays.binarySearch(chars, 'Q') > -1);
        assertTrue(Arrays.binarySearch(chars, 'R') > -1);
        assertTrue(Arrays.binarySearch(chars, 'S') > -1);
        assertTrue(Arrays.binarySearch(chars, 'T') > -1);
        assertTrue(Arrays.binarySearch(chars, 'U') > -1);
        assertTrue(Arrays.binarySearch(chars, 'V') > -1);
        assertTrue(Arrays.binarySearch(chars, 'W') > -1);
        assertTrue(Arrays.binarySearch(chars, 'X') > -1);
        assertTrue(Arrays.binarySearch(chars, 'Y') > -1);
        assertTrue(Arrays.binarySearch(chars, 'Z') > -1);
        assertTrue(Arrays.binarySearch(chars, '_') > -1);
    }

    @Test
    public void testCheckFunctionNames() {
        assertTrue(Function.isValidFunctionName("log"));
        assertTrue(Function.isValidFunctionName("sin"));
        assertTrue(Function.isValidFunctionName("abz"));
        assertTrue(Function.isValidFunctionName("alongfunctionnamecanhappen"));
        assertTrue(Function.isValidFunctionName("_log"));
        assertTrue(Function.isValidFunctionName("__blah"));
        assertTrue(Function.isValidFunctionName("foox"));
        assertTrue(Function.isValidFunctionName("aZ"));
        assertTrue(Function.isValidFunctionName("Za"));
        assertTrue(Function.isValidFunctionName("ZZaa"));
        assertTrue(Function.isValidFunctionName("_"));
        assertTrue(Function.isValidFunctionName("log2"));
        assertTrue(Function.isValidFunctionName("lo32g2"));
        assertTrue(Function.isValidFunctionName("_o45g2"));

        assertFalse(Function.isValidFunctionName("&"));
        assertFalse(Function.isValidFunctionName("_+log"));
        assertFalse(Function.isValidFunctionName("_k&l"));
        assertFalse(Function.isValidFunctionName("k&l"));
        assertFalse(Function.isValidFunctionName("+log"));
        assertFalse(Function.isValidFunctionName("fo-o"));
        assertFalse(Function.isValidFunctionName("log+"));
        assertFalse(Function.isValidFunctionName("perc%"));
        assertFalse(Function.isValidFunctionName("del$a"));
    }

}
