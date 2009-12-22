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
import org.eclipse.dltk.ast.declarations.TypeDeclaration;

public class TableDeclaration extends TypeDeclaration {

    public TableDeclaration(String name, int nameStart, int nameEnd, int start,
	    int end) {
	super(name, nameStart, nameEnd, start, end);
	this.setModifiers(Declaration.AccPublic);
    }
}
