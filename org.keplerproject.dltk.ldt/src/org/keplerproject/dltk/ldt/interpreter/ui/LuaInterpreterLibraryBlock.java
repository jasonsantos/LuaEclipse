package org.keplerproject.dltk.ldt.interpreter.ui;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.LibraryLabelProvider;
import org.eclipse.jface.dialogs.IDialogSettings;
import org.eclipse.jface.viewers.IBaseLabelProvider;
import org.keplerproject.dltk.ldt.core.LuaCorePlugin;

public class LuaInterpreterLibraryBlock extends AbstractInterpreterLibraryBlock {

	protected LuaInterpreterLibraryBlock(AddScriptInterpreterDialog dialog) {
		super(dialog);
	}

	@Override
	protected IDialogSettings getDialogSettions() {
		return LuaCorePlugin.getDefault().getDialogSettings();
	}

	@Override
	protected IBaseLabelProvider getLabelProvider() {
		return new LibraryLabelProvider();
	}

}
