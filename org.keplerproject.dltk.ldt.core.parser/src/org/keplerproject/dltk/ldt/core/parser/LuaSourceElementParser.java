package org.keplerproject.dltk.ldt.core.parser;

import org.eclipse.dltk.core.AbstractSourceElementParser;
import org.keplerproject.dltk.ldt.core.LuaNature;

public class LuaSourceElementParser extends AbstractSourceElementParser {
	@Override
	protected String getNatureId() {
		return LuaNature.LUA_NATURE;
	}
}
