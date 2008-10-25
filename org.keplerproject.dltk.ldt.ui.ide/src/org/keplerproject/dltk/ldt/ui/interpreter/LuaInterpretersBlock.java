package org.keplerproject.dltk.ldt.ui.interpreter;

import org.eclipse.dltk.internal.debug.ui.interpreters.AddScriptInterpreterDialog;
import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.launching.ScriptRuntime;
import org.keplerproject.dltk.ldt.core.LuaNature;

public class LuaInterpretersBlock extends InterpretersBlock {

	@Override
	protected AddScriptInterpreterDialog createInterpreterDialog(
			IInterpreterInstall standin) {
		AddLuaInterpreterDialog dialog = new AddLuaInterpreterDialog(this, getShell(), ScriptRuntime.getInterpreterInstallTypes(getCurrentNature()), standin);
		return dialog;
	}

	@Override
	protected String getCurrentNature() {
		return LuaNature.LUA_NATURE;
	}

}
