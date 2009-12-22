/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Number.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.Literal;
import org.eclipse.dltk.utils.CorePrinter;
import org.keplerproject.luaeclipse.internal.parser.Index;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;



// TODO: Auto-generated Javadoc
/**
 * The Class Number.
 */
public class Number extends Literal implements LuaExpressionConstants, Index {

	/** Number's value. */
	private Double fNumberValue;
	private long id;

	/**
	 * Instantiates a new number.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param value
	 *            the value
	 */
	public Number(int start, int end, double value) {
		super(start, end);
		fNumberValue = value;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return NUMBER_LITERAL;
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

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.expressions.Literal#getValue()
	 */
	public java.lang.String getValue() {
		return this.toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.expressions.Literal#toString()
	 */
	public java.lang.String toString() {
		return fNumberValue.toString();
	}
}
