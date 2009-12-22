package org.keplerproject.luaeclipse.editor.internal.text;

import org.eclipse.jface.text.IDocument;

import com.anwrt.ldt.parser.LuaConstants;

public interface ILuaPartitions {
	public static final String LUA_PARTITIONING = LuaConstants.LUA_PARTITIONING;

	public static final String LUA_COMMENT = "__lua_comment"; //$NON-NLS-1$
	public static final String LUA_MULTI_LINE_COMMENT = "__lua_multi_line_comment"; //$NON-NLS-1$
	public static final String LUA_STRING = "__lua_string"; //$NON-NLS-1$
	public static final String LUA_SINGLE_QUOTE_STRING = "__lua_single_quote_string"; //$NON-NLS-1$

	public final static String[] LUA_PARTITION_TYPES = new String[] {
			IDocument.DEFAULT_CONTENT_TYPE, ILuaPartitions.LUA_COMMENT,
			ILuaPartitions.LUA_STRING, ILuaPartitions.LUA_SINGLE_QUOTE_STRING,
			ILuaPartitions.LUA_MULTI_LINE_COMMENT };
}
