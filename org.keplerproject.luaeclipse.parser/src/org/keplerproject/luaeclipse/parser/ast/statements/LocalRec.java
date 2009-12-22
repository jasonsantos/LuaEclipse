/******************************************************************************
 * Copyright (c) 2009 KeplerProject, Sierra Wireless.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 *          - initial API and implementation and initial documentation
 *****************************************************************************/


/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:55:03 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: LocalRec.java 1841 2009-06-15 15:55:03Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.statements;

// TODO: Auto-generated Javadoc
/**
 * The Class LocalRec.
 */
public class LocalRec extends Local implements LuaStatementConstants {

	/**
	 * Instantiates a new local rec.
	 * 
	 * @param start the start
	 * @param end the end
	 * @param identifiers the identifiers
	 */
	public LocalRec(int start, int end, Chunk identifiers) {
		this(start, end, identifiers, null);
	}

	/**
	 * Instantiates a new local rec.
	 * 
	 * @param start the start
	 * @param end the end
	 * @param identifiers the identifiers
	 * @param expressions the expressions
	 */
	public LocalRec(int start, int end, Chunk identifiers, Chunk expressions) {
		super(start, end, identifiers, expressions);
	}

	/* (non-Javadoc)
	 * @see com.anwrt.ldt.parser.ast.statements.Local#getKind()
	 */
	public int getKind(){
		return D_FUNC_DEC;
	}

}
