package construction;

import construction.mocks.ReadConstructionElementUseCaseMock;
import de.geo2web.arithmetic.Operand;
import de.geo2web.arithmetic.Number;
import de.geo2web.arithmetic.tokenizer.UnknownFunctionOrVariableException;
import de.geo2web.construction.ConstructionElement;
import de.geo2web.construction.application.ConstructionElementChanges;
import de.geo2web.shared.ElementName;
import de.geo2web.shared.ExpressionInput;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ConstructionElementTest {

    static final float Epsilon = 1e-3f;

    @Test
    public void testCreateConstructionElement() {
        String expression = "1+2";
        ConstructionElementChanges changes = ConstructionElementChanges.builder()
                .input(ExpressionInput.of(expression))
                .build();
        ConstructionElement element = changes.apply(ConstructionElement.builder(), ConstructionElement.InitialConstructionIndex);

        assertEquals(element.getInput().toString(), expression);
        assertEquals(element.getConstructionIndex(), ConstructionElement.InitialConstructionIndex);
    }

    @Test
    public void testEvaluateConstructionElement() {
        ReadConstructionElementUseCaseMock read = new ReadConstructionElementUseCaseMock();
        String expression1 = "1+2";
        ConstructionElementChanges changes = ConstructionElementChanges.builder()
                .input(ExpressionInput.of(expression1))
                .build();
        ConstructionElement element = changes.apply(ConstructionElement.builder(), ConstructionElement.InitialConstructionIndex);
        read.Add(element);

        Operand result = element.evaluate(read);

        String expectedResult = Float.toString(3);
        assertEquals(result.toReadableString(), expectedResult);
    }

    @Test
    public void testEvaluateMultipleConstructionElements1() {
        ReadConstructionElementUseCaseMock read = new ReadConstructionElementUseCaseMock();

        String expression1 = "1+2";
        String element1Name = "a";
        String expression2 = "3+" + element1Name;
        String element2Name = "result";
        ConstructionElementChanges changes1 = ConstructionElementChanges.builder()
                .name(ElementName.of(element1Name))
                .input(ExpressionInput.of(expression1))
                .build();
        ConstructionElement element1 = changes1.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element1);

        ConstructionElementChanges changes2 = ConstructionElementChanges.builder()
                .name(ElementName.of(element2Name))
                .input(ExpressionInput.of(expression2))
                .build();
        ConstructionElement element2 = changes2.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element2);

        Operand result = element2.evaluate(read);

        float expectedResult = 6;
        assertEquals(((Number)result).getValue(), expectedResult, Epsilon);
        assertEquals(element1.getConstructionIndex(), ConstructionElement.InitialConstructionIndex);
        assertEquals(element2.getConstructionIndex(), ConstructionElement.InitialConstructionIndex + 1);
    }

    @Test
    public void testEvaluateMultipleConstructionElements2() {
        ReadConstructionElementUseCaseMock read = new ReadConstructionElementUseCaseMock();

        String expression1 = "sin(2 * pi)";
        String element1Name = "a";
        String expression2 = "2*" + element1Name;
        String element2Name = "result";
        ConstructionElementChanges changes1 = ConstructionElementChanges.builder()
                .name(ElementName.of(element1Name))
                .input(ExpressionInput.of(expression1))
                .build();
        ConstructionElement element1 = changes1.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element1);

        ConstructionElementChanges changes2 = ConstructionElementChanges.builder()
                .name(ElementName.of(element2Name))
                .input(ExpressionInput.of(expression2))
                .build();
        ConstructionElement element2 = changes2.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element2);

        Operand result = element2.evaluate(read);

        float expectedResult = 2f * (float)Math.sin(2 * Math.PI);
        assertEquals(((Number)result).getValue(), expectedResult, Epsilon);
        assertEquals(element1.getConstructionIndex(), ConstructionElement.InitialConstructionIndex);
        assertEquals(element2.getConstructionIndex(), ConstructionElement.InitialConstructionIndex + 1);
    }

    @Test
    public void testEvaluateMultipleConstructionElements3() {
        ReadConstructionElementUseCaseMock read = new ReadConstructionElementUseCaseMock();

        String expression1 = "sin(2 * pi)";
        String element1Name = "a";
        String expression2 = "1 / 2";
        String element2Name = "b";
        String expression3 = element1Name + "*" + element2Name;
        String element3Name = "result";

        ConstructionElementChanges changes1 = ConstructionElementChanges.builder()
                .name(ElementName.of(element1Name))
                .input(ExpressionInput.of(expression1))
                .build();
        ConstructionElement element1 = changes1.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element1);

        ConstructionElementChanges changes2 = ConstructionElementChanges.builder()
                .name(ElementName.of(element2Name))
                .input(ExpressionInput.of(expression2))
                .build();
        ConstructionElement element2 = changes2.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element2);

        ConstructionElementChanges changes3 = ConstructionElementChanges.builder()
                .name(ElementName.of(element3Name))
                .input(ExpressionInput.of(expression3))
                .build();
        ConstructionElement element3 = changes3.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element3);

        Operand result = element3.evaluate(read);

        float expectedResult = 1/2f * (float)Math.sin(2 * Math.PI);
        assertEquals(((Number)result).getValue(), expectedResult, Epsilon);
        assertEquals(element1.getConstructionIndex(), ConstructionElement.InitialConstructionIndex);
        assertEquals(element2.getConstructionIndex(), ConstructionElement.InitialConstructionIndex + 1);
        assertEquals(element3.getConstructionIndex(), ConstructionElement.InitialConstructionIndex + 2);
    }

    @Test
    public void testEvaluateLongVariableNameConstructionElements() {
        ReadConstructionElementUseCaseMock read = new ReadConstructionElementUseCaseMock();

        String expression1 = "1+2";
        String element1Name = "longVariableName";
        String expression2 = "3+"+element1Name;
        String element2Name = "result";
        ConstructionElementChanges changes1 = ConstructionElementChanges.builder()
                .name(ElementName.of(element1Name))
                .input(ExpressionInput.of(expression1))
                .build();
        ConstructionElement element1 = changes1.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element1);

        ConstructionElementChanges changes2 = ConstructionElementChanges.builder()
                .name(ElementName.of(element2Name))
                .input(ExpressionInput.of(expression2))
                .build();
        ConstructionElement element2 = changes2.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element2);

        Operand result = element2.evaluate(read);

        float expectedResult = 6;
        assertEquals(((Number)result).getValue(), expectedResult, Epsilon);
        assertEquals(element1.getConstructionIndex(), ConstructionElement.InitialConstructionIndex);
        assertEquals(element2.getConstructionIndex(), ConstructionElement.InitialConstructionIndex + 1);
    }

    @Test(expected = UnknownFunctionOrVariableException.class)
    public void testNotValidVariableNameConstructionElements() {
        ReadConstructionElementUseCaseMock read = new ReadConstructionElementUseCaseMock();

        String expression1 = "1+2";
        String element1Name = "notValid{Name";
        String expression2 = "3+"+element1Name;
        String element2Name = "result";
        ConstructionElementChanges changes1 = ConstructionElementChanges.builder()
                .name(ElementName.of(element1Name))
                .input(ExpressionInput.of(expression1))
                .build();
        ConstructionElement element1 = changes1.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element1);

        ConstructionElementChanges changes2 = ConstructionElementChanges.builder()
                .name(ElementName.of(element2Name))
                .input(ExpressionInput.of(expression2))
                .build();
        ConstructionElement element2 = changes2.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element2);

        element2.evaluate(read);
    }

    @Test(expected = IllegalArgumentException.class)
    public void testVariableSameNameAsFunctionConstructionElements() {
        ReadConstructionElementUseCaseMock read = new ReadConstructionElementUseCaseMock();

        String expression1 = "1+2";
        String element1Name = "sin";
        String expression2 = "3+"+element1Name;
        String element2Name = "result";
        ConstructionElementChanges changes1 = ConstructionElementChanges.builder()
                .name(ElementName.of(element1Name))
                .input(ExpressionInput.of(expression1))
                .build();
        ConstructionElement element1 = changes1.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element1);

        ConstructionElementChanges changes2 = ConstructionElementChanges.builder()
                .name(ElementName.of(element2Name))
                .input(ExpressionInput.of(expression2))
                .build();
        ConstructionElement element2 = changes2.apply(ConstructionElement.builder(), read.getNextConstructionIndex());
        read.Add(element2);

        element2.evaluate(read);
    }

}
