package com.anwrt.ldt.internal.parser;

import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;

import com.anwrt.ldt.parser.ast.expressions.Identifier;
import com.anwrt.ldt.parser.ast.expressions.Index;
import com.anwrt.ldt.parser.ast.expressions.String;

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
		/*
		 * Some function declarations look like : function table:method() end
		 * Those ones use an index as name.
		 *		`Set{ { `Index{ `Id "table", `String "method" } }, 
       	 * 			{ `Function{ { `Id "self" }, { } } } }
       	 * So let's use a composed name, as instance: table.method()
		 */
		java.lang.String name;
		if (expr instanceof Index) {
			// First part of name, before the dot
			Index index = (Index) expr;
			if ( index.getKey() instanceof Identifier ){
				name = ((Identifier)index.getKey()).getValue();
			}else{
				name = index.getKey().toString();
			}

			// After the dot
			name +='.';
			if ( index.getValue() instanceof String ){
				name += ((String)index.getValue()).getValue();
			}else{
				name += index.getKey().toString();
			}
		} else if (expr instanceof Identifier) {
			/*
			 * When call is designed by an identifier, just name the function
			 * after it.
			 */
			name = ((Identifier) expr).getValue();
		} else if (expr instanceof com.anwrt.ldt.parser.ast.expressions.String) {
			name = ((com.anwrt.ldt.parser.ast.expressions.String) expr).getValue();
		}else{
			/*
			 * When expression does not match any of previous types, just go
			 * with "...". 
			 */
			name = "...";
		}
		return name;
	}
}
