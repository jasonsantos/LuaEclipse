package org.keplerproject.luaeclipse.ui.interpreter.preferences;

import org.eclipse.dltk.internal.debug.ui.interpreters.AbstractInterpreterLibraryBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.IAddInterpreterDialogRequestor;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.eclipse.swt.widgets.Shell;
import org.keplerproject.luaeclipse.ui.interpreter.preferences.LuaInterpreterLibraryBlock;


public class AddLuaInterpreterDialog extends AddScriptInterpreterDialog {

	public AddLuaInterpreterDialog(IAddInterpreterDialogRequestor requestor,
            Shell shell, IInterpreterInstallType[] interpreterInstallTypes,
            IInterpreterInstall editedInterpreter) {
		super(requestor, shell, interpreterInstallTypes, editedInterpreter);
	}

	@Override
	protected AbstractInterpreterLibraryBlock createLibraryBlock(AddScriptInterpreterDialog dialog) {
		return new LuaInterpreterLibraryBlock(dialog);
	}

}
