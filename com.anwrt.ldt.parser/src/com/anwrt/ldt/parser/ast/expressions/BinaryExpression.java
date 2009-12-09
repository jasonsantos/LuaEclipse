/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: BinaryExpression.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.utils.CorePrinter;

import com.anwrt.ldt.internal.parser.Index;

// TODO: Auto-generated Javadoc
/**
 * The Class BinaryExpression.
 */
public class BinaryExpression extends Expression implements Index {

	/** Left parent of the expression. */
	private Statement left;

	/** Right parent of the expression. */
	private Statement right;

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
	public BinaryExpression(int start, int end, Statement left, int kind,
			Statement right) {
		super(start, end);
		if (left != null) {
			this.setStart(left.sourceStart());
		}

		if (right != null) {
			this.setEnd(right.sourceEnd());
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
	public BinaryExpression(Statement left, int kind, Statement right) {
		this(0, 0, left, kind, right);
	}

	/**
	 * Left parent of the expression.
	 * 
	 * @return Left parent of the expression
	 */
	public Statement getLeft() {
		return left;
	}

	/**
	 * Gets the right.
	 * 
	 * @return Left parent of the expression
	 */
	public Statement getRight() {
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
