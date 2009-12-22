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
 * $Id: Parenthesis.java 1841 2009-06-15 15:55:03Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.Expression;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;


// TODO: Auto-generated Javadoc
/**
 * The Class Parenthesis.
 */
public class Parenthesis extends UnaryExpression {

	/**
	 * Instantiates a new parenthesis.
	 * 
	 * @param start the start
	 * @param end the end
	 * @param e the e
	 */
	public Parenthesis(int start, int end, Expression e) {
		super(start, end, LuaExpressionConstants.E_PAREN, e);
	}

}
