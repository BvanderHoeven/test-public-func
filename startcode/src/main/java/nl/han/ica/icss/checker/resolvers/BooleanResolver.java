package nl.han.ica.icss.checker.resolvers;

import nl.han.ica.icss.ast.Expression;
import nl.han.ica.icss.ast.BooleanClause;
import nl.han.ica.icss.ast.types.ExpressionType;

public class BooleanResolver {

    private final ExpressionResolver checkExpression;

    public BooleanResolver(ExpressionResolver checkExpression) {
        this.checkExpression = checkExpression;
    }

    public ExpressionType resolveBoolean(BooleanClause ifClause) {
        Expression conditionalExpression = ifClause.conditionalExpression;
        ExpressionType expressionType = this.checkExpression.getExpressionTypeForExpression(conditionalExpression);

        if (expressionType != ExpressionType.BOOL) {
            ifClause.setError("ConditionalExpression should be a boolean literal.");
            return ExpressionType.UNDEFINED;
        }

        return ExpressionType.BOOL;
    }
}
