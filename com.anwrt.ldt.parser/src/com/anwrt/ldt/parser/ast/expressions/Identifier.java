/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Identifier.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.ExpressionConstants;
import org.eclipse.dltk.ast.expressions.Literal;
import java.lang.String;

import com.anwrt.ldt.internal.parser.Index;

// TODO: Auto-generated Javadoc
/**
 * Used to define variables' names.
 * 
 * @author kkinfoo
 */
public class Identifier extends Literal implements LeftHandSide, Index {

	private long id;

	/**
	 * Instantiates a new identifier.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param value
	 *            the value
	 */
	public Identifier(int start, int end, String value) {
		super(start, end);
		fLiteralValue = value;
	}

	/**
	 * Instantiates a new identifier.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public Identifier(int start, int end) {
		this(start, end, "");
	}

	/**
	 * Instantiates a new identifier.
	 * 
	 * @param value
	 *            the value
	 */
	public Identifier(String value) {
		this(0, 0, value);
	}

	/**
	 * Gets the kind.
	 * 
	 * @return Parser's token value
	 */
	@Override
	public int getKind() {
		return ExpressionConstants.E_IDENTIFIER;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anwrt.ldt.parser.ast.expressions.LeftHandSide#isLeftHandSide()
	 */
	@Override
	public boolean isLeftHandSide() {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.expressions.Literal#toString()
	 */
	public String toString() {
		return fLiteralValue;
	}

}
