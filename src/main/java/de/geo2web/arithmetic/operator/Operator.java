/*
 *Source of inspiration: https://github.com/fasseg/exp4j
 */
package de.geo2web.arithmetic.operator;

import de.geo2web.arithmetic.Operand;
import de.geo2web.arithmetic.VariableOperand;

/**
 * Class representing operators that can be used in an expression
 */
public abstract class Operator {
    /**
     * The precedence value for the addition operation
     */
    public static final int PRECEDENCE_ADDITION = 500;
    /**
     * The precedence value for the subtraction operation
     */
    public static final int PRECEDENCE_SUBTRACTION = PRECEDENCE_ADDITION;
    /**
     * The precedence value for the multiplication operation
     */
    public static final int PRECEDENCE_MULTIPLICATION = 1000;
    /**
     * The precedence value for the division operation
     */
    public static final int PRECEDENCE_DIVISION = PRECEDENCE_MULTIPLICATION;
    /**
     * The precedence value for the modulo operation
     */
    public static final int PRECEDENCE_MODULO = PRECEDENCE_DIVISION;
    /**
     * The precedence value for the power operation
     */
    public static final int PRECEDENCE_POWER = 10000;
    /**
     * The precedence value for the unary minus operation
     */
    public static final int PRECEDENCE_UNARY_MINUS = 5000;
    /**
     * The precedence value for the unary plus operation
     */
    public static final int PRECEDENCE_UNARY_PLUS = PRECEDENCE_UNARY_MINUS;

    /**
     * The max precedence value
     */
    public static final int PRECEDENCE_MAX = 100000;

    /**
     * The set of allowed operator chars
     */
    public static final char[] ALLOWED_OPERATOR_CHARS = {'+', '-', '*', '/', '%', '^', '!', '#', '§',
            '$', '&', ';', ':', '~', '<', '>', '|', '=', '÷', '√', '∛', '⌈', '⌊'};

    private final int numOperands;
    private final boolean leftAssociative;
    private final String symbol;
    private final int precedence;

    /**
     * Create a new operator for use in expressions
     *
     * @param symbol           the symbol of the operator
     * @param numberOfOperands the number of operands the operator takes (1 or 2)
     * @param leftAssociative  set to true if the operator is left associative, false if it is right associative
     * @param precedence       the precedence value of the operator
     */
    public Operator(String symbol, int numberOfOperands, boolean leftAssociative,
                    int precedence) {
        super();
        this.numOperands = numberOfOperands;
        this.leftAssociative = leftAssociative;
        this.symbol = symbol;
        this.precedence = precedence;
    }

    /**
     * Check if a character is an allowed operator char
     *
     * @param ch the char to check
     * @return true if the char is allowed on an operator symbol, false otherwise
     */
    public static boolean isAllowedOperatorChar(char ch) {
        for (char allowed : ALLOWED_OPERATOR_CHARS) {
            if (ch == allowed) {
                return true;
            }
        }
        return false;
    }

    /**
     * Check if the operator is left associative
     *
     * @return true os the operator is left associative, false otherwise
     */
    public boolean isLeftAssociative() {
        return leftAssociative;
    }

    /**
     * Check the precedence value for the operator
     *
     * @return the precedence value
     */
    public int getPrecedence() {
        return precedence;
    }

    /**
     * This method checks whether there are unresolved variables in the operands before the actual calculation.
     * @param args the set of arguments used for calculating the operation
     * @return the calculated result of the operation or an instruction
     */
    public Operand applyWithVariableCheck(Operand... args){
        if (VariableOperand.containsVariable(args)){
            return VariableOperand.convertOperator(args, this);
        }
        return apply(args);
    }

    /**
     * Apply the operation on the given operands
     *
     * @param args the operands for the operation
     * @return the calculated result of the operation
     */
    public abstract Operand apply(Operand... args);

    /**
     * Get the operator symbol
     *
     * @return the symbol
     */
    public String getSymbol() {
        return symbol;
    }

    /**
     * Get the number of operands
     *
     * @return the number of operands
     */
    public int getNumOperands() {
        return numOperands;
    }
}
