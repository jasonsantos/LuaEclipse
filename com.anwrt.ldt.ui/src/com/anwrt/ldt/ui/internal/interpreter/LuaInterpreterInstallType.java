package com.anwrt.ldt.ui.internal.interpreter;

import java.io.IOException;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.osgi.framework.Bundle;

import com.anwrt.ldt.ui.Activator;
import com.anwrt.ldt.core.LuaNature;
import com.anwrt.ldt.ui.internal.interpreter.LuaInterpreterInstall;

public class LuaInterpreterInstallType extends AbstractInterpreterInstallType {
	
	private static final String[] INTERPRETER_NAMES = { "lua", "lua5.1" };

	@Override
	protected IPath createPathFile(IDeployment deployment) throws IOException {
		Bundle bundle = Activator.getDefault().getBundle();
		return deployment.add(bundle, "scripts/path.lua");
	}

	@Override
	protected IInterpreterInstall doCreateInterpreterInstall(String id) {
		return new LuaInterpreterInstall(this, id);
	}

	@Override
	protected ILog getLog() {
		return Activator.getDefault().getLog();
	}

	@Override
	protected String getPluginId() {
		return Activator.PLUGIN_ID;
	}

	@Override
	protected String[] getPossibleInterpreterNames() {
		return INTERPRETER_NAMES;
	}

	@Override
	public String getName() {
		return "Lua";
	}

	@Override
	public String getNatureId() {
		return LuaNature.LUA_NATURE;
	}

}
