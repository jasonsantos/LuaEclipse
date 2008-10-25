package org.keplerproject.dltk.ldt.ui.interpreter;

import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.ScriptInterpreterPreferencePage;

public class LuaInterpreterPreferencePage extends
		ScriptInterpreterPreferencePage {
	public static final String PAGE = "org.keplerproject.dltk.ldt.preferences.interpreters";

	@Override
	public InterpretersBlock createInterpretersBlock() {
		return new LuaInterpretersBlock();
	}

}
