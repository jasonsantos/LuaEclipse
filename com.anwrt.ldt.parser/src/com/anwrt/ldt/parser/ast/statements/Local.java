/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Local.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.statements.Statement;
import org.eclipse.dltk.ast.statements.StatementConstants;

// TODO: Auto-generated Javadoc
/**
 * The Class Local.
 */
public class Local extends Statement implements StatementConstants {

	/** The identifiers. */
	private Chunk identifiers;

	/** The expressions. */
	private Chunk expressions;

	/**
	 * Instantiates a new local.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param identifiers
	 *            the identifiers
	 * @param expressions
	 *            the expressions
	 */
	public Local(int start, int end, Chunk identifiers, Chunk expressions) {
		super(start, end);
		this.expressions = expressions;
		this.identifiers = identifiers;
	}

	/**
	 * Instantiates a new local.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param identifiers
	 *            the identifiers
	 */
	public Local(int start, int end, Chunk identifiers) {
		this(start, end, identifiers, null);
	}

	/**
	 * Gets the identifiers.
	 * 
	 * @return the identifiers
	 */
	public Chunk getIdentifiers() {
		return identifiers;
	}

	/**
	 * Gets the expressions.
	 * 
	 * @return the expressions
	 */
	public Chunk getExpressions() {
		return expressions;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return D_VAR_DECL;
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
			if (expressions != null) {
				expressions.traverse(visitor);
			}
			identifiers.traverse(visitor);
			visitor.endvisit(this);
		}
	}
}
