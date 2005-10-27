package org.keplerproject.ldt.ui;

import java.util.ResourceBundle;

import org.eclipse.ui.plugin.*;
import org.eclipse.jface.resource.ImageDescriptor;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class LDTUIPlugin extends AbstractUIPlugin {

	//The shared instance.
	private static LDTUIPlugin plugin;
	private ResourceBundle resourceBundle;
	
	/**
	 * The constructor.
	 */
	public LDTUIPlugin() {
		plugin = this;
		resourceBundle =
			ResourceBundle.getBundle(
				"org.keplerproject.ldt.ui.LDTPluginPluginResources");
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
	public static LDTUIPlugin getDefault() {
		return plugin;
	}

	/**
	 * Returns an image descriptor for the image file at the given
	 * plug-in relative path.
	 *
	 * @param path the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin("org.keplerproject.ldt.ui", path);
	}


	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}
}
