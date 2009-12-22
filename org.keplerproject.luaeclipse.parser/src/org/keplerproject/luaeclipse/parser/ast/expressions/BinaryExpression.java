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
 * $Id: BinaryExpression.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.utils.CorePrinter;
import org.keplerproject.luaeclipse.internal.parser.Index;


// TODO: Auto-generated Javadoc
/**
 * The Class BinaryExpression.
 */
public class BinaryExpression extends Expression implements Index {

	/** Left parent of the expression. */
	private Expression left;

	/** Right parent of the expression. */
	private Expression right;

	/** Kind of expression's operator. */
	protected int kind;

	protected long id;

	/**
	 * Defines a two operands expression.
	 * 
	 * @param left
	 *            Left parent of the expression
	 * @param kind
	 *            Token of the operator
	 * @param right
	 *            Right parent of the expression
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * 
	 * @see org.eclipse.dltk.ast.expressions.ExpressionConstants
	 */
	public BinaryExpression(int start, int end, Expression left, int kind,
			Expression right) {
		super(start, end);
		if (left != null) {
			this.setStart(left.sourceStart());
			assert left instanceof Expression;
		}

		if (right != null) {
			this.setEnd(right.sourceEnd());
			assert right instanceof Expression;
		}

		this.kind = kind;
		this.left = left;
		this.right = right;
	}

	/**
	 * Instantiates a new binary expression.
	 * 
	 * @param left
	 *            the left
	 * @param kind
	 *            the kind
	 * @param right
	 *            the right
	 */
	public BinaryExpression(Expression left, int kind, Expression right) {
		this(0, 0, left, kind, right);
	}

	/**
	 * Left parent of the expression.
	 * 
	 * @return Left parent of the expression
	 */
	public Expression getLeft() {
		return left;
	}

	public java.lang.String getOperator() {
		switch (getKind()) {
		case E_CONCAT:
			return "..";
		default:
		}
		return super.getOperator();
	}

	/**
	 * Gets the right.
	 * 
	 * @return Left parent of the expression
	 */
	public Expression getRight() {
		return right;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return this.kind;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.ast.expressions.Expression#printNode(org.eclipse.dltk
	 * .utils.CorePrinter)
	 */
	public void printNode(CorePrinter output) {
		if (this.left != null) {
			this.left.printNode(output);
		}

		output.formatPrintLn(this.getOperator());

		if (this.right != null) {
			this.right.printNode(output);
		}
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
			if (left != null) {
				left.traverse(visitor);
			}

			if (right != null) {
				right.traverse(visitor);
			}
			visitor.endvisit(this);
		}
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}
}
