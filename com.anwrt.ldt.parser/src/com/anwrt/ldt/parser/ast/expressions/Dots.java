/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Dots.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.Expression;

import com.anwrt.ldt.parser.LuaExpressionConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class Dots.
 */
public class Dots extends Expression implements LuaExpressionConstants {

	/**
	 * Instantiates a new dots.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public Dots(int start, int end) {
		super(start, end);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return E_DOTS;
	}
}
