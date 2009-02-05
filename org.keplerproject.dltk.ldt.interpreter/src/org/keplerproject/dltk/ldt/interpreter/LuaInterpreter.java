package org.keplerproject.dltk.ldt.interpreter;

import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

public class LuaInterpreter {
	LuaState L;
	boolean valid = false;

	private LuaInterpreter(String description) {
		open(true);
	}

	static LuaInterpreter interpreter;

	public static LuaInterpreter getInterpreter() {
		if (interpreter == null) {
			interpreter = new LuaInterpreter("default");
		}
		return interpreter;
	}

	public boolean isValid() {
		return valid;
	}

	protected void open(boolean withLibraries) {
		if (L == null) {
			L = LuaStateFactory.newLuaState();

			if (withLibraries) {
				loadStandardLibraries();
			}

			valid = true;
		}
	}

	protected void close() {
		if (L != null) {
			L.close();
			L = null;
		}
	}

	public void reset() {
		close();
		open(false);
	}

	public void reset(boolean withLibraries) {
		close();
		open(withLibraries);
	}

	public void loadStandardLibraries() {
		if (L == null) {
			throw new IllegalStateException();
		}
		// TODO: stub
	}

	public boolean loadModule(String moduleName) {
		if (L == null) {
			throw new IllegalStateException();
		}
		// TODO: stub
		return false;
	}

	public Object doFile(String fileName) {
		if (L == null) {
			throw new IllegalStateException();
		}
		// TODO: stub
		return null;
	}

	public Object doString(String chunk) {
		if (L == null) {
			throw new IllegalStateException();
		}
		// TODO: stub
		return null;
	}

}
