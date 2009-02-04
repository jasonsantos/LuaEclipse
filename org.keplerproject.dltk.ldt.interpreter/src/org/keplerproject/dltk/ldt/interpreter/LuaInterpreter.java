package org.keplerproject.dltk.ldt.interpreter;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

public class LuaInterpreter {
	LuaState L;

	private LuaInterpreter(String description) {
		if (L == null) {
			L = LuaStateFactory.newLuaState();
			L.openLibs();
		}
	}

	static LuaInterpreter interpreter;

	public static LuaInterpreter getInterpreter() {
		if (interpreter == null) {
			interpreter = new LuaInterpreter("default");
		}
		return interpreter;
	}

	public void loadStandardLibraries() {
		L.openLibs();
	}

}
