package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.IDocument;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentTypeExtension;

public class LuaContentTypeContributor implements ILuaContentTypeExtension {

	public String[] getContentTypes() {

		return new String[] { IDocument.DEFAULT_CONTENT_TYPE,
				//ILuaPartitions.LUA_SINGLE_LINE_COMMENT,
				//ILuaPartitions.LUA_STRING ,				
				ILuaPartitions.LUA_MULTI_LINE_COMMENT,				
				ILuaPartitions.LUA_CODE};
	}

}
