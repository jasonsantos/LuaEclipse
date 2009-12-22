package org.keplerproject.luaeclipse.ui.internal.interpreter;

import org.eclipse.dltk.launching.AbstractInterpreterInstall;
import org.eclipse.dltk.launching.IInterpreterInstallType;
import org.keplerproject.luaeclipse.core.LuaNature;


public class LuaInterpreterInstall extends AbstractInterpreterInstall {

	public LuaInterpreterInstall(IInterpreterInstallType type, String id) {
		super(type, id);
	}

	@Override
	public String getNatureId() {
		return LuaNature.LUA_NATURE;
	}

}
