package org.keplerproject.dltk.ldt.interpreter.syntax;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

public interface IParser {
	public ModuleDeclaration parse(char[] fileName, char[] source,
			IProblemReporter reporter);
}
