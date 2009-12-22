/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-22 14:42:54 +0200 (mer., 22 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Chunk.java 2151 2009-07-22 12:42:54Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.statements;

import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;
import org.keplerproject.luaeclipse.internal.parser.Index;


// TODO: Auto-generated Javadoc
/**
 * The Class Chunk.
 */
public class Chunk extends Block implements Index {

	private long id;

	/**
	 * Instantiates a new chunk.
	 */
	public Chunk() {
		super();
	}

	/**
	 * Instantiates a new chunk.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param statems
	 *            the statems
	 */
	public Chunk(int start, int end, List<Statement> statems) {
		super(start, end, statems);
	}

	/**
	 * Instantiates a new chunk.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 */
	public Chunk(int start, int end) {
		super(start, end);
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	@Override
	public void traverse(ASTVisitor visitor) throws Exception {
		super.traverse(visitor);
	}
}
