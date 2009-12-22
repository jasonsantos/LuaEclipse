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


package org.keplerproject.luaeclipse.parser.ast.expressions;

import org.eclipse.dltk.ast.expressions.BooleanLiteral;
import org.keplerproject.luaeclipse.internal.parser.Index;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;


public class Boolean extends BooleanLiteral implements Index,
		LuaExpressionConstants {

	private long id;

	public Boolean(int start, int end, boolean b) {
		super(start, end, b);
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public int getKind() {
		return boolValue() ? BOOL_TRUE : BOOL_FALSE;
	}

}
