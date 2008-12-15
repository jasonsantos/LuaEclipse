package org.keplerproject.dltk.ldt;

import org.eclipse.dltk.codeassist.IAssistParser;
import org.eclipse.dltk.codeassist.ScriptCompletionEngine;
import org.eclipse.dltk.compiler.env.ISourceModule;
import org.eclipse.dltk.core.IField;
import org.eclipse.dltk.core.IMethod;
import org.eclipse.dltk.core.IType;

public class LuaCompletionEngine extends ScriptCompletionEngine {

	@Override
	protected int getEndOfEmptyToken() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected String processFieldName(IField arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String processMethodName(IMethod arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected String processTypeName(IType arg0, String arg1) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public IAssistParser getParser() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void complete(ISourceModule arg0, int arg1, int arg2) {
		// TODO Auto-generated method stub

	}

}
