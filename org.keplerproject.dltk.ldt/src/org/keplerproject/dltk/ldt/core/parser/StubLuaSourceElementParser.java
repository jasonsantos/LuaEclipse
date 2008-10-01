package org.keplerproject.dltk.ldt.core.parser;

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.ISourceElementRequestor.TypeInfo;
import org.eclipse.dltk.core.AbstractSourceElementParser;
import org.eclipse.dltk.core.ISourceModuleInfoCache.ISourceModuleInfo;
import org.keplerproject.dltk.ldt.core.LuaNature;

public class StubLuaSourceElementParser extends AbstractSourceElementParser {
	@Override
	public void parseSourceModule(char[] contents, ISourceModuleInfo astCache,
			char[] filename) {
		ISourceElementRequestor requestor = getRequestor();
		requestor.enterModule();
		TypeInfo info = new TypeInfo();
		info.name = "Example Type";
		requestor.enterType(info);
		requestor.exitType(0);
		requestor.exitModule(0);
	}
	
	@Override
	protected String getNatureId() {
		return LuaNature.LUA_NATURE;
	}

}
