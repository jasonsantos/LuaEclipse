package org.keplerproject.dltk.ldt.core.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;

public class RepeatStatement extends Statement {
	private Expression condition;
	private Block action;
	
	public RepeatStatement(final Expression condition, final Block action) {
		this.condition = condition;
		this.action = action;
	}
	
	@Override
	public int getKind() {
		return S_DOWHILE;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			if (condition != null) condition.traverse(visitor);
			if (action != null) action.traverse(visitor);
			visitor.endvisit(this);
		}
	}

}
