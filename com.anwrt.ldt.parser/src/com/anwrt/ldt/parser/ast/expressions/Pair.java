/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Pair.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Statement;

import com.anwrt.ldt.internal.parser.NameFinder;
import com.anwrt.ldt.parser.LuaExpressionConstants;
import com.anwrt.ldt.parser.ast.statements.FunctionDeclaration;

// TODO: Auto-generated Javadoc
/**
 * The Class Pair.
 */
public class Pair extends BinaryExpression {

	/**
	 * Instantiates a new pair.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 */
	public Pair(int start, int end, Statement left, Statement right) {
		super(start, end, left, LuaExpressionConstants.E_PAIR, right);
		if (right instanceof Function) {
			SimpleReference ref = NameFinder.getReference((Expression) left);
			FunctionDeclaration declaration = new FunctionDeclaration(ref
					.getName(), ref.matchStart() - 1, ref.matchStart()
					+ ref.matchLength(), right.matchStart(), right.matchStart()
					+ ref.matchLength());
			((Function) right).addStatement(declaration);
		}
	}
}
