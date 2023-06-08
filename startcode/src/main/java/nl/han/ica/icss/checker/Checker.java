package nl.han.ica.icss.checker;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.interfaces.IHANLinkedList;
import nl.han.ica.icss.ast.AST;
import nl.han.ica.icss.ast.ASTNode;
import nl.han.ica.icss.ast.Declaration;
import nl.han.ica.icss.ast.ElseClause;
import nl.han.ica.icss.ast.IfClause;
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
        this.checkStylesheet(ast.root);
    }

    private void checkStylesheet(ASTNode astNode) {
        Stylesheet stylesheet = (Stylesheet) astNode;

        variableTypes.addFirst(new HashMap<>());

        for (ASTNode child : stylesheet.getChildren()) {
            if (child instanceof VariableAssignment) {
                this.variableResolver.checkASTNode(child);
                continue;
            }

            if (child instanceof Stylerule) {
                variableTypes.addFirst(new HashMap<>());
                this.checkStylerule(child);
                variableTypes.removeFirst();
            }

        }

        variableTypes.removeFirst();
    }

    private void checkStylerule(ASTNode astNode) {
        Stylerule stylerule = (Stylerule) astNode;
        this.checkRuleBody(stylerule.body);
    }

    private void checkRuleBody(ArrayList<ASTNode> astNodes) {
        for (ASTNode astNode : astNodes) {
            if (astNode instanceof Declaration) {
                this.checkDeclaration(astNode);
                continue;
            }

            if (astNode instanceof IfClause) {
                this.checkIfClause(astNode);
                continue;
            }

            if (astNode instanceof VariableAssignment) {
                this.variableResolver.checkASTNode(astNode);
            }
        }
    }

    private void checkIfClause(ASTNode astNode) {
        IfClause ifClause = (IfClause) astNode;
        variableTypes.addFirst(new HashMap<>());
        this.booleanResolver.resolveBoolean(ifClause);
        this.checkRuleBody(ifClause.body);
        this.variableTypes.removeFirst();

        if (ifClause.elseClause != null) {
            this.variableTypes.addFirst(new HashMap<>());
            this.checkElseClause(ifClause.elseClause);
            this.variableTypes.removeFirst();
        }
    }

    private void checkElseClause(ASTNode astNode) {
        ElseClause elseClause = (ElseClause) astNode;
        this.checkRuleBody(elseClause.body);
    }

    private void checkDeclaration(ASTNode astNode) {
        Declaration declaration = (Declaration) astNode;
        ExpressionType expressionType = this.expressionResolver.getExpressionTypeForASTNode(declaration.expression);

        switch (declaration.property.name) {
            case "color":
                if (expressionType != ExpressionType.COLOR) {
                    astNode.setError("Color value can only be a color literal.");
                }
                break;
            case "background-color":
                if (expressionType != ExpressionType.COLOR) {
                    astNode.setError("Background-color value can only be a color literal.");
                }
                break;
            case "width":
                if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError("Width value can only be a pixel or percentage literal.");
                }
                break;
            case "height":
                if (expressionType != ExpressionType.PIXEL && expressionType != ExpressionType.PERCENTAGE) {
                    astNode.setError("Height value can only be a pixel or percentage literal.");
                }
                break;
            default:
                astNode.setError("The only properties allowed are height, width, background-color and color.");
                break;
        }
    }

    
}
