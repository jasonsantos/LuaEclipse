package org.keplerproject.dltk.ldt.core.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;

public class IfStatement extends Statement {
	private Expression condition;
	private Block thenStatement;
	private Block elseStatement;
	
	public IfStatement(final Expression condition, final Block thenStatement, final Block elseStatement) {
		this.condition = condition;
		this.thenStatement = thenStatement;
		this.elseStatement = elseStatement;
	}
	
	@Override
	public int getKind() {
		return S_IF;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			if (condition != null) condition.traverse(visitor);
			if (thenStatement != null) thenStatement.traverse(visitor);
			if (elseStatement != null) elseStatement.traverse(visitor);
			visitor.endvisit(this);
		}
	}

	public void setCondition(Expression condition) {
		this.condition = condition;
	}

	public void setThenStatement(Block thenStatement) {
		this.thenStatement = thenStatement;
	}

	public void setElseStatement(Block elseStatement) {
		this.elseStatement = elseStatement;
	}
}
