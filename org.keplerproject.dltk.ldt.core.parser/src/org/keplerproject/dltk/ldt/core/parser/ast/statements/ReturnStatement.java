package org.keplerproject.dltk.ldt.core.parser.ast.statements;

import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;

public class ReturnStatement extends Statement {
	private List<Expression> results;

	public ReturnStatement(List<Expression> results) {
		this.results = results;
	}

	@Override
	public int getKind() {
		return S_RETURN;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			for (Expression result : results) {
				if (result != null)
					result.traverse(visitor);
				visitor.endvisit(this);
			}
		}
	}
}
