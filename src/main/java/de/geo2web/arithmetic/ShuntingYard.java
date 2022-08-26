/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic;

import de.geo2web.arithmetic.operator.Operator;
import de.geo2web.arithmetic.function.Function;
import de.geo2web.arithmetic.tokenizer.OperatorToken;
import de.geo2web.arithmetic.tokenizer.Token;
import de.geo2web.arithmetic.tokenizer.Tokenizer;

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
    public static Token[] convertToRPN(final String expression, final Map<String, Function> userFunctions,
                                       final Map<String, Operator> userOperators, final Set<String> variableNames, final boolean implicitMultiplication) {
        final Stack<Token> stack = new Stack<>();
        final List<Token> output = new ArrayList<>();

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
}
