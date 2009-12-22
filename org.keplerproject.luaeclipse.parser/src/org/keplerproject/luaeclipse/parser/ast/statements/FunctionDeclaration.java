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


package org.keplerproject.luaeclipse.parser.ast.statements;

import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;

public class FunctionDeclaration extends MethodDeclaration {

    public FunctionDeclaration(String name, int nameStart, int nameEnd, int start,
	    int end) {
	super(name, nameStart, nameEnd, start, end);
	this.setModifier(Declaration.D_METHOD);
	this.setModifiers(Declaration.AccPublic);
    }
}
