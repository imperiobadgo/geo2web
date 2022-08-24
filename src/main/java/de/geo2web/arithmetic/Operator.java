package de.geo2web.arithmetic;

public enum Operator {
    PLUS {
        @Override
        public Expression handle(ExpressionNode node){
            return OperationEvaluation.handlePlus(node.getLeft().evaluate(), node.getRight().evaluate());
        }
    },
    MINUS {
        @Override
        public Expression handle(ExpressionNode node){
            return OperationEvaluation.handleMinus(node.getLeft().evaluate(), node.getRight().evaluate());
        }
    };

    public abstract Expression handle(ExpressionNode node);
}
