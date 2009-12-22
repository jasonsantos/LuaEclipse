/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Index.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;


// TODO: Auto-generated Javadoc
/**
 * The Class Index.
 */
public class Index extends Expression implements LeftHandSide,
	LuaExpressionConstants, org.keplerproject.luaeclipse.internal.parser.Index {

    /** The index. */
    private Expression value;

    /** The table. */
    private Expression key;

    private long id;

    /**
     * Instantiates a new index.
     * 
     * @param start
     *            the start
     * @param end
     *            the end
     * @param key
     *            the table
     * @param value
     *            the index
     */
    public Index(int start, int end, Expression key, Expression value) {
	super(start, end);
	this.value = value;
	this.key = key;
    }

    /**
     * Gets the index.
     * 
     * @return the index
     */
    public Expression getValue() {
	return value;
    }

    /*
     * (non-Javadoc)
     * 
     * @see org.eclipse.dltk.ast.statements.Statement#getKind()
     */
    @Override
    public int getKind() {
	return E_INDEX;
    }

    public long getID() {
	return id;
    }

    public void setID(long id) {
	this.id = id;
    }

    /**
     * Gets the table.
     * 
     * @return the table
     */
    public Expression getKey() {
	return key;
    }

    /*
     * (non-Javadoc)
     * 
     * @see com.anwrt.ldt.parser.ast.expressions.LeftHandSide#isLeftHandSide()
     */
    @Override
    public boolean isLeftHandSide() {
	return true;
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
	    value.traverse(visitor);
	    key.traverse(visitor);
	    visitor.endvisit(this);
	}
    }
}
