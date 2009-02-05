package org.keplerproject.dltk.ldt.core.parser;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.IProblemReporter;
import org.keplerproject.dltk.ldt.interpreter.syntax.IParser;

public class MetaLuaParser implements IParser {

	@Override
	public ModuleDeclaration parse(char[] fileName, char[] source,
			IProblemReporter reporter) {

		// TODO: load the interpreter (should be a factory rather than a
		// singleton, to allow a distinct interpreter per project, if necessary)
		// TODO: load metalua extension files into the interpreter (damn!
		// preference pages! :-( )
		// TODO: run all extensions on the interpreter LuaState
		// TODO: find a way to include extensions for formatting, refactoring,
		// etc
		// TODO: register the AST into the interpreter LuaState

		return null;
	}

}
