package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.interfaces.IHANLinkedList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.ElseClause;
import nl.han.ica.icss.ast.BooleanClause;
import nl.han.ica.icss.ast.Stylerule;
import nl.han.ica.icss.ast.Stylesheet;
import nl.han.ica.icss.ast.VariableAssignment;
import nl.han.ica.icss.ast.types.ExpressionType;
import nl.han.ica.icss.checker.resolvers.BooleanResolver;
import nl.han.ica.icss.checker.resolvers.ExpressionResolver;
import nl.han.ica.icss.checker.resolvers.VariableResolver;

import java.util.ArrayList;
import java.util.HashMap;



public class Checker {

    private IHANLinkedList<HashMap<String, ExpressionType>> variableTypes;

    private final ExpressionResolver expressionResolver;
    private final BooleanResolver booleanResolver;
    private final VariableResolver variableResolver;

    public Checker() {
        this.variableTypes = new HANLinkedList<>();
        this.variableResolver = new VariableResolver(variableTypes);
        this.expressionResolver = new ExpressionResolver(variableResolver);
        this.booleanResolver = new BooleanResolver(expressionResolver);
    }

    public void check(AST ast) {
        this.checkRootNode(ast.root);
    }

    private void checkRootNode(ASTNode astNode) {
        Stylesheet stylesheet = (Stylesheet) astNode;

        variableTypes.addFirst(new HashMap<>());

        for (ASTNode child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                this.variableResolver.resolveASTNode(child);
                continue;
            }

            if (child instanceof Stylerule) {
                variableTypes.addFirst(new HashMap<>());
                this.resolveStylerule(child);
                variableTypes.removeFirst();
            }

        }

        variableTypes.removeFirst();
    }

    private void resolveStylerule(ASTNode astNode) {
        Stylerule stylerule = (Stylerule) astNode;
        this.resolveRuleBody(stylerule.body);
    }

    private void resolveRuleBody(ArrayList<ASTNode> astNodes) {
        for (ASTNode astNode : astNodes) {
            if (astNode instanceof Declaration) {
                this.resolveDeclaration(astNode);
                continue;
            }

            if (astNode instanceof BooleanClause) {
                this.resolveIfClause(astNode);
                continue;
            }

            if (astNode instanceof VariableAssignment) {
                this.variableResolver.resolveASTNode(astNode);
            }
        }
    }    

    private void resolveDeclaration(ASTNode astNode) {
        Declaration declaration = (Declaration) astNode;
        ExpressionType expressionType = this.expressionResolver.getExpressionTypeForASTNode(declaration.expression);

        String declarationName = declaration.property.name;
        switch (declarationName) {
            case "color":
                if (expressionType != ExpressionType.COLOR) {
                    astNode.setError(String.format("Declaration & Expressiontype mismatch, Declaration:'%s', expressionType:'%s'.", declarationName, expressionType));
                }
                break;
            case "background-color":
                if (expressionType != ExpressionType.COLOR) {
                    astNode.setError(String.format("Declaration & Expressiontype mismatch, Declaration:'%s', expressionType:'%s'.", declarationName, expressionType));
                }
                break;
            case "width":
                if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError(String.format("Declaration & Expressiontype mismatch, Declaration:'%s', expressionType:'%s'.", declarationName, expressionType));
                }
                break;
            case "height":
                if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError(String.format("Declaration & Expressiontype mismatch, Declaration: '%s', expressionType:'%s'.", declarationName, expressionType));
                }
                break;
            default:
                astNode.setError(String.format("Declaration name: '%s' not found", declarationName));
                break;
        }
    }

    private void resolveIfClause(ASTNode astNode) {
        BooleanClause ifClause = (BooleanClause) astNode;
        variableTypes.addFirst(new HashMap<>());
        this.booleanResolver.resolveBoolean(ifClause);
        this.resolveRuleBody(ifClause.body);
        this.variableTypes.removeFirst();

        if (ifClause.elseClause != null) {
            this.variableTypes.addFirst(new HashMap<>());
            this.resolveElse(ifClause.elseClause);
            this.variableTypes.removeFirst();
        }
    }

    private void resolveElse(ASTNode astNode) {
        ElseClause elseClause = (ElseClause) astNode;
        this.resolveRuleBody(elseClause.body);
    }    
}
