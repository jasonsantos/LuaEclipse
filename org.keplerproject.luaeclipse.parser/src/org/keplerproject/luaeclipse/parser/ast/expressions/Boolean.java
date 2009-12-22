package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.BooleanLiteral;
import org.keplerproject.luaeclipse.internal.parser.Index;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;


public class Boolean extends BooleanLiteral implements Index,
		LuaExpressionConstants {

	private long id;

	public Boolean(int start, int end, boolean b) {
		super(start, end, b);
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public int getKind() {
		return boolValue() ? BOOL_TRUE : BOOL_FALSE;
	}

}
