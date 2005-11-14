package org.keplerproject.ldt.ui.baseExts;

import luajava.LuaState;
import luajava.LuaStateFactory;

import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class BaseExtsPlugin extends Plugin {

	//The shared instance.
	private static BaseExtsPlugin plugin;
	private LuaState luastate;
	
	/**
	 * The constructor.
	 */
	public BaseExtsPlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static BaseExtsPlugin getDefault() {
		return plugin;
	}

	/**
	 * Temporary method
	 * TODO centralize the lua State??? or not??
	 * @return
	 */
	public LuaState getLuaState() {
		if(luastate == null)
			this.luastate = LuaStateFactory.newLuaState();
					
		return this.luastate;
	}

}
