package arithmetic;

import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.Number;
import de.geo2web.arithmetic.Operand;
import de.geo2web.arithmetic.Vector;
import org.junit.Test;

import static arithmetic.TestUtil.*;
import static org.junit.Assert.assertEquals;

public class VectorBuilderTest {

    @Test
    public void testVectorBuilder01() {
        Operand result = new ExpressionBuilder("{2,1,5}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(1f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(5f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder02() {
        Operand result = new ExpressionBuilder("{-2,3,0}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(3f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(0f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder03() {
        Operand result = new ExpressionBuilder("{-23,31,0,6,50}")
                .build()
                .evaluate();
        assertEquals(5, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-23f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(31f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(0f, ((Number) operand3).getValue(), 0f);
        Operand operand4 = ((Vector) result).getValues()[3];
        assertEquals(6f, ((Number) operand4).getValue(), 0f);
        Operand operand5 = ((Vector) result).getValues()[4];
        assertEquals(50f, ((Number) operand5).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder04() {
        Operand result = new ExpressionBuilder("{-2,sin(3),0}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(Math.sin(3), ((Number) operand2).getValue(), EPSILON);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(0f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder05() {
        Operand result = new ExpressionBuilder("{-2,sin(3),cos(3)}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(Math.sin(3), ((Number) operand2).getValue(), EPSILON);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(Math.cos(3), ((Number) operand3).getValue(), EPSILON);
    }

    @Test
    public void testVectorBuilder06() {
        Operand result = new ExpressionBuilder("{-2,x,3}")
                .variable("x")
                .build()
                .setVariable("x", new Number(42))
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(42f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(3f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder07() {
        Operand result = new ExpressionBuilder("{-2,3,4+5}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(3f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(4f + 5f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder08() {
        Operand result = new ExpressionBuilder("{-2, 3, (4+5)/2}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(3f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals((4f + 5f) / 2f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder09() {
        Operand result = new ExpressionBuilder("{-2.52, 3.1, 4.5555 + 5.647}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2.52f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(3.1f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(4.5555f + 5.647f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorBuilder10() {
        Operand result = new ExpressionBuilder("{-2.52, 3.1e5, 4.5555 + 5.647}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(-2.52f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(3.1e5f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(4.5555f + 5.647f, ((Number) operand3).getValue(), 0f);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVectorBuilder11() {
        Operand result = new ExpressionBuilder("{-2,3,0},5")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVectorBuilder12() {
        Operand result = new ExpressionBuilder("{{-2,3,0}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);
    }

    @Test
    public void testNestedVectorBuilder01() {
        Operand result = new ExpressionBuilder("{{1,2,3},{-1,-2,-3},{0,2,0}}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand vector1 = ((Vector) result).getValues()[0];
        Operand vector1Operand1 = ((Vector) vector1).getValues()[0];
        Operand vector1Operand2 = ((Vector) vector1).getValues()[1];
        Operand vector1Operand3 = ((Vector) vector1).getValues()[2];
        assertEquals(1f, ((Number) vector1Operand1).getValue(), 0f);
        assertEquals(2f, ((Number) vector1Operand2).getValue(), 0f);
        assertEquals(3f, ((Number) vector1Operand3).getValue(), 0f);

        Operand vector2 = ((Vector) result).getValues()[1];
        Operand vector2Operand1 = ((Vector) vector2).getValues()[0];
        Operand vector2Operand2 = ((Vector) vector2).getValues()[1];
        Operand vector2Operand3 = ((Vector) vector2).getValues()[2];
        assertEquals(-1f, ((Number) vector2Operand1).getValue(), 0f);
        assertEquals(-2f, ((Number) vector2Operand2).getValue(), 0f);
        assertEquals(-3f, ((Number) vector2Operand3).getValue(), 0f);

        Operand vector3 = ((Vector) result).getValues()[2];
        Operand vector3Operand1 = ((Vector) vector3).getValues()[0];
        Operand vector3Operand2 = ((Vector) vector3).getValues()[1];
        Operand vector3Operand3 = ((Vector) vector3).getValues()[2];
        assertEquals(0f, ((Number) vector3Operand1).getValue(), 0f);
        assertEquals(2f, ((Number) vector3Operand2).getValue(), 0f);
        assertEquals(0f, ((Number) vector3Operand3).getValue(), 0f);
    }

    @Test
    public void testNestedVectorBuilder02() {
        Operand result = new ExpressionBuilder("{{1,2,sin(3)},{tan(-1),-2,-3},{0,cos(3),0}}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand vector1 = ((Vector) result).getValues()[0];
        Operand vector1Operand1 = ((Vector) vector1).getValues()[0];
        Operand vector1Operand2 = ((Vector) vector1).getValues()[1];
        Operand vector1Operand3 = ((Vector) vector1).getValues()[2];
        assertEquals(1f, ((Number) vector1Operand1).getValue(), 0f);
        assertEquals(2f, ((Number) vector1Operand2).getValue(), 0f);
        assertEquals(Math.sin(3), ((Number) vector1Operand3).getValue(), EPSILON);

        Operand vector2 = ((Vector) result).getValues()[1];
        Operand vector2Operand1 = ((Vector) vector2).getValues()[0];
        Operand vector2Operand2 = ((Vector) vector2).getValues()[1];
        Operand vector2Operand3 = ((Vector) vector2).getValues()[2];
        assertEquals(Math.tan(-1), ((Number) vector2Operand1).getValue(), EPSILON);
        assertEquals(-2f, ((Number) vector2Operand2).getValue(), 0f);
        assertEquals(-3f, ((Number) vector2Operand3).getValue(), 0f);

        Operand vector3 = ((Vector) result).getValues()[2];
        Operand vector3Operand1 = ((Vector) vector3).getValues()[0];
        Operand vector3Operand2 = ((Vector) vector3).getValues()[1];
        Operand vector3Operand3 = ((Vector) vector3).getValues()[2];
        assertEquals(0f, ((Number) vector3Operand1).getValue(), 0f);
        assertEquals(Math.cos(3), ((Number) vector3Operand2).getValue(), EPSILON);
        assertEquals(0f, ((Number) vector3Operand3).getValue(), 0f);
    }

    @Test
    public void testNestedVectorBuilder03() {
        Operand result = new ExpressionBuilder("{{x,2,3},{-1,y,-3},{0,2,z}}")
                .variables("x", "y", "z")
                .build()
                .setVariable("x", new Number(Math.PI))
                .setVariable("y", new Number(42))
                .setVariable("z", new Number(Math.E))
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand vector1 = ((Vector) result).getValues()[0];
        Operand vector1Operand1 = ((Vector) vector1).getValues()[0];
        Operand vector1Operand2 = ((Vector) vector1).getValues()[1];
        Operand vector1Operand3 = ((Vector) vector1).getValues()[2];
        assertEquals(Math.PI, ((Number) vector1Operand1).getValue(), EPSILON);
        assertEquals(2f, ((Number) vector1Operand2).getValue(), 0f);
        assertEquals(3f, ((Number) vector1Operand3).getValue(), 0f);

        Operand vector2 = ((Vector) result).getValues()[1];
        Operand vector2Operand1 = ((Vector) vector2).getValues()[0];
        Operand vector2Operand2 = ((Vector) vector2).getValues()[1];
        Operand vector2Operand3 = ((Vector) vector2).getValues()[2];
        assertEquals(-1f, ((Number) vector2Operand1).getValue(), 0f);
        assertEquals(42, ((Number) vector2Operand2).getValue(), 0f);
        assertEquals(-3f, ((Number) vector2Operand3).getValue(), 0f);

        Operand vector3 = ((Vector) result).getValues()[2];
        Operand vector3Operand1 = ((Vector) vector3).getValues()[0];
        Operand vector3Operand2 = ((Vector) vector3).getValues()[1];
        Operand vector3Operand3 = ((Vector) vector3).getValues()[2];
        assertEquals(0f, ((Number) vector3Operand1).getValue(), 0f);
        assertEquals(2f, ((Number) vector3Operand2).getValue(), 0f);
        assertEquals(Math.E, ((Number) vector3Operand3).getValue(), EPSILON);
    }

    @Test
    public void testVectorIndexBuilder01() {
        Operand result = new ExpressionBuilder("{2,1,5}[0]")
                .build()
                .evaluate();
        assertEquals(2, ((Number) result).getValue(), 0);
    }

    @Test
    public void testVectorIndexBuilder02() {
        Operand result = new ExpressionBuilder("{2,1,5}[1]")
                .build()
                .evaluate();
        assertEquals(1, ((Number) result).getValue(), 0);
    }

    @Test
    public void testVectorIndexBuilder03() {
        Operand result = new ExpressionBuilder("{2,1,5}[2]")
                .build()
                .evaluate();
        assertEquals(5, ((Number) result).getValue(), 0);
    }

    @Test
    public void testVectorIndexBuilder04() {
        Operand result = new ExpressionBuilder("x[1]")
                .variable("x")
                .build()
                .setVariable("x", new Vector(new Operand[] {
                        new Number(-2),
                        new Number(1),
                        new Number(3),
                }))
                .evaluate();
        assertEquals(1, ((Number) result).getValue(), 0);
    }

    @Test
    public void testVectorIndexBuilder05() {
        Operand result = new ExpressionBuilder("{2,{5,3},-4}[1,0]")
                .build()
                .evaluate();
        assertEquals(5, ((Number) result).getValue(), 0);
    }

    @Test
    public void testVectorIndexBuilder06() {
        Operand result = new ExpressionBuilder("{2,{5,3}[0],-4}")
                .build()
                .evaluate();

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(5f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(-4f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorIndexBuilder07() {
        Operand result = new ExpressionBuilder("{2,{sin(4),3}[0],-4}")
                .build()
                .evaluate();

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(2f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(Math.sin(4f), ((Number) operand2).getValue(), EPSILON);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(-4f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void testVectorIndexBuilder08() {
        Operand result = new ExpressionBuilder("{2,-4,{sin(4),3}}[2,0]")
                .build()
                .evaluate();
        assertEquals(Math.sin(4f), ((Number) result).getValue(), EPSILON);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVectorIndex_OutOfBounds01() {
        new ExpressionBuilder("{2,1,5}[-1]")
                .build()
                .evaluate();
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVectorIndex_OutOfBounds02() {
        new ExpressionBuilder("{2,1,5}[3]")
                .build()
                .evaluate();
    }


}
