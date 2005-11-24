/*********************************************
 * 
 * 
 *********************************************/
package org.keplerproject.ldt.core;

import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The Core Plugin Class. This plugin provide the common infra-structure to the
 * LDT plugins.
 * 
 * @author Guilherme Martins
 * @version 1.0.0
 */
public class LuaCorePlugin extends AbstractUIPlugin {

	private static final String PLUGIN_CORE_ID = "org.keplerproject.ldt.core";

	// The shared instance.
	private static LuaCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public LuaCorePlugin() {
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
	public static LuaCorePlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_CORE_ID, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
	 */
	protected void initializeImageRegistry(ImageRegistry reg) {
		String[] images = { "function", "table", "string", "userdata" };

		for (int i = 0; i < images.length; i++) {
			ImageDescriptor image = null;

			image = LuaCorePlugin.getImageDescriptor("icons/" + images[i]
					+ ".gif");
			reg.put(images[i], image);
		}

		super.initializeImageRegistry(reg);
	}

}
