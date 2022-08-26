package arithmetic;

import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.Number;
import de.geo2web.arithmetic.Operand;
import de.geo2web.arithmetic.Vector;
import org.junit.Test;

import static arithmetic.TestUtil.EPSILON;
import static org.junit.Assert.assertEquals;

public class VectorArithmeticOperationTests {

    @Test
    public void addVectorCalc01() {
        Operand result = new ExpressionBuilder("{2,0,5}+5")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(7f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(5f, ((Number) operand2).getValue(), 0f);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(10f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void addVectorCalc02() {
        Operand result = new ExpressionBuilder("{2^2,2pi,5}+5")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(9f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(2 * Math.PI + 5, ((Number) operand2).getValue(), EPSILON);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(10f, ((Number) operand3).getValue(), 0f);
    }

    @Test
    public void addVectorCalc03() {
        Operand result = new ExpressionBuilder("{2,0,5}+{2,-5,5}")
                .build()
                .evaluate();
        assertEquals(3, ((Vector) result).getValues().length, 0);

        Operand operand1 = ((Vector) result).getValues()[0];
        assertEquals(4f, ((Number) operand1).getValue(), 0f);
        Operand operand2 = ((Vector) result).getValues()[1];
        assertEquals(-5f, ((Number) operand2).getValue(), EPSILON);
        Operand operand3 = ((Vector) result).getValues()[2];
        assertEquals(10f, ((Number) operand3).getValue(), 0f);
    }

}
