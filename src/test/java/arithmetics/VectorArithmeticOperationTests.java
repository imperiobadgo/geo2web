package arithmetics;

import de.geo2web.arithmetics.*;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class VectorArithmeticOperationTests {

    @Test
    public void AddVectorsTest1() {
        Vector expected = new Vector(Vector.toNumberArray(new float[]{3,5,7}));

        Vector a = new Vector(Vector.toNumberArray(new float[] {1,2,3}));
        Vector b = new Vector(Vector.toNumberArray(new float[] {2,3,4}));
        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
        Expression result = node.evaluate();

        Assert.isInstanceOf(Vector.class, result);
        Assert.isTrue(((Vector) result).getVector().equals(expected), "Expected");
    }

    @Test
    public void AddVectorsTest2() {
        Vector expected = new Vector(Vector.toNumberArray(new float[]{-5,2,0}));

        Vector a = new Vector(Vector.toNumberArray(new float[] {1,2,-4}));
        Vector b = new Vector(Vector.toNumberArray(new float[] {-6,0,4}));
        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
        Expression result = node.evaluate();

        Assert.isInstanceOf(Vector.class, result);
        Assert.isTrue(((Vector) result).getVector().equals(expected), "Expected");
    }

}
