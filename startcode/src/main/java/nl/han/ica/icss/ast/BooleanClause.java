package nl.han.ica.icss.ast;

import java.util.ArrayList;
import java.util.Objects;

public class BooleanClause extends ASTNode {


    public Expression booleanExpression;
    public ArrayList<ASTNode> body = new ArrayList<>();
    public ElseClause elseClause;

    public BooleanClause() { }

    public BooleanClause(Expression conditionalExpression, ArrayList<ASTNode> body) {

        this.booleanExpression = conditionalExpression;
        this.body = body;
    }
    public BooleanClause(Expression conditionalExpression, ArrayList<ASTNode> body, ElseClause elseClause) {

        this.booleanExpression = conditionalExpression;
        this.body = body;
        this.elseClause = elseClause;
    }

    @Override
    public String getNodeLabel() {
        return "Boolean_Clause";
    }
    @Override
    public ArrayList<ASTNode> getChildren() {
        ArrayList<ASTNode> children = new ArrayList<>();
        children.add(booleanExpression);
        children.addAll(body);
        if (elseClause!=null)
            children.add(elseClause);

        return children;
    }

    @Override
    public ASTNode addChild(ASTNode child) {
        if(child instanceof Expression)
            booleanExpression  = (Expression) child;
        else if (child instanceof ElseClause)
            elseClause = (ElseClause) child;
        else
            body.add(child);

        return this;
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        BooleanClause ifClause = (BooleanClause) o;
        if (this.elseClause == null)
            return Objects.equals(booleanExpression, ifClause.getBooleanExpression()) &&
                Objects.equals(body, ifClause.body);
        else
            return Objects.equals(booleanExpression, ifClause.getBooleanExpression()) &&
                    Objects.equals(body, ifClause.body) &&
                    Objects.equals(elseClause, ifClause.elseClause);

    }

    @Override
    public int hashCode() {
        return Objects.hash(booleanExpression, body, elseClause);
    }

    public Expression getBooleanExpression() {
        return booleanExpression;
    }
    public ElseClause getElseClause() { return elseClause; }
}
