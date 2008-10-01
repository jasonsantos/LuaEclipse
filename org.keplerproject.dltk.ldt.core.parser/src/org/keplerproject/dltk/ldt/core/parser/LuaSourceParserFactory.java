package org.keplerproject.dltk.ldt.core.parser;

import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserFactory;

public class LuaSourceParserFactory implements ISourceParserFactory {
	public ISourceParser createSourceParser() {
		return new LuaSourceParser();
	}

}
