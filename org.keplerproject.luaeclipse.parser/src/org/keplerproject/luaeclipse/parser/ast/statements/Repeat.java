/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:55:03 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Repeat.java 1841 2009-06-15 15:55:03Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.statements;

import org.eclipse.dltk.ast.expressions.Expression;

// TODO: Auto-generated Javadoc
/**
 * The Class Repeat.
 */
public class Repeat extends While implements LuaStatementConstants {

	/**
	 * Instantiates a new repeat.
	 * 
	 * @param start the start
	 * @param end the end
	 * @param bloc the bloc
	 * @param expr the expr
	 */
	public Repeat(int start, int end, Chunk bloc, Expression expr) {
		super(start, end, expr, bloc);
	}

	/* (non-Javadoc)
	 * @see com.anwrt.ldt.parser.ast.statements.While#getKind()
	 */
	public int getKind() {
		return S_UNTIL;
	}
}
