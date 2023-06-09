package nl.han.ica.icss.checker.resolvers;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.BooleanClause;
import nl.han.ica.icss.ast.types.ExpressionType;

public class BooleanResolver {

    private final ExpressionResolver checkExpression;

    public BooleanResolver(ExpressionResolver checkExpression) {
        this.checkExpression = checkExpression;
    }

    public ExpressionType resolveBoolean(BooleanClause booleanClause) {
        Expression conditionalExpression = booleanClause.booleanExpression;
        ExpressionType expressionType = this.checkExpression.getExpressionTypeForExpression(conditionalExpression);

        if (expressionType != ExpressionType.BOOL) {
            booleanClause.setError(String.format("Expression type should be boolean, but was: '%s'.", expressionType));
            return ExpressionType.UNDEFINED;
        }

        return ExpressionType.BOOL;
    }
}
