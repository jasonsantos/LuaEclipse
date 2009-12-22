/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: UnaryExpression.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.keplerproject.luaeclipse.internal.parser.Index;

// TODO: Auto-generated Javadoc
/**
 * The Class UnaryExpression.
 */
public class UnaryExpression extends Expression implements Index {

	/** The kind. */
	private int kind;
	
	/** The expression. */
	private Statement expression;

	private long id;

	/**
	 * Instantiates a new unary expression.
	 * 
	 * @param start the start
	 * @param end the end
	 * @param kind the kind
	 * @param e the e
	 */
	public UnaryExpression(int start, int end, int kind, Expression e) {
		super(start, end);
		this.expression = e;
		this.kind = kind;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return kind;
	}
	
	/**
	 * Gets the expression.
	 * 
	 * @return the expression
	 */
	public Statement getExpression() {
		return expression;
	}


	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}
	/**
	 * Traverse.
	 * 
	 * @param pVisitor the visitor
	 * 
	 * @throws Exception the exception
	 */
	@Override
	public void traverse(ASTVisitor pVisitor) throws Exception {
		if (pVisitor.visit(this)) {
			if (expression != null) {
				expression.traverse(pVisitor);
			}
			pVisitor.endvisit(this);
		}
	}
}
