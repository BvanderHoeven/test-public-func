package nl.han.ica.icss.generator;


import nl.han.ica.icss.ast.*;
import nl.han.ica.icss.ast.literals.ColorLiteral;
import nl.han.ica.icss.ast.literals.PercentageLiteral;
import nl.han.ica.icss.ast.literals.PixelLiteral;

import java.util.List;
import java.util.stream.Collectors;

public class Generator {

    private final StringBuilder builder;

    public Generator() {
        this.builder = new StringBuilder();
    }

    public String generate(AST ast) {
        this.addNode(ast.root);
        return builder.toString();
    }

    private void addNode(ASTNode astNode) {
        for (ASTNode node : astNode.getChildren()) {
            if (node instanceof Stylerule) {
                this.addSelector(node);

                this.addDeclaration(node);

				builder.append('}')
                .append(System.lineSeparator());
            }
        }
		
        if (this.builder.length() > 1) {
            this.builder.delete(this.builder.length() - 1, this.builder.length());
        }
    }
    private void addDeclaration(ASTNode astNode) {
        for (ASTNode node : astNode.getChildren()) {
            if (node instanceof Declaration) {
                Declaration declaration = (Declaration) node;
				Expression expression = declaration.expression;
				String value = "";
				if (expression instanceof PercentageLiteral) {
					value = ((PercentageLiteral) expression).value + "%";
				}
				if (expression instanceof PixelLiteral) {
					value = ((PixelLiteral) expression).value + "px";
				}
				if (expression instanceof ColorLiteral) {
					value = ((ColorLiteral) expression).value + "";
				}
				
				if(value == "") {
					return;
				}

                this.builder.append("  ")
                        .append(declaration.property.name)
                        .append(": ")
                        .append(value)
                        .append(";") 
						.append(System.lineSeparator());;
            }
        }
    }

	

    private void addSelector(ASTNode astNode) {
        Stylerule stylerule = (Stylerule) astNode;

        List<String> selectors = stylerule.selectors.stream()
                .map(ASTNode::toString)
                .collect(Collectors.toList());

        String str = String.join(", ", selectors);
        this.builder.append(str);

        this.builder.append(" {") 
		.append(System.lineSeparator());;
    }

}
