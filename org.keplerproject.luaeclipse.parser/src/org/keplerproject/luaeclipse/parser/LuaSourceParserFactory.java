package org.keplerproject.luaeclipse.parser;

import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserFactory;

/**
 * Provides source parser
 * 
 * @author Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 */
public class LuaSourceParserFactory implements ISourceParserFactory {

    public LuaSourceParserFactory() {
    }

    @Override
    public ISourceParser createSourceParser() {
	return new LuaSourceParser();
    }

}
