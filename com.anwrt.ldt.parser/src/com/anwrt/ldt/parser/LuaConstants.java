package com.anwrt.ldt.parser;

import org.eclipse.dltk.ast.Modifiers;

public class LuaConstants {
    protected LuaConstants() {
    }
    public final static String LUA_PARTITIONING = "__lua_partitioning"; //$NON-NLS-1$
    public static final int LuaAttributeModifier = 2 << (Modifiers.USER_MODIFIER + 1);
    public static final int LuaAliasModifier = 2 << (Modifiers.USER_MODIFIER + 2);
    public static final String REQUIRE = "require"; //$NON-NLS-1$
}
