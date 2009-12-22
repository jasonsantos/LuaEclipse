/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: If.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;
import org.keplerproject.luaeclipse.internal.parser.Index;


// TODO: Auto-generated Javadoc
/**
 * The Class If.
 */
public class If extends Statement implements LuaStatementConstants, Index {

	/** The condition. */
	private Expression condition;

	/** The nominal. */
	private Chunk nominal;

	/** The alternative. */
	private Chunk alternative;

	private long id;

	/**
	 * Instantiates a new if.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param condition
	 *            the condition
	 * @param nominal
	 *            the nominal
	 * @param alternative
	 *            the alternative
	 */
	public If(int start, int end, Expression condition, Chunk nominal,
			Chunk alternative) {
		super(start, end);
		this.condition = condition;
		this.nominal = nominal;
		this.alternative = alternative;
	}

	public void setAlternative(Chunk alternative) {
		this.alternative = alternative;
	}

	/**
	 * Instantiates a new if.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param condition
	 *            the condition
	 * @param nominal
	 *            the nominal
	 */
	public If(int start, int end, Expression condition, Chunk nominal) {
		this(start, end, condition, nominal, null);
	}

	/**
	 * Gets the condition.
	 * 
	 * @return the condition
	 */
	public Expression getCondition() {
		return condition;
	}

	/**
	 * Gets the nominal.
	 * 
	 * @return the nominal
	 */
	public Chunk getNominal() {
		return nominal;
	}

	/**
	 * Gets the alternative.
	 * 
	 * @return the alternative
	 */
	public Chunk getAlternative() {
		return alternative;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return S_IF;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
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
			condition.traverse(visitor);
			nominal.traverse(visitor);
			if (alternative != null) {
				alternative.traverse(visitor);
			}
			visitor.endvisit(this);
		}
	}

}
