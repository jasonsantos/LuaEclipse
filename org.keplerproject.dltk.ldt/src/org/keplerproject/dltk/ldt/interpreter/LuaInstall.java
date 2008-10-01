package org.keplerproject.dltk.ldt.interpreter;

import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.keplerproject.dltk.ldt.core.LuaNature;

public class LuaInstall extends AbstractInterpreterInstall {

	public LuaInstall(IInterpreterInstallType type, String id) {
		super(type, id);
	}

	public String getNatureId() {
		return LuaNature.LUA_NATURE;
	}

}
