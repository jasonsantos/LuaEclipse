package org.keplerproject.dltk.ldt.core.parser;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.parser.AbstractSourceParser;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.keplerproject.dltk.ldt.core.parser.ast.LuaModuleDeclaration;

public class LuaSourceParser extends AbstractSourceParser {

	public ModuleDeclaration parse(char[] fileName, char[] source,
			IProblemReporter reporter) {

		// TODO: remove
		LuaLexer lexer = new LuaLexer(source);
		LuaParser parser = new LuaParser(lexer, false);

		// TODO: add
		// MetaLuaParser parser = new MetaLuaParser(fileName, source, reporter);

		LuaModuleDeclaration moduleDeclaration = parser.parse();
		moduleDeclaration.rebuild();
		return moduleDeclaration;
	}
}
