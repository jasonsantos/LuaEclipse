package com.anwrt.ldt.ui.interpreter.preferences;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.LibraryLabelProvider;
import org.eclipse.jface.viewers.IBaseLabelProvider;

public class LuaInterpreterLibraryBlock extends AbstractInterpreterLibraryBlock {

	protected LuaInterpreterLibraryBlock(AddScriptInterpreterDialog dialog) {
		super(dialog);
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new LibraryLabelProvider();
	}

}
