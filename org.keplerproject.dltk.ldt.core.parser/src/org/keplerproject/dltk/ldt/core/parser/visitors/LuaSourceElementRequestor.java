package org.keplerproject.dltk.ldt.core.parser.visitors;

import org.eclipse.dltk.compiler.ISourceElementRequestor;
import org.eclipse.dltk.compiler.SourceElementRequestVisitor;

public class LuaSourceElementRequestor extends SourceElementRequestVisitor {

	public LuaSourceElementRequestor(ISourceElementRequestor requesor) {
		super(requesor);
	}

}
