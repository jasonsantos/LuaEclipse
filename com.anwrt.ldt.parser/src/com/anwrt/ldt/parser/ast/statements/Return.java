/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Return.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.statements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;

// TODO: Auto-generated Javadoc
/**
 * The Class Return.
 */
public class Return extends Statement {

	/** The expressions. */
	private List<Expression> expressions;

	/**
	 * Instantiates a new return.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param expressions
	 *            the expressions
	 */
	public Return(int start, int end, List<Expression> expressions) {
		super(start, end);
		this.expressions = expressions;
	}

	/**
	 * Instantiates a new return.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public Return(int start, int end) {
		this(start, end, new ArrayList<Expression>());
	}

	/**
	 * Adds the expression.
	 * 
	 * @param expression
	 *            the expression
	 * 
	 * @return true, if successful
	 */
	public boolean addExpression(Expression expression) {
		return this.expressions.add(expression);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return LuaStatementConstants.S_RETURN;
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
			for (Expression expression : expressions) {
				expression.traverse(visitor);
			}
			visitor.endvisit(this);
		}
	}
}
