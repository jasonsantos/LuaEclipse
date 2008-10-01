package org.keplerproject.dltk.ldt.core.parser.ast.expressions;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.keplerproject.dltk.ldt.core.parser.ast.declarations.LuaFunctionDeclaration;

public class Closure extends Expression {
	private LuaFunctionDeclaration functionDeclaration;
	
	public Closure(final LuaFunctionDeclaration decl) {
		functionDeclaration = decl;
	}
	
	public LuaFunctionDeclaration getFunctionDeclaration() {
		return functionDeclaration;
	}

	@Override
	public int getKind() {
		return 0;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		// TODO fazer o traverse
	}

}
