/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-18 16:46:07 +0200 (jeu., 18 juin 2009) $
 * $Author: kkinfoo $
 * $Id: Table.java 1887 2009-06-18 14:46:07Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;

import com.anwrt.ldt.parser.LuaExpressionConstants;
// TODO: Auto-generated Javadoc
/**
 * The Class Table.
 */
public class Table extends Expression{
	
	/** The statements. */
	List<Statement> statements;

	/**
	 * Instantiates a new table.
	 * 
	 * @param start the start
	 * @param end the end
	 * @param statements the statements
	 */
	public Table(int start, int end, List<Statement> statements) {
		super(start, end);
		this.statements = statements;
	}

	/**
	 * Instantiates a new table.
	 * 
	 * @param start the start
	 * @param end the end
	 */
	public Table(int start, int end) {
		this(start, end, new ArrayList<Statement>() );
	}
	
	/**
	 * Adds the statement.
	 * 
	 * @param statement the statement
	 */
	public void addStatement(Statement statement){
		statements.add(statement);
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.ast.statements.Statement#getKind()
	 */
	@Override
	public int getKind() {
		return LuaExpressionConstants.E_TABLE;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.ast.statements.Statement#traverse(org.eclipse.dltk.ast.ASTVisitor)
	 */
	public void traverse(ASTVisitor pVisitor) throws Exception {
		if (pVisitor.visit(this)) {
			for (Statement node : statements) {
				node.traverse(pVisitor);
			}
			pVisitor.endvisit(this);
		}
	}
}
