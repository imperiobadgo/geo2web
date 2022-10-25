package arithmetic;

import de.geo2web.arithmetic.Expression;
import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.Operand;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class VariableFunctionBuilderTest {

    @Test
    public void testVariableFunction1() {
        Expression expr = new ExpressionBuilder("a(x)=2+1").build();
        Operand result = expr.evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "a(x)=3.0");
    }

    @Test
    public void testVariableFunction2() {
        Expression expr = new ExpressionBuilder("a(x)=2+3*(5-2)").build();
        Operand result = expr.evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "a(x)=11.0");
    }

    @Test
    public void testVariableFunction3() {
        Expression expr = new ExpressionBuilder("a(x)=2*x+5").build();
        Operand result = expr.evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "a(x)=11.0");
    }
}
