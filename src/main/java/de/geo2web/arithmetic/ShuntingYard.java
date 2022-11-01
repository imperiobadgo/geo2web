/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic;

import de.geo2web.arithmetic.operator.Operator;
import de.geo2web.arithmetic.function.Function;
import de.geo2web.arithmetic.tokenizer.AssignmentToken;
import de.geo2web.arithmetic.tokenizer.OperatorToken;
import de.geo2web.arithmetic.tokenizer.Token;
import de.geo2web.arithmetic.tokenizer.Tokenizer;
import de.geo2web.util.StringUtil;

import java.util.*;

/**
 * Shunting yard implementation to convert infix to reverse polish notation
 */
public class ShuntingYard {

    /**
     * Convert a Set of tokens from infix to reverse polish notation
     *
     * @param expression             the expression to convert
     * @param userFunctions          the custom functions used
     * @param userOperators          the custom operators used
     * @param variableNames          the variable names used in the expression
     * @param implicitMultiplication set false to turn off implicit multiplication
     * @return a {@link de.geo2web.arithmetic.tokenizer.Token} array containing the result
     */
    public static Token[] convertToRPN(String expression, final Map<String, Function> userFunctions,
                                       final Map<String, Operator> userOperators, final Set<String> variableNames, final boolean implicitMultiplication) {
        final Stack<Token> stack = new Stack<>();
        final List<Token> output = new ArrayList<>();

        final boolean hasAssignment = StringUtil.contains(expression, ArithmeticSettings.Instance().Assignment);

        if (hasAssignment) {
            expression = parseVariableFunctionAndReturnRemainingExpression(expression, stack, variableNames);
        }

        final Tokenizer tokenizer = new Tokenizer(expression, userFunctions, userOperators, variableNames, implicitMultiplication);
        while (tokenizer.hasNext()) {
            Token token = tokenizer.nextToken();
            switch (token.getType()) {
                case Token.TOKEN_NUMBER:
                case Token.TOKEN_VARIABLE:
                    output.add(token);
                    break;
                case Token.TOKEN_FUNCTION:
                    stack.add(token);
                    break;
                case Token.TOKEN_SEPARATOR:
                    while (!stack.empty() && stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        output.add(stack.pop());
                    }
                    if (stack.empty() || stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        throw new IllegalArgumentException("Misplaced function separator ',' or mismatched parentheses");
                    }
                    break;
                case Token.TOKEN_OPERATOR:
                    while (!stack.empty() && stack.peek().getType() == Token.TOKEN_OPERATOR) {
                        OperatorToken o1 = (OperatorToken) token;
                        OperatorToken o2 = (OperatorToken) stack.peek();
                        if (o1.getOperator().getNumOperands() == 1 && o2.getOperator().getNumOperands() == 2) {
                            break;
                        } else if ((o1.getOperator().isLeftAssociative() && o1.getOperator().getPrecedence() <= o2.getOperator().getPrecedence())
                                || (o1.getOperator().getPrecedence() < o2.getOperator().getPrecedence())) {
                            output.add(stack.pop());
                        } else {
                            break;
                        }
                    }
                    stack.push(token);
                    break;
                case Token.TOKEN_PARENTHESES_OPEN:
                    stack.push(token);
                    break;
                case Token.TOKEN_PARENTHESES_CLOSE:
                    handleCloseParentheses(stack, output);
                    break;
                case Token.TOKEN_VECTOR:
                    //put everything except the open parentheses into the output
                    while (stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
                        output.add(stack.pop());
                    }
                    //pop the open parentheses from the stack
                    stack.pop();
                    output.add(token);
                    break;
                case Token.TOKEN_INDEX:
                    handleCloseParentheses(stack, output);
                    output.add(token);
                    break;
                case Token.TOKEN_ASSIGNMENT:

                    break;
                default:
                    throw new IllegalArgumentException("Unknown Token type encountered. This should not happen");
            }
        }
        while (!stack.empty()) {
            Token t = stack.pop();
            if (t.getType() == Token.TOKEN_PARENTHESES_CLOSE || t.getType() == Token.TOKEN_PARENTHESES_OPEN) {
                throw new IllegalArgumentException("Mismatched parentheses detected. Please check the expression");
            } else {
                output.add(t);
            }
        }
        return output.toArray(new Token[0]);
    }

    private static void handleCloseParentheses(final Stack<Token> stack, final List<Token> output) {
        //put everything except the open parentheses into the output
        while (stack.peek().getType() != Token.TOKEN_PARENTHESES_OPEN) {
            output.add(stack.pop());
        }
        //pop the open parentheses from the stack
        stack.pop();
        if (!stack.isEmpty() && stack.peek().getType() == Token.TOKEN_FUNCTION) {
            //put the function token into the output
            output.add(stack.pop());
        }
    }

    /**
     * Extracts the name declaration and parameters of a given function.
     * @param expression    the expression for the analysis and extraction.
     * @param stack         the resulting assignment token is pushed onto the stack.
     * @param variableNames the detected parameters are added to this list.
     * @return the function body as remaining {@link de.geo2web.arithmetic.Expression}.
     */
    private static String parseVariableFunctionAndReturnRemainingExpression(final String expression, final Stack<Token> stack, final Set<String> variableNames) {
        String trimmedExpression = expression.trim();
        char[] exprArray = trimmedExpression.toCharArray();

        String name = null;
        List<String> parameters = new ArrayList<>();
        int openParenthesesIndex = 0;
        int lastArgumentSeparator = 0;
        int closedParenthesesIndex = 0;
        int functionDeclarationIndex = 0;

        for (int i = 0, exprArrayLength = exprArray.length; i < exprArrayLength; i++) {
            char ch = exprArray[i];
            if (ch == ArithmeticSettings.Instance().Assignment) {
                if (closedParenthesesIndex == 0) {
                    return expression;//Maybe no function intended
                }
                functionDeclarationIndex = i;
                break;
            } else if (ch == ArithmeticSettings.Instance().Open_Parentheses) {
                if (openParenthesesIndex > 0) {
                    throw new IllegalArgumentException("Mismatched parentheses in function declaration detected. Please check the expression");
                }
                if (i == 0) {
                    throw new IllegalArgumentException("Function needs a name. Please check the expression");
                }
                openParenthesesIndex = i;
                lastArgumentSeparator = i + 1;
                name = trimmedExpression.substring(0, i);//name without the open parentheses
            } else if (ch == ArithmeticSettings.Instance().Close_Parentheses) {
                if (closedParenthesesIndex > 0) {
                    throw new IllegalArgumentException("Mismatched parentheses in function declaration detected. Please check the expression");
                }
                String parameter = trimmedExpression.substring(lastArgumentSeparator, i);
                testAndAddParameter(parameters, parameter);
                closedParenthesesIndex = i;
            } else if (ch == ArithmeticSettings.Instance().Argument_Separator) {
                if (openParenthesesIndex == 0 || lastArgumentSeparator == i) {
                    throw new IllegalArgumentException("No parameter between separator detected. Please check the expression");
                }
                String parameter = trimmedExpression.substring(lastArgumentSeparator, i);
                testAndAddParameter(parameters, parameter);
                lastArgumentSeparator = i + 1;
            }
        }
        if (closedParenthesesIndex == 0) {
            throw new IllegalArgumentException("Mismatched parentheses in function declaration detected. Please check the expression");
        }
        if (openParenthesesIndex == 0) {
            throw new IllegalArgumentException("A function needs at least one parameter. Please check the expression");
        }

        //get rest of expression for function body without assignment character
        String functionBody = trimmedExpression.substring(functionDeclarationIndex + 1);

        //noinspection ToArrayCallWithZeroLengthArrayArgument
        stack.push(new AssignmentToken(name, parameters.toArray(new String[parameters.size()])));
        variableNames.addAll(parameters);

        return functionBody;
    }

    private static void testAndAddParameter(List<String> parameters, String parameter){
        if (!parameter.isBlank()) {
            //Don't create parameters with empty string, to get a variable as reference
            parameters.add(parameter.trim());
        }
    }
}
