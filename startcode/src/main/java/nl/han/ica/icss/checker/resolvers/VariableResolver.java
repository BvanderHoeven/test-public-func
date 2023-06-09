package nl.han.ica.icss.checker.resolvers;

import java.util.HashMap;

import nl.han.ica.datastructures.interfaces.IHANLinkedList;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.VariableReference;
import nl.han.ica.icss.ast.types.ExpressionType;

public class VariableResolver {

    private final IHANLinkedList<HashMap<String, ExpressionType>> expressionTypes;
    private final ExpressionResolver expressionResolver;

    public VariableResolver(IHANLinkedList<HashMap<String, ExpressionType>> expressionTypes) {
        this.expressionTypes = expressionTypes;
        this.expressionResolver = new ExpressionResolver(this);
    }

    public void resolveASTNode(ASTNode astNode) {
        VariableAssignment variableAssignment = (VariableAssignment) astNode;
        VariableReference variableReference = variableAssignment.name;
        ExpressionType expressionType = expressionResolver.getExpressionTypeForASTNode(variableAssignment.expression);

        if (expressionType == null || expressionType == ExpressionType.UNDEFINED) {
            astNode.setError(String.format("Variable assignment %s is invalid.", variableReference.name));
            return;
        }

        ExpressionType previousExpressionType = getExpressionTypeByName(variableReference.name);
        if (previousExpressionType != null && expressionType != previousExpressionType) {
            astNode.setError(String.format("Can't change from '%s' to '%s'", previousExpressionType.toString(), expressionType.toString()));
        }

        expressionTypes.getFirst().put(variableReference.name, expressionType);
    }

    public ExpressionType getExpressionTypeForVariableReference(VariableReference variableReference) {
        ExpressionType expressionType = getExpressionTypeByName(variableReference.name);
        if (expressionType == null) {
            variableReference.setError(String.format("Variable: '%s' not declared or in scope.", variableReference.name));
            return null;
        }
        return expressionType;
    }

    private ExpressionType getExpressionTypeByName(String name) {
        for (HashMap<String, ExpressionType> expressionType : expressionTypes) {
            ExpressionType type = expressionType.get(name);
            if (type != null) {
                return type;
            }
        }
        return null;
    }
}
