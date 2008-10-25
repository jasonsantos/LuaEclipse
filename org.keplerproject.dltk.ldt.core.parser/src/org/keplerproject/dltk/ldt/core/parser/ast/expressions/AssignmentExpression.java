package org.keplerproject.dltk.ldt.core.parser.ast.expressions;

import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.keplerproject.dltk.ldt.core.parser.ast.references.LuaVariableReference;

public class AssignmentExpression extends Expression {
	private List<LuaVariableReference> lhs;
	private List<? extends Statement> rhs;

	public AssignmentExpression(List<LuaVariableReference> lhs, List<? extends Statement> rhs) {
		this.lhs = lhs;
		this.rhs = rhs;
	}
		
	@Override
	public int getKind() {
		return E_ASSIGN;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			if (lhs != null) {
				for (LuaVariableReference var : lhs) {
					var.traverse(visitor);
				}
			}
			if (rhs != null) {
				for (Statement stm : rhs) {
					stm.traverse(visitor);
				}
			}
			visitor.endvisit(this);
		}
	}

	@Override
	public String toString() {
		return String.format("%s = %s", lhs.toString(), rhs.toString());
	}
}
