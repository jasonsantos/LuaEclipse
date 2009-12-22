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
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Call.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.CallExpression;
import org.eclipse.dltk.ast.expressions.Expression;
import org.keplerproject.luaeclipse.internal.parser.NameFinder;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;


// TODO: Auto-generated Javadoc
/**
 * The Class Call.
 */
public class Call extends CallExpression implements LuaExpressionConstants {

	/**
	 * Instantiates a new call.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param name
	 *            the name
	 * @param params
	 *            the params
	 */
	public Call(int start, int end, Expression name, CallArgumentsList args) {
		super(start, end, name, NameFinder.extractName(name), args);
	}

	public Call(int start, int end, Expression name) {
		super(start, end, name, NameFinder.extractName(name),
				new CallArgumentsList(start, end));
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return E_CALL;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.ast.statements.Statement#traverse(org.eclipse.dltk.ast
	 * .ASTVisitor)
	 */
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			super.traverse(visitor);
			visitor.endvisit(this);
		}
	}
}
