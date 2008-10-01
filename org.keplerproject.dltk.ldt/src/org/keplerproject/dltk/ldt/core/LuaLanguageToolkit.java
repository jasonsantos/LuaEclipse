package org.keplerproject.dltk.ldt.core;

import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;

public class LuaLanguageToolkit extends AbstractLanguageToolkit {

	private static LuaLanguageToolkit toolkit;

	public static IDLTKLanguageToolkit getDefault() {
		toolkit = (toolkit == null) ? new LuaLanguageToolkit() : toolkit;
		return toolkit;
	}

	public String getLanguageContentType() {
		return LuaContentTypeDescriber.CONTENT_TYPE;
	}

	public String getLanguageName() {
		return "Lua";
	}

	public String getNatureId() {
		return LuaNature.LUA_NATURE;
	}
}
