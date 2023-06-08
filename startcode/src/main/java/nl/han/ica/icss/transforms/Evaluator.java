package nl.han.ica.icss.transforms;

import nl.han.ica.datastructures.HANLinkedList;
import nl.han.ica.datastructures.interfaces.IHANLinkedList;
import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.BoolLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;
import nl.han.ica.icss.ast.literals.ScalarLiteral;
import nl.han.ica.icss.ast.operations.AddOperation;
import nl.han.ica.icss.ast.operations.MultiplyOperation;
import nl.han.ica.icss.ast.operations.SubtractOperation;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class Evaluator implements Transform {

    private final IHANLinkedList<HashMap<String, Literal>> variableValues;

    public Evaluator() {
        variableValues = new HANLinkedList<>();
    }

    @Override
    public void apply(AST ast) {
        Stylesheet stylesheet = ast.root;
        this.transformStylesheet(stylesheet);
    }

    private void transformStylesheet(ASTNode astNode) {
        List<ASTNode> toRemove = new ArrayList<>();

        this.variableValues.addFirst(new HashMap<>());

        for (ASTNode child : astNode.getChildren()) {
            if (child instanceof VariableAssignment) {
                this.transformVariableAssignment((VariableAssignment) child);
                toRemove.add(child);
                continue;
            }

            if (child instanceof Stylerule) {
                this.transformStylerule((Stylerule) child);
            }
        }

        this.variableValues.removeFirst();

        toRemove.forEach(astNode::removeChild);
    }

    private void transformStylerule(Stylerule stylerule) {
        ArrayList<ASTNode> toAdd = new ArrayList<>();

        this.variableValues.addFirst(new HashMap<>());

        for (ASTNode child : stylerule.body) {
            this.transformRuleBody(child, toAdd);
        }

       this.variableValues.removeFirst();

        if (this.hasDuplicateDeclaration(toAdd)) {
            stylerule.setError("Duplicate css properties are declared.");
        }

        stylerule.body = toAdd;
    }

    private void transformRuleBody(ASTNode astNode, ArrayList<ASTNode> parentBody) {
        if (astNode instanceof VariableAssignment) {
            this.transformVariableAssignment((VariableAssignment) astNode);
            return;
        }

        if (astNode instanceof Declaration) {
            this.transformDeclaration((Declaration) astNode);
            parentBody.add(astNode);
            return;
        }

        if (astNode instanceof IfClause) {
            IfClause ifClause = (IfClause) astNode;
            ifClause.conditionalExpression = this.transformExpression(ifClause.conditionalExpression);

            if (((BoolLiteral) ifClause.conditionalExpression).value) {
                if (ifClause.elseClause != null) {
                    ifClause.elseClause.body = new ArrayList<>();
                }
            } else {
                if (ifClause.elseClause == null) {
                    ifClause.body = new ArrayList<>();
                    return;
                } else {
                    ifClause.body = ifClause.elseClause.body;
                    ifClause.elseClause.body = new ArrayList<>();
                }
            }

            this.transformIfClause((IfClause) astNode, parentBody);
        }
    }

    private void transformIfClause(IfClause ifClause, ArrayList<ASTNode> parentBody) {
        for (ASTNode child : ifClause.getChildren()) {
            this.transformRuleBody(child, parentBody);
        }
    }

    private void transformDeclaration(Declaration declaration) {
        declaration.expression = this.transformExpression(declaration.expression);
    }

    private void transformVariableAssignment(VariableAssignment variableAssignment) {
        Expression expression = variableAssignment.expression;
        variableAssignment.expression = this.transformExpression(expression);
        this.variableValues.getFirst().put(variableAssignment.name.name, (Literal) variableAssignment.expression);
    }


    private Literal transformExpression(Expression expression) {
        if (expression instanceof Operation) {
            return this.transformOperation((Operation) expression);
        }

        if (expression instanceof VariableReference) {
            return this.getLiteralByName(((VariableReference) expression).name);
        }

        return (Literal) expression;
    }

    private Literal transformOperation(Operation operation) {
        Literal left;
        Literal right;

        int leftValue;
        int rightValue;

        if (operation.lhs instanceof Operation) {
            left = this.transformOperation((Operation) operation.lhs);
        } else if (operation.lhs instanceof VariableReference) {
            left = this.getLiteralByName(((VariableReference) operation.lhs).name);
        } else {
            left = (Literal) operation.lhs;
        }

        if (operation.rhs instanceof Operation) {
            right = this.transformOperation((Operation) operation.rhs);
        } else if (operation.rhs instanceof VariableReference) {
            right = this.getLiteralByName(((VariableReference) operation.rhs).name);
        } else {
            right = (Literal) operation.rhs;
        }

        leftValue = this.getLiteralValue(left);
        rightValue = this.getLiteralValue(right);

        if (operation instanceof AddOperation) {
            return this.newLiteral(left, leftValue + rightValue);
        } else if (operation instanceof SubtractOperation) {
            return this.newLiteral(left, leftValue - rightValue);
        } else if (operation instanceof MultiplyOperation) {
            if (right instanceof ScalarLiteral) {
                return this.newLiteral(left, leftValue * rightValue);
            } else {
                return this.newLiteral(right, leftValue * rightValue);
            }
        } else {
            return this.newLiteral(left, leftValue / rightValue);
        }
    }

    private int getLiteralValue(Literal literal) {
        if (literal instanceof PixelLiteral) {
            return ((PixelLiteral) literal).value;
        } else if (literal instanceof ScalarLiteral) {
            return ((ScalarLiteral) literal).value;
        } else {
            return ((PercentageLiteral) literal).value;
        }
    }

    private Literal newLiteral(Literal literal, int value) {
        if (literal instanceof PixelLiteral) {
            return new PixelLiteral(value);
        } else if (literal instanceof ScalarLiteral) {
            return new ScalarLiteral(value);
        } else {
            return new PercentageLiteral(value);
        }
    }

    public boolean hasDuplicateDeclaration(List<ASTNode> astNodes) {
        Set<String> appeared = new HashSet<>();
        for (ASTNode astNode : astNodes) {
            if (!appeared.add(((Declaration) astNode).property.name)) {
                return true;
            }
        }
        return false;
    }

    private Literal getLiteralByName(String name) {
        for (HashMap<String, Literal> value : variableValues) {
            Literal type = value.get(name);
            if (type != null) {
                return type;
            }
        }
        return null;
    }
}
