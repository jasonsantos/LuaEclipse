package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.NilLiteral;

import com.anwrt.ldt.internal.parser.Index;
import com.anwrt.ldt.parser.LuaExpressionConstants;

public class Nil extends NilLiteral  implements LuaExpressionConstants, Index{

	private long id;

	public Nil(int start, int end) {
		super(start, end);
	}
	
	@Override
	public int getKind() {
		return NIL_LITTERAL;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

}
