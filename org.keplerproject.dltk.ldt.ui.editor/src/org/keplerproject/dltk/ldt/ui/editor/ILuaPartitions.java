package org.keplerproject.dltk.ldt.ui.editor;

import org.eclipse.jface.text.IDocument;

public interface ILuaPartitions {
	public final static String LUA_PARTITIONING = "__lua_partitioning";
	public final static String LUA_COMMENT = "__lua_comment";
	public final static String LUA_STRING = "__lua_string";
	
	public final static String[] LUA_PARTITION_TYPES = new String[] {
		LUA_COMMENT, LUA_STRING, IDocument.DEFAULT_CONTENT_TYPE
	};
}
