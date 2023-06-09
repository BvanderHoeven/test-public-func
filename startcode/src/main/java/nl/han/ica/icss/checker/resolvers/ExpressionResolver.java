package nl.han.ica.icss.checker.resolvers;

import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.literals.*;
import nl.han.ica.icss.ast.types.ExpressionType;

public class ExpressionResolver {

    private final OperationResolver checkOperation;
    private final VariableResolver checkVariable;

    public ExpressionResolver(VariableResolver checkVariable) {
        this.checkVariable = checkVariable;
        this.checkOperation = new OperationResolver(this);
    }

    public ExpressionType getExpressionTypeForASTNode(ASTNode astNode) {
        Expression expression = (Expression) astNode;

        if (expression instanceof Operation) {
            return this.checkOperation.getExpressionTypeForOperation((Operation) expression);
        }

        return this.getExpressionTypeForExpression(expression);
    }

    public ExpressionType getExpressionTypeForExpression(Expression expression) {

        if (expression instanceof VariableReference) {
            return this.checkVariable.getExpressionTypeForVariableReference((VariableReference) expression);
        } else {
            if (expression instanceof PercentageLiteral) {
                return ExpressionType.PERCENTAGE;
            } else if (expression instanceof ColorLiteral) {
                return ExpressionType.COLOR;
            } else if (expression instanceof ScalarLiteral) {
                return ExpressionType.SCALAR;
            } else if (expression instanceof PixelLiteral) {
                return ExpressionType.PIXEL;
            } else if (expression instanceof BoolLiteral) {
                return ExpressionType.BOOL;
            }
        }

        return ExpressionType.UNDEFINED;
    }
}
