/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Function.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.expressions;

import java.util.ArrayList;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.utils.CorePrinter;

import com.anwrt.ldt.internal.parser.NameFinder;
import com.anwrt.ldt.parser.LuaExpressionConstants;
import com.anwrt.ldt.parser.ast.statements.Chunk;

// TODO: Auto-generated Javadoc
/**
 * The Class Function.
 */
public class Function extends Block {

	/** The parameters. */
	private ArrayList<Argument> arguments;

	/**
	 * Instantiates a new function.
	 * 
	 * @param start
	 *            the start
	 * @param end
	 *            the end
	 * @param parameters
	 *            the parameters
	 * @param body
	 *            the body
	 */
	public Function(int start, int end, Chunk parameters, Chunk body) {
		super(start, end, body.getStatements());
		// Declare parameters as arguments
		int argCount = parameters.getStatements().size();
		this.arguments = new ArrayList<Argument>(argCount);
		for (int arg = 0; arg < argCount; arg++) {
			Expression expr = (Expression) parameters.getStatements().get(arg);
			SimpleReference ref = NameFinder.getReference(expr);
			Argument param = new Argument(ref, ref.matchStart(), ref.matchStart()
					+ ref.matchStart(), expr, Declaration.AccFinal);
			param.setModifiers(Declaration.D_ARGUMENT);
			this.arguments.add(param);
		}
	}

	public ArrayList<Argument> getArguments() {
		return arguments;
	}

	/**
	 * Gets the parameters.
	 * 
	 * @return the parameters
	 * 
	 *         public Chunk getParameters() { return parameters; }
	 */

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ast.statements.Block#getKind()
	 */
	@Override
	public int getKind() {
		return LuaExpressionConstants.E_FUNCTION;
	}

	public void printNode(CorePrinter output) {

		// Arguments
		output.indent();
		output.indent();
		for (Argument arg : getArguments()) {
			arg.printNode(output);
		}
		output.dedent();
		output.dedent();
		super.printNode(output);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @seeorg.eclipse.dltk.ast.statements.Block#traverse(org.eclipse.dltk.ast.
	 * ASTVisitor)
	 */
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			super.traverse(visitor);
			for (Argument arg : this.arguments) {
				arg.traverse(visitor);
			}
			visitor.endvisit(this);
		}
	}
}
