package org.keplerproject.luaeclipse.parser.ast.statements;

import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.TypeDeclaration;

public class TableDeclaration extends TypeDeclaration {

    public TableDeclaration(String name, int nameStart, int nameEnd, int start,
	    int end) {
	super(name, nameStart, nameEnd, start, end);
	this.setModifiers(Declaration.AccPublic);
    }
}
