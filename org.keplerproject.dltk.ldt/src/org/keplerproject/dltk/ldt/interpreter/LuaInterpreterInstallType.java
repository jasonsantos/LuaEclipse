package org.keplerproject.dltk.ldt.interpreter;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.ILog;
import org.eclipse.core.runtime.IPath;
import org.eclipse.dltk.core.environment.IDeployment;
import org.eclipse.dltk.internal.launching.AbstractInterpreterInstallType;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.keplerproject.dltk.ldt.core.LuaCorePlugin;
import org.keplerproject.dltk.ldt.core.LuaNature;

public class LuaInterpreterInstallType extends AbstractInterpreterInstallType {
	private static final String[] INTERPRETER_NAMES = { "lua", "lua50",
			"lua5.1", "lua51", "lua.exe", "lua50.exe", "lua5.1.exe",
			"lua51.exe" };

	protected File createPathFile() throws IOException {
		return storeToMetadata(LuaCorePlugin.getDefault(), "path.lua",
				"scripts/path.lua");
	}

	private File storeToMetadata(LuaCorePlugin default1, String string,
			String string2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected IInterpreterInstall doCreateInterpreterInstall(String id) {
		return new LuaInstall(this, id);
	}

	@Override
	protected ILog getLog() {
		return LuaCorePlugin.getDefault().getLog();
	}

	@Override
	protected String getPluginId() {
		return LuaCorePlugin.PLUGIN_ID;
	}

	@Override
	protected String[] getPossibleInterpreterNames() {
		// TODO: obtain from configuration
		return INTERPRETER_NAMES;
	}

	public String getName() {
		return "Lua";
	}

	public String getNatureId() {
		return LuaNature.LUA_NATURE;
	}

	@Override
	protected IPath createPathFile(IDeployment arg0) throws IOException {
		// TODO: return the path to a script
		return null;
	}

}
