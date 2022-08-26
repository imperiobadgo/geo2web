package arithmetic;

import de.geo2web.arithmetic.Vector;
import org.junit.jupiter.api.Test;
import org.springframework.util.Assert;

public class VectorArithmeticOperationTests {

//    @Test
//    public void AddVectorsTest1() {
//        Vector expected = new Vector(Vector.toNumberArray(new float[]{3, 5, 7, 0}));
//
//        Vector a = new Vector(Vector.toNumberArray(new float[]{1, 2, 3, 0}));
//        Vector b = new Vector(Vector.toNumberArray(new float[]{2, 3, 4, 0}));
//        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
//        Expression result = node.evaluate();
//
//        Assert.isInstanceOf(Vector.class, result);
//        Assert.isTrue(((Vector) result).getVector().equals(expected), "Expected");
//    }

//    @Test
//    public void AddVectorsTest2() {
//        Vector expected = new Vector(Vector.toNumberArray(new float[]{-5, 2, 0, -11}));
//
//        Vector a = new Vector(Vector.toNumberArray(new float[]{1, 2, -4, -15}));
//        Vector b = new Vector(Vector.toNumberArray(new float[]{-6, 0, 4, 4}));
//        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
//        Expression result = node.evaluate();
//
//        Assert.isInstanceOf(Vector.class, result);
//        Assert.isTrue(((Vector) result).getVector().equals(expected), "Expected");
//    }
//
//    @Test
//    public void AddVectorsAndChangeNumberTest1() {
//        Vector expected1 = new Vector(Vector.toNumberArray(new float[]{-5, 2, 0}));
//        Vector expected2 = new Vector(Vector.toNumberArray(new float[]{-5, 10, 0}));
//
//        Vector a = new Vector(Vector.toNumberArray(new float[]{1, 2, -4}));
//        NumberVariable y = new NumberVariable("y").setValue(new Number(0));
//        Vector b = new Vector(new NumberValue[]{
//                new Number(-6),
//                y,
//                new Number(4)});
//
//        ExpressionNode node = new ExpressionNode(a, b, Operator.PLUS);
//        Expression result1 = node.evaluate();
//
//        Assert.isInstanceOf(Vector.class, result1);
//        Assert.isTrue(((Vector) result1).getVector().equals(expected1), "Expected1");
//
//        y.setValue(new Number(8));
//        Expression result2 = node.evaluate();
//
//        Assert.isInstanceOf(Vector.class, result2);
//        Assert.isTrue(((Vector) result2).getVector().equals(expected2), "Expected2");
//    }

}
