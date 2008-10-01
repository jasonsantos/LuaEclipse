package org.keplerproject.dltk.ldt.core.parser.ast.declarations;

import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.utils.CorePrinter;

public class LuaFunctionDeclaration extends MethodDeclaration {

	public LuaFunctionDeclaration(String name, int nameStart, int nameEnd,
			int declStart, int declEnd) {
		super(name, nameStart, nameEnd, declStart, declEnd);
	}
	
	@Override
	public void printNode(CorePrinter output) {
		output.print("function ");
		output.print(this.getName());
		output.print("(");
		for (Object argument: arguments) {
			LuaArgument arg = (LuaArgument) argument;
			arg.printNode(output);
		}
		output.print(")");
		this.getBody().printNode(output);
		output.print("end");
		super.printNode(output);
	}
}
