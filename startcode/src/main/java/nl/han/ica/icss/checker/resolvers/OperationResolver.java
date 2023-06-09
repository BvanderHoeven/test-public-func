package nl.han.ica.icss.checker.resolvers;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.Operation;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;
import nl.han.ica.icss.ast.types.ExpressionType;

public class OperationResolver {

    private final ExpressionResolver expressionResolver;

    public OperationResolver(ExpressionResolver checkExpression) {
        this.expressionResolver = checkExpression;
    }

    // Returns the expression type for the given operation.
    public ExpressionType getExpressionTypeForOperation(Operation operation) {
        ExpressionType left = getExpressionType(operation.lhs);
        ExpressionType right = getExpressionType(operation.rhs);

        if (left == ExpressionType.COLOR || right == ExpressionType.COLOR || left == ExpressionType.BOOL || right == ExpressionType.BOOL) {
            operation.setError(String.format("Type '%s' not allowed in operation", left));
            return ExpressionType.UNDEFINED;
        }

        if (operation instanceof MultiplyOperation) {
            if (left != ExpressionType.SCALAR && right != ExpressionType.SCALAR) {
                operation.setError("Multiply is only allowed with at least one scalar literal.");
                return ExpressionType.UNDEFINED;
            }
            return right != ExpressionType.SCALAR ? right : left;
        } 
        else if ((operation instanceof SubtractOperation || operation instanceof AddOperation) && left != right) {
            operation.setError(String.format("Left: '%s' and Right: '%s' need to be the same type for this operation: '%s'.", left, right, operation.getNodeLabel()));
            return ExpressionType.UNDEFINED;
        }

        return left;
    }

    private ExpressionType getExpressionType(Expression expression) {
        if (expression instanceof Operation) {
            return getExpressionTypeForOperation((Operation) expression);
        } else {
            return expressionResolver.getExpressionTypeForExpression(expression);
        }
    }
}
