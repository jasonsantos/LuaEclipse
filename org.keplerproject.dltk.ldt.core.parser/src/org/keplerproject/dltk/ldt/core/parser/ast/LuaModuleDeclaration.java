package org.keplerproject.dltk.ldt.core.parser.ast;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;

public class LuaModuleDeclaration extends ModuleDeclaration {

	public LuaModuleDeclaration(int sourceLength) {
		super(sourceLength);
	}

	public LuaModuleDeclaration(int sourceLength, boolean rebuildEnabled) {
		super(sourceLength, rebuildEnabled);
	}
}
