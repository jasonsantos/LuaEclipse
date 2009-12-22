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


/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Function.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import java.util.ArrayList;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.Argument;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.utils.CorePrinter;
import org.keplerproject.luaeclipse.internal.parser.Index;
import org.keplerproject.luaeclipse.internal.parser.NameFinder;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;
import org.keplerproject.luaeclipse.parser.ast.statements.Chunk;


// TODO: Auto-generated Javadoc
/**
 * The Class Function.
 */
public class Function extends Block implements Index {

    /** The parameters. */
    private ArrayList<Argument> arguments;
    private long id;

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
	for (int k = 0; k < argCount; k++) {
	    Expression expr = (Expression) parameters.getStatements().get(k);
	    SimpleReference ref = NameFinder.getReference(expr);
	    Argument arg = new Argument(ref, ref.matchStart(), ref.matchStart()
		    + ref.matchStart(), expr, Declaration.AccFinal);
	    arg.setModifiers(Declaration.D_ARGUMENT);
	    this.arguments.add(arg);
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

    public long getID() {
	return id;
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

    public void setID(long id) {
	this.id = id;
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
