package com.anwrt.ldt.ui.interpreter.preferences;

import org.eclipse.dltk.internal.debug.ui.interpreters.InterpretersBlock;
import org.eclipse.dltk.internal.debug.ui.interpreters.ScriptInterpreterPreferencePage;

import com.anwrt.ldt.ui.interpreter.preferences.LuaInterpretersBlock;

public class LuaInterpreterPreferencePage extends
		ScriptInterpreterPreferencePage {
	
	public static final String PAGE_ID =
			"com.anwrt.ldt.interpreter.preferences";

	@Override
	public InterpretersBlock createInterpretersBlock() {
		return new LuaInterpretersBlock();
	}

}
