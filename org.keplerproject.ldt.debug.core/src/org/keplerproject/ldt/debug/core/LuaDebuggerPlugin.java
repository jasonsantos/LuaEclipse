package org.keplerproject.ldt.debug.core;

import java.io.File;
import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Plugin;
import org.osgi.framework.BundleContext;

/**
 * The activator class controls the plug-in life cycle
 */
public class LuaDebuggerPlugin extends Plugin {

	// The plug-in ID
	public static final String			PLUGIN_ID							= "org.keplerproject.ldt.debug.core";

	public static final String			ID_LUA_DEBUG_MODEL					= "lua.debugModelPresentation";

	public static final String			ID_LUA_SOURCE_LOCATOR				= "lua.sourceLocator";

	public static final String			ID_LUA_LAUNCH_CONFIGURATION_TYPE	= "lua.launchType";

	public static final String			LUA_SCRIPT_ATTRIBUTE				= ID_LUA_DEBUG_MODEL
																					+ ".LUA_SCRIPT_ATTRIBUTE";

	public static final String			LUA_PROJECT_ATTRIBUTE				= ID_LUA_DEBUG_MODEL
																					+ ".LUA_PROJECT_ATTRIBUTE";

	// The shared instance
	private static LuaDebuggerPlugin	plugin;

	/**
	 * The constructor
	 */
	public LuaDebuggerPlugin() {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugins#start(org.osgi.framework.BundleContext)
	 */
	@Override
	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.Plugin#stop(org.osgi.framework.BundleContext)
	 */
	@Override
	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	/**
	 * Returns the shared instance
	 * 
	 * @return the shared instance
	 */
	public static LuaDebuggerPlugin getDefault() {
		return plugin;
	}

	/**
	 * Return a <code>java.io.File</code> object that corresponds to the
	 * specified <code>IPath</code> in the plugin directory, or
	 * <code>null</code> if none.
	 */
	public static File getFileInPlugin(IPath path) {
		try {

			URL installURL = FileLocator.find(getDefault().getBundle(), path,
					null);
			URL localURL = FileLocator.toFileURL(installURL);

			return new File(localURL.getFile());
		} catch (IOException ioe) {
			return null;
		}
	}

}
