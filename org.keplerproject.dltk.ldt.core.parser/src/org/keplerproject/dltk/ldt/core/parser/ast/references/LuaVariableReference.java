package org.keplerproject.dltk.ldt.core.parser.ast.references;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.references.VariableReference;
import org.eclipse.dltk.utils.CorePrinter;

public class LuaVariableReference extends VariableReference {
	
	public LuaVariableReference(int start, int end, String name) {
		super(start, end, name);
	}

	private List<Expression> references = new ArrayList<Expression>();

	public void addReference(Expression reference) {
		references.add(reference);
	}
		
	@Override
	public void printNode(CorePrinter output) {
		super.printNode(output);
	}

}
