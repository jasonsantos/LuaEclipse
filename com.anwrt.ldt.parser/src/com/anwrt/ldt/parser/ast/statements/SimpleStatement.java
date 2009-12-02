/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: SimpleStatement.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;

// TODO: Auto-generated Javadoc
/**
 * The Class SimpleStatement.
 */
public abstract class SimpleStatement extends Statement {

	/** The expression. */
	protected Expression fExpression;

	/**
	 * Instantiates a new simple statement.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param expression
	 *            the expression
	 */
	protected SimpleStatement(int start, int end, Expression expression) {
		super(start, end);
		this.fExpression = expression;
	}

	/**
	 * Gets the expression.
	 * 
	 * @return the expression
	 */
	public Expression getExpression() {
		return fExpression;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.ast.statements.Statement#traverse(org.eclipse.dltk.ast
	 * .ASTVisitor)
	 */
	public void traverse(ASTVisitor pVisitor) throws Exception {
		if (pVisitor.visit(this)) {
			if (fExpression != null) {
				fExpression.traverse(pVisitor);
			}
			pVisitor.endvisit(this);
		}
	}
}