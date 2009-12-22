/******************************************************************************
 * Copyright (c) 2009 KeplerProject, Sierra Wireless.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 *          - initial API and implementation and initial documentation
 *****************************************************************************/


package org.keplerproject.luaeclipse.internal.parser;

import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.expressions.Literal;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.keplerproject.luaeclipse.parser.ast.expressions.BinaryExpression;
import org.keplerproject.luaeclipse.parser.ast.expressions.Call;
import org.keplerproject.luaeclipse.parser.ast.expressions.Index;
import org.keplerproject.luaeclipse.parser.ast.expressions.String;


/**
 * In an AST from Lua code sometimes you need a name or a reference for a node,
 * but most of the time the name is hidden in the child nodes. This class seek
 * for name in AST.
 * 
 * @author Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date$ $Author$ $Id$
 */
public class NameFinder {

	/**
	 * Extract an identifier name from {@link Expression} when possible
	 * 
	 * @param Expression
	 *            var {@link Expression} for name extraction
	 * @return {@link SimpleReference} in code, or "..." when not available
	 */
	public static SimpleReference getReference(Expression node) {
		// Set default name
		int start = node.matchStart();
		int end = node.matchStart() + node.matchLength();
		return new SimpleReference(start, end, extractName(node));
	}

	/**
	 * @see NameFinder#getReference(Expression)
	 * @param node
	 * @return {@link String} name from reference
	 */
	public static java.lang.String extractName(Expression expr) {
		//
		// Some function declarations look like : function table:method() end
		// Those ones use an index as name.
		// `Set{ { `Index{ `Id "table", `String "method" } },
		// { `Function{ { `Id "self" }, { } } } }
		// So let's use a composed name, as instance: table.method()
		//
		java.lang.String name;
		if (expr instanceof Index) {
			// Deal with key of index
			Index index = (Index) expr;
			name = extractName(index.getKey());

			/*
			 * Choose between "[*]" and ".*" depending on type of key value
			 */
			Expression value = index.getValue();
			if (value instanceof Call || value instanceof BinaryExpression) {
				name += '[' + extractName(value) + ']';
			} else {
				name += '.' + extractName(value);
			}
		} else if (expr instanceof BinaryExpression) {
			name = extractNameFromBinary((BinaryExpression) expr);
		} else if (expr instanceof Literal) {
			/*
			 * When call is designed by an identifier, just name the function
			 * after it.
			 */
			name = ((Literal) expr).getValue();
		} else if (expr instanceof Call) {
			// Cast argument
			Call call = (Call) expr;

			// Built argument list
			CallArgumentsList args = call.getArgs();
			java.lang.String argsList = new java.lang.String();
			for (Object arg : args.getChilds()) {
				assert arg instanceof Expression : "Statement found in argument list.";
				argsList += ", " + extractName((Expression) arg);
			}

			// Compose name
			if (!argsList.isEmpty()) {
				argsList = argsList.substring(2);
			}
			name = call.getName() + '(' + argsList + ')';
		} else {
			/*
			 * When expression does not match any of previous types, just go
			 * with "...".
			 */
			name = "...";
		}
		return name;
	}

	private static java.lang.String extractNameFromBinary(BinaryExpression bin) {

		int left = 0, right = 1;
		java.lang.String[] operand = { "left", "right" };
		Expression[] expressions = { bin.getLeft(), bin.getRight() };
		int k = 0;
		for (Expression expr : expressions) {
			if (expr instanceof String) {
				operand[k] = '"' + ((String) expr).getValue() + '"';
			} else if (expr instanceof Literal) {
				operand[k] = ((Literal) expr).getValue();
			} else if (expr instanceof BinaryExpression) {
				operand[k] = extractNameFromBinary((BinaryExpression) expr);
			} else {
				operand[k] = extractName(expr);
			}
			k++;
		}
		return operand[left] + bin.getOperator() + operand[right];
	}
}
