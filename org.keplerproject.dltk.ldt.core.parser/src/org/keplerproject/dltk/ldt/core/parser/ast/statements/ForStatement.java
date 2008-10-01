package org.keplerproject.dltk.ldt.core.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;

public class ForStatement extends Statement {
	private Expression init;
	private Expression condition;
	private Expression step;
	private Block action;
	
	@Override
	public int getKind() {
		return S_FOR;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			if (init != null) init.traverse(visitor);
			if (condition != null) condition.traverse(visitor);
			if (step != null) step.traverse(visitor);
			if (action != null) action.traverse(visitor);
			visitor.endvisit(this);
		}
	}

}
