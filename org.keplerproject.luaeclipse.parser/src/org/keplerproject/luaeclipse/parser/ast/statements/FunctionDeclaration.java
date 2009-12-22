package org.keplerproject.luaeclipse.parser.ast.statements;

import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;

public class FunctionDeclaration extends MethodDeclaration {

    public FunctionDeclaration(String name, int nameStart, int nameEnd, int start,
	    int end) {
	super(name, nameStart, nameEnd, start, end);
	this.setModifier(Declaration.D_METHOD);
	this.setModifiers(Declaration.AccPublic);
    }
}
