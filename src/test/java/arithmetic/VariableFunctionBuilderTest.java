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
        assertEquals(readableString, "a(x)=2.0*x+5.0");
    }

    @Test
    public void testVariable1() {
        Operand result = new ExpressionBuilder("2*x+5")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "2.0*x+5.0");
    }

    @Test
    public void testVariable2() {
        Operand result = new ExpressionBuilder("2*x+5*2")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "2.0*x+10.0");
    }

    @Test
    public void testVariable3() {
        Operand result = new ExpressionBuilder("4*x/y")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "4.0*x/y");
    }

    @Test
    public void testVariable4() {
        Operand result = new ExpressionBuilder("3*sin(x)")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "3.0*sin(x)");
    }

    @Test
    public void testVariable5() {
        Operand result = new ExpressionBuilder("3*sin(x*-2/sqrt(y^2+3^2))")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "3.0*sin(x*-2.0/sqrt(y^2.0+9.0))");
    }

    @Test
    public void testLongVariableName1() {
        Operand result = new ExpressionBuilder("2*name+5")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "2.0*name+5.0");
    }

    @Test
    public void testLongVariableName2() {
        Operand result = new ExpressionBuilder("2*name+5 / another_long_name")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "2.0*name+5.0/another_long_name");
    }

    @Test
    public void testLongVariableName3() {
        Operand result = new ExpressionBuilder("2*name+6/2- another_long_name")
                .build()
                .evaluate();

        String readableString = result.toReadableString();
        assertEquals(readableString, "2.0*name+3.0-another_long_name");
    }
}
