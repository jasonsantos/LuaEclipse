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

import org.eclipse.dltk.ast.expressions.NilLiteral;
import org.keplerproject.luaeclipse.internal.parser.Index;
import org.keplerproject.luaeclipse.parser.LuaExpressionConstants;


public class Nil extends NilLiteral  implements LuaExpressionConstants, Index{

	private long id;

	public Nil(int start, int end) {
		super(start, end);
	}
	
	@Override
	public int getKind() {
		return NIL_LITTERAL;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

}
