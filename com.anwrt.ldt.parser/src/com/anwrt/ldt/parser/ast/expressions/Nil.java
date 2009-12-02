package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.NilLiteral;

import com.anwrt.ldt.parser.LuaExpressionConstants;

public class Nil extends NilLiteral  implements LuaExpressionConstants{

	public Nil(int start, int end) {
		super(start, end);
	}
	
	@Override
	public int getKind() {
		return NIL_LITTERAL;
	}
}
