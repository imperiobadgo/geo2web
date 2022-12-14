/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic;

import de.geo2web.arithmetic.function.Function;
import de.geo2web.arithmetic.function.Functions;
import de.geo2web.arithmetic.operator.Operator;
import de.geo2web.arithmetic.tokenizer.*;

import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Future;

/**
 * A class representing a mathematical expression. The resolution of the variables can also be stored for evaluation purposes.
 */
public class Expression {

    private final Token[] tokens;

    private final Map<String, Operand> variables;

    private final Set<String> userFunctionNames;

    private static Map<String, Operand> createDefaultVariables() {
        final Map<String, Operand> vars = new HashMap<>(4);
        vars.put("pi", new Number(Math.PI));
        vars.put("π", new Number(Math.PI));
        vars.put("φ", new Number(1.61803398874d));
        vars.put("e", new Number(Math.E));
        return vars;
    }

    /**
     * Creates a new expression that is a copy of the existing one.
     *
     * @param existing the expression to copy
     */
    public Expression(final Expression existing) {
        this.tokens = Arrays.copyOf(existing.tokens, existing.tokens.length);
        this.variables = new HashMap<>();
        this.variables.putAll(existing.variables);
        this.userFunctionNames = new HashSet<>(existing.userFunctionNames);
    }

    public Expression(final Token[] tokens) {
        this.tokens = tokens;
        this.variables = createDefaultVariables();
        this.userFunctionNames = Collections.emptySet();
    }

    Expression(final Token[] tokens, Set<String> userFunctionNames) {
        this.tokens = tokens;
        this.variables = createDefaultVariables();
        this.userFunctionNames = userFunctionNames;
    }

    public Expression setVariable(final String name, final Operand value) {
        this.checkVariableName(name);
        this.variables.put(name, value);
        return this;
    }

    private void checkVariableName(String name) {
        if (this.userFunctionNames.contains(name) || Functions.getBuiltinFunction(name) != null) {
            throw new IllegalArgumentException("The variable name '" + name + "' is invalid. Since there exists a function with the same name");
        }
    }

    public Expression setVariables(Map<String, Operand> variables) {
        for (Map.Entry<String, Operand> v : variables.entrySet()) {
            this.setVariable(v.getKey(), v.getValue());
        }
        return this;
    }

    public Expression clearVariables() {
        this.variables.clear();
        return this;
    }

    public Set<String> getVariableNames() {
        final Set<String> variables = new HashSet<>();
        for (final Token t : tokens) {
            if (t.getType() == Token.TOKEN_VARIABLE)
                variables.add(((VariableToken) t).getName());
        }
        return variables;
    }

    public ValidationResult validate(boolean checkVariablesSet) {
        final List<String> errors = new ArrayList<>(0);
        if (checkVariablesSet) {
            /* check that all vars have a value set */
            for (final Token t : this.tokens) {
                if (t.getType() == Token.TOKEN_VARIABLE) {
                    final String var = ((VariableToken) t).getName();
                    if (!variables.containsKey(var)) {
                        errors.add("The setVariable '" + var + "' has not been set");
                    }
                }
            }
        }

        /* Check if the number of operands, functions and operators match.
           The idea is to increment a counter for operands and decrease it for operators.
           When a function occurs the number of available arguments has to be greater
           than or equals to the function's expected number of arguments.
           The count has to be larger than 1 at all times and exactly 1 after all tokens
           have been processed */
        int count = 0;
        for (Token tok : this.tokens) {
            switch (tok.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    count++;
                    break;
                case Token.TOKEN_VECTOR:
                    final VectorToken vectorToken = (VectorToken) tok;
                    final int argsNumVector = vectorToken.getNumArguments();
                    if (argsNumVector > count) {
                        errors.add("Not enough arguments for 'Vector of " + argsNumVector + " dimensions'");
                    }
                    if (argsNumVector > 1) {
                        count -= argsNumVector - 1;
                    }
                    break;
                case Token.TOKEN_INDEX:
                    final IndexToken indexToken = (IndexToken) tok;
                    final int argsNumIndex = indexToken.getNumArguments();
                    if (argsNumIndex > count) {
                        errors.add("Not enough arguments for 'Index of " + argsNumIndex + " dimensions'");
                    }
                    count -= argsNumIndex;
                    break;
                case Token.TOKEN_FUNCTION:
                    final Function func = ((FunctionToken) tok).getFunction();
                    final int argsNum = func.getNumArguments();
                    if (argsNum > count) {
                        errors.add("Not enough arguments for '" + func.getName() + "'");
                    }
                    if (argsNum > 1) {
                        count -= argsNum - 1;
                    } else if (argsNum == 0) {
                        // see https://github.com/fasseg/exp4j/issues/59
                        count++;
                    }
                    break;
                case Token.TOKEN_OPERATOR:
                    Operator op = ((OperatorToken) tok).getOperator();
                    if (op.getNumOperands() == 2) {
                        count--;
                    }
                    break;
            }
            if (count < 1) {
                errors.add("Too many operators");
                return new ValidationResult(false, errors);
            }
        }
        if (count > 1) {
            errors.add("Too many operands");
        }
        return errors.size() == 0 ? ValidationResult.SUCCESS : new ValidationResult(false, errors);

    }

    public ValidationResult validate() {
        return validate(true);
    }

    public Future<Operand> evaluateAsync(ExecutorService executor) {
        return executor.submit(this::evaluate);
    }

    /**
     * Evaluates the content of the expression.
     * @return The operand can be the result or an instruction on how to retrieve the result by using variables.
     */
    public Operand evaluate() {
        final Stack<Operand> output = new Stack<>();
        for (Token t : tokens) {
            if (t.getType() == Token.TOKEN_NUMBER) {
                output.push(((NumberToken) t).getValue());
            } else if (t.getType() == Token.TOKEN_VECTOR) {
                VectorToken vec = (VectorToken) t;
                final int numArguments = vec.getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for vector");
                }
                /* collect the operands from the stack */
                Operand[] args = new Operand[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    args[j] = output.pop();
                }
                output.push(new Vector(args));
            } else if (t.getType() == Token.TOKEN_INDEX) {
                IndexToken indexToken = (IndexToken) t;
                /* Collect the arguments from the stack */
                final int numArguments = indexToken.getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for index description!");
                }

                int[] indices = new int[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    Operand arg = output.pop();
                    if (!(arg instanceof NumberOperand)) {
                        throw new IllegalArgumentException("Only numbers are allowed as arguments for indices!");
                    }
                    Number numberIndex = ((NumberOperand) arg).getNumber();
                    int index = Math.round(numberIndex.getValue());
                    if (index < 0) {
                        throw new IllegalArgumentException("Index cannot be smaller than zero!");
                    }
                    indices[j] = index;
                }

                /* get the desired content from the vector-combination and push the resulting content onto the stack */

                //remember the current result of the last index operation
                Operand lastOutputElement = output.pop();
                for (int index : indices) {
                    if (!(lastOutputElement instanceof VectorOperand)) {
                        throw new IllegalArgumentException("Index only available after vectors!");
                    }
                    Vector lastVector = ((VectorOperand) lastOutputElement).getVector();
                    if (index >= lastVector.getValues().length) {
                        throw new IllegalArgumentException("Index cannot be greater than elements in the vector!");
                    }
                    lastOutputElement = lastVector.getValues()[index];
                }

                output.push(lastOutputElement);
            } else if (t.getType() == Token.TOKEN_VARIABLE) {
                final String name = ((VariableToken) t).getName();
                final Operand value = this.variables.get(name);
                if (value == null) {
                    //If no known variable was found, pass it on the output.
                    final Variable variable = new Variable(name);
                    output.push(variable);
                } else {
                    if (value instanceof VariableFunctionOperand) {
                        //Use the content of variable function
                        output.push(((VariableFunctionOperand) value).getFunction().getFunctionBody());
                    } else {
                        output.push(value);
                    }

                }

            } else if (t.getType() == Token.TOKEN_OPERATOR) {
                OperatorToken op = (OperatorToken) t;
                if (output.size() < op.getOperator().getNumOperands()) {
                    throw new IllegalArgumentException("Invalid number of operands available for '" + op.getOperator().getSymbol() + "' operator");
                }
                if (op.getOperator().getNumOperands() == 2) {
                    /* pop the operands and push the result of the operation */
                    Operand rightArg = output.pop();
                    Operand leftArg = output.pop();
                    output.push(op.getOperator().applyWithVariableCheck(leftArg, rightArg));
                } else if (op.getOperator().getNumOperands() == 1) {
                    /* pop the operand and push the result of the operation */
                    Operand arg = output.pop();
                    output.push(op.getOperator().applyWithVariableCheck(arg));
                }
            } else if (t.getType() == Token.TOKEN_FUNCTION) {
                FunctionToken func = (FunctionToken) t;
                final int numArguments = func.getFunction().getNumArguments();
                if (output.size() < numArguments) {
                    throw new IllegalArgumentException("Invalid number of arguments available for '" + func.getFunction().getName() + "' function");
                }
                /* collect the arguments from the stack */
                Operand[] args = new Operand[numArguments];
                for (int j = numArguments - 1; j >= 0; j--) {
                    args[j] = output.pop();
                }
                output.push(func.getFunction().applyWithVariableCheck(args));
            } else if (t.getType() == Token.TOKEN_ASSIGNMENT) {
                AssignmentToken assignment = (AssignmentToken) t;
                if (output.size() > 1) {
                    throw new IllegalArgumentException("To many arguments left for function");
                }
                Operand lastOperand = output.pop();
                VariableFunction variableFunction = new VariableFunction(lastOperand, assignment.getName(), assignment.getParameters());
                output.push(variableFunction);
            }
        }
        if (output.size() > 1) {
            throw new IllegalArgumentException("Invalid number of items on the output queue. Might be caused by an invalid number of arguments for a function.");
        }
        return output.pop();
    }
}
