package org.keplerproject.dltk.ldt.ui.editor;

import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

public class LuaEditorUIPlugin extends AbstractUIPlugin {

	public static final String PLUGIN_ID = "org.keplerproject.dltk.ldt.ui.editor";
	private static LuaEditorUIPlugin plugin;
	private LuaTextTools textTools;
	
	public LuaEditorUIPlugin() {
	}

	public void start(BundleContext context) throws Exception {
		super.start(context);
		plugin = this;
	}

	public void stop(BundleContext context) throws Exception {
		plugin = null;
		super.stop(context);
	}

	public static LuaEditorUIPlugin getDefault() {
		return plugin;
	}

	public synchronized LuaTextTools getTextTools() {
		textTools = (textTools == null) ? new LuaTextTools(true) : textTools;
		return textTools;
	}
}
