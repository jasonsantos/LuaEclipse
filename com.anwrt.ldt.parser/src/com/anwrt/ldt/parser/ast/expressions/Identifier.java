/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Identifier.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.ExpressionConstants;
import org.eclipse.dltk.ast.expressions.Literal;

// TODO: Auto-generated Javadoc
/**
 * Used to define variables' names.
 * 
 * @author kkinfoo
 */
public class Identifier extends Literal implements LeftHandSide {

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
	public Identifier(int start, int end, java.lang.String value) {
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
	 * Gets the kind.
	 * 
	 * @return Parser's token value
	 */
	@Override
	public int getKind() {
		return ExpressionConstants.E_IDENTIFIER;
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
	public java.lang.String toString() {
		return fLiteralValue;
	}

}
