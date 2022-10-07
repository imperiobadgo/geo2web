package arithmetic;

import de.geo2web.arithmetic.Expression;
import de.geo2web.arithmetic.ExpressionBuilder;
import de.geo2web.arithmetic.ValidationResult;
import org.junit.Assert;
import org.junit.Test;

public class VectorValidateTest {

    @Test
    public void testValidateVector1() {
        Expression exp = new ExpressionBuilder("{1,2}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVector2() {
        Expression exp = new ExpressionBuilder("{1,y}")
                .variable("y")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVector3() {
        Expression exp = new ExpressionBuilder("{x,y}")
                .variables("x", "y")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVector4() {
        Expression exp = new ExpressionBuilder("{1}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorPositive1() {
        Expression exp = new ExpressionBuilder("{+1,2}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorPositive2() {
        Expression exp = new ExpressionBuilder("{1,+2}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorPositive3() {
        Expression exp = new ExpressionBuilder("{1,2,+5}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorNegative1() {
        Expression exp = new ExpressionBuilder("{-1,2}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorNegative2() {
        Expression exp = new ExpressionBuilder("{1,-2}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorNegative3() {
        Expression exp = new ExpressionBuilder("{1,2,-5}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorOperator() {
        Expression exp = new ExpressionBuilder("{1 + 2, 3}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateMultipleVectorOperator() {
        Expression exp = new ExpressionBuilder("{1 + 2, 3} - {1, 3 + 5}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorFunction() {
        Expression exp = new ExpressionBuilder("{sin(1), 3}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateMultipleVectorFunction() {
        Expression exp = new ExpressionBuilder("cross({1, 3, 4},{1, 3, 5})")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorOperatorAndFunction() {
        Expression exp = new ExpressionBuilder("{sin(1 + 2), 3}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorIndices1() {
        Expression exp = new ExpressionBuilder("{1,2}[0]")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateVectorIndices2() {
        Expression exp = new ExpressionBuilder("{1,{4,5},3}[1,0]")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertTrue(result.isValid());
    }

    @Test
    public void testValidateInvalidVector1() {
        Expression exp = new ExpressionBuilder("{1,}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidVector2() {
        Expression exp = new ExpressionBuilder("{}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidVectorOperator() {
        Expression exp = new ExpressionBuilder("{1 + , 3}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidVectorFunction() {
        Expression exp = new ExpressionBuilder("{sin(), 3}")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidMultipleVectorFunction() {
        Expression exp = new ExpressionBuilder("cross({1, 3, 4},)")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidVectorIndices1() {
        Expression exp = new ExpressionBuilder("{1,2}[]")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

    @Test
    public void testValidateInvalidVectorIndices2() {
        Expression exp = new ExpressionBuilder("{1,{4,5},3}[1,]")
                .build();
        ValidationResult result = exp.validate(false);
        Assert.assertFalse(result.isValid());
    }

}
