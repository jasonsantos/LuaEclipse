/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Set.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package com.anwrt.ldt.parser.ast.statements;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.FieldDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.expressions.ExpressionConstants;

import com.anwrt.ldt.internal.parser.NameFinder;
import com.anwrt.ldt.parser.ast.expressions.BinaryExpression;
import com.anwrt.ldt.parser.ast.expressions.Function;
import com.anwrt.ldt.parser.ast.expressions.Index;
import com.anwrt.ldt.parser.ast.expressions.Table;

// TODO: Auto-generated Javadoc
/**
 * The Class Set.
 */
public class Set extends BinaryExpression {
	/**
	 * Construct default strict assignment.
	 * 
	 * @param left
	 *            the left
	 * @param right
	 *            the right
	 */
	private Set(int start, int end, Chunk left, Chunk right) {
		super(start, end, left, E_ASSIGN, right);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.anwrt.ldt.parser.ast.expressions.BinaryExpression#getKind()
	 */
	@Override
	public int getKind() {
		return ExpressionConstants.E_ASSIGN;
	}

	public static Set factory(int start, int end, Chunk left, Chunk right) {
		int leftSize = left.getStatements().size();
		int rightSize = right.getStatements().size();

		/*
		 * Create empty chunk for right side, it will contain statement and
		 * declarations when needed.
		 */
		int availableValues = leftSize > rightSize ? rightSize : leftSize;
		Chunk castedRight = new Chunk(left.matchStart(), left.matchLength()
				+ left.matchStart());
		for (int n = 0; n < availableValues; n++) {

			// Process needed only for identifiers and indexes
			Expression declaredVar = (Expression) left.getStatements().get(n);
			Expression assignedValue = (Expression) right.getStatements()
					.get(n);

			// Get variable name
			String varName = NameFinder.getName(declaredVar);

			// Declare Identifier as Arguments
			int idStart = declaredVar.matchStart() - 1;
			int idEnd = declaredVar.matchStart() + declaredVar.matchLength();

			/*
			 * Deal with table declarations, they will be represented as Classes
			 * in outline.
			 */
			if (assignedValue instanceof Table) {
				/*
				 * Make table declaration as type
				 */
				TableDeclaration table = new TableDeclaration(varName, idStart,
						idEnd, start, end);

				// Append current statement to declaration
				Chunk body = new Chunk(assignedValue.matchStart(),
						assignedValue.matchStart()
								+ assignedValue.matchLength());
				body.addStatement(assignedValue);
				table.setBody(body);

				// Insert declaration in AST
				castedRight.addStatement(table);
			} else if (assignedValue instanceof Function) {
				/*
				 * Deal with function declarations
				 */
				FunctionDeclaration function = new FunctionDeclaration(varName,
						idStart, idEnd, start, end);
				// Append function body to function declaration
				function.acceptBody((Function) assignedValue);

				// Associate function's arguments to function declaration
				function.acceptArguments(((Function) assignedValue)
						.getArguments());

				// Insert declaration in AST
				castedRight.addStatement(function);
			} else if (declaredVar instanceof Index) {
				/*
				 * Deal with fields, they are index of a table that aren't
				 * functions.
				 */
				FieldDeclaration field = new FieldDeclaration(varName, idStart,
						idEnd, declaredVar.matchStart(), declaredVar
								.matchStart()
								+ declaredVar.matchLength());
				field.setModifier(Declaration.AccGlobal);
				castedRight.addStatement(field);
				castedRight.addStatement(assignedValue);
				System.out.println("attribute : " + varName);
			} else {
				// Add casted if needed statement to chunk
				castedRight.addStatement(assignedValue);
			}
		}

		// Instantiate "Set" nodes with casted chunks
		return new Set(start, end, left, castedRight);
	}

	/**
	 * Convert to string in pattern: "left = right".
	 * 
	 * @return the string
	 */
	public String toString() {
		return getLeft().toString() + '=' + getRight().toString();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.anwrt.ldt.parser.ast.expressions.BinaryExpression#traverse(org.eclipse
	 * .dltk.ast.ASTVisitor)
	 */
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			super.traverse(visitor);
			visitor.endvisit(this);
		}
	}
}
