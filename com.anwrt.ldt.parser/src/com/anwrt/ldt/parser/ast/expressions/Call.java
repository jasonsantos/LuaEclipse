/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Call.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.CallExpression;

import com.anwrt.ldt.internal.parser.Index;
import com.anwrt.ldt.parser.LuaExpressionConstants;

;

// TODO: Auto-generated Javadoc
/**
 * The Class Call.
 */
public class Call extends CallExpression/* Expression */implements
		LuaExpressionConstants, Index {

	private long id;

	private static ASTNode receiver(int start, int end) {
		ASTNode node = new ASTNode() {

			@Override
			public void traverse(ASTVisitor visitor) throws Exception {
				if (visitor.visit(this)) {
					visitor.endvisit(this);
				}
			}
		};
		node.setStart(start);
		node.setEnd(end);
		return node;
	}

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
	public Call(int start, int end, Identifier name, CallArgumentsList args) {
		super(start, end, receiver(start, end), name.getValue(), args);
	}

	public Call(int start, int end, Identifier name) {
		super(start, end, receiver(start, end), name.getValue(),
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

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

}
