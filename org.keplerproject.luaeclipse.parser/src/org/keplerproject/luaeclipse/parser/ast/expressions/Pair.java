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
 * $Id: Pair.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.SimpleReference;
import org.keplerproject.luaeclipse.internal.parser.NameFinder;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;
import org.keplerproject.luaeclipse.parser.ast.statements.FunctionDeclaration;


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
    public Pair(int start, int end, Expression left, Expression right) {
	super(start, end, left, LuaExpressionConstants.E_PAIR, right);
	if (right instanceof Function) {
	    SimpleReference ref = NameFinder.getReference((Expression) left);
	    FunctionDeclaration declaration = new FunctionDeclaration(ref
		    .getName(), ref.matchStart()-1, ref.matchStart()
		    + ref.matchLength(), right.matchStart(), right.matchStart()
		    + ref.matchLength());
	    ((Function) right).addStatement(declaration);
	}
    }
}
