/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:55:03 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Break.java 1841 2009-06-15 15:55:03Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.statements;


// TODO: Auto-generated Javadoc
/**
 * The Class Break.
 */
public class Break extends SimpleStatement {

	/**
	 * Instantiates a new break.
	 * 
	 * @param start the start
	 * @param end the end
	 */
	public Break(int start, int end) {
		super(start, end, null);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return LuaStatementConstants.S_BREAK;
	}

}
