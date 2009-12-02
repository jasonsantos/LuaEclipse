package com.anwrt.ldt.internal.parser;

import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;

import com.anwrt.ldt.parser.ast.expressions.Identifier;
import com.anwrt.ldt.parser.ast.expressions.Index;

/**
 * In an AST from Lua code sometimes you need a name or a reference for a node,
 * but most of the time the name is hidden in the child nodes. This class seek
 * for name in AST.
 * 
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date$
 * $Author$
 * $Id$
 */
public class NameFinder {

    /**
     * @see NameFinder#getReference(Expression)
     * @param node
     * @return {@link String} name from reference
     */
    public static String getName(Expression node) {
	return getReference(node).getName();

    }

    /**
     * Extract an identifier name from {@link Expression} when possible
     * 
     * @param Expression
     *            var {@link Expression} for name extraction
     * @return {@link SimpleReference} in code, or "..." when not available
     */
    public static SimpleReference getReference(Expression node) {
	
	// Set default name
	String name = "...";
	int start = node.matchStart();
	int end = node.matchStart() + node.matchLength();

	// Search name in Identifier
	if (node instanceof Identifier) {
	    Identifier id = (Identifier) node;
	    name = id.getValue();
	} else if (node instanceof Index) {

	    /*
	     * Compose name from index
	     */
	    Index index = (Index) node;
	    if (index.getKey() instanceof Identifier
		    && index.getValue() instanceof com.anwrt.ldt.parser.ast.expressions.String) {
		// Table name
		name = ((Identifier) index.getKey()).getValue() + ".";
		// Function name
		name += ((com.anwrt.ldt.parser.ast.expressions.String) index
			.getValue()).getValue();
	    }
	} else if (node instanceof com.anwrt.ldt.parser.ast.expressions.String) {
	    name = ((com.anwrt.ldt.parser.ast.expressions.String) node)
		    .getValue();
	}
	return new SimpleReference(start, end, name);
    }
}
