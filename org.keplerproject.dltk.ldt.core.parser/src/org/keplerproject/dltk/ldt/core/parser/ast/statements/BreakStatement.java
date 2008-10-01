package org.keplerproject.dltk.ldt.core.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.statements.Statement;

public class BreakStatement extends Statement {

	@Override
	public int getKind() {
		return S_BREAK;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {

	}

}
