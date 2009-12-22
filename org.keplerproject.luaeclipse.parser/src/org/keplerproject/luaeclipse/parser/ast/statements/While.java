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
 * $Id: While.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Block;
import org.keplerproject.luaeclipse.internal.parser.Index;


// TODO: Auto-generated Javadoc
/**
 * The Class While.
 */
public class While extends Block implements LuaStatementConstants, Index {

	/** The expression. */
	private Expression expression;
	private long id;

	/**
	 * Instantiates a new while.
	 * 
	 * @param start the start
	 * @param end the end
	 * @param expr the expr
	 * @param block the block
	 */
	public While(int start, int end, Expression expr, Chunk block) {
		super(start, end, block.getStatements());
		this.expression = expr;
	}

	/**
	 * Gets the expression.
	 * 
	 * @return the expression
	 */
	public Expression getExpression() {
		return expression;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.ast.statements.Block#getKind()
	 */
	@Override
	public int getKind() {
		return S_WHILE;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}
	/* (non-Javadoc)
	 * @see org.eclipse.dltk.ast.statements.Block#traverse(org.eclipse.dltk.ast.ASTVisitor)
	 */
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			super.traverse(visitor);
			expression.traverse(visitor);
			visitor.endvisit(this);
		}
	}
}
