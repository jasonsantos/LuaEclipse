package org.keplerproject.dltk.ldt.core.parser;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.keplerproject.dltk.ldt.interpreter.LuaInterpreter;
import org.osgi.framework.BundleContext;

public class LuaEclipseParserPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.keplerproject.dltk.ldt.core.parser";
	private static LuaEclipseParserPlugin plugin;

	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static LuaEclipseParserPlugin getDefault() {
		return plugin;
	}

	public static LuaInterpreter getInterpreter() {
		return LuaInterpreter.getInterpreter();
	}

}
