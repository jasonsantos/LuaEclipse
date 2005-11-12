package org.keplerproject.ldt.ui.baseExts.scanner;

import org.keplerproject.ldt.ui.editors.ext.ILuaContentTypeExtension;

public class LuaContentTypeContributor implements ILuaContentTypeExtension {

	public String[] getContentTypes() {

		return new String[] { ILuaPartitions.LUA_CODE,
				ILuaPartitions.LUA_MULTI_LINE_COMMENT,
				ILuaPartitions.LUA_SINGLE_LINE_COMMENT,
				ILuaPartitions.LUA_STRING };
	}

}
