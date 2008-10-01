package org.keplerproject.dltk.ldt.ui.ide;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class LuaEclipseIDEPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.keplerproject.dltk.ldt.ui.ide";
	private static LuaEclipseIDEPlugin plugin;
	
	public LuaEclipseIDEPlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static LuaEclipseIDEPlugin getDefault() {
		return plugin;
	}

}
