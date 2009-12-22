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


package org.keplerproject.luaeclipse.parser;

import org.eclipse.dltk.ast.Modifiers;

public class LuaConstants {
    protected LuaConstants() {
    }
    public final static String LUA_PARTITIONING = "__lua_partitioning"; //$NON-NLS-1$
    public static final int LuaAttributeModifier = 2 << (Modifiers.USER_MODIFIER + 1);
    public static final int LuaAliasModifier = 2 << (Modifiers.USER_MODIFIER + 2);
    public static final String REQUIRE = "require"; //$NON-NLS-1$
}
