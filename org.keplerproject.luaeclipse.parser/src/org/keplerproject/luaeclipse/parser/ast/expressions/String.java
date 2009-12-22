/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: String.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.Literal;
import org.eclipse.dltk.utils.CorePrinter;
import org.keplerproject.luaeclipse.internal.parser.Index;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;


// TODO: Auto-generated Javadoc
/**
 * The Class String.
 */
public class String extends Literal implements Index {

	private long id;

	/**
	 * Instantiates a new string.
	 * 
	 * @param start the start
	 * @param end the end
	 */
	public String(int start, int end, java.lang.String value) {
		super(start, end);
		fLiteralValue = value;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return LuaExpressionConstants.STRING_LITERAL;
	}

	public long getID() {
		return id;
	}

	public void printNode(CorePrinter output){
		output.formatPrintLn(getValue());
	}
	
	public void setID(long id) {
		this.id = id;
	}
}
