package org.keplerproject.ldt.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class LDTUIPlugin extends AbstractUIPlugin {

	private static final String PARTITION_SCANNER_POINT = "org.keplerproject.ldt.ui.partitionerScannerRules";

	private static final Object TAG_RULE = "rule";

	private static final String CODE_SCANNER_POINT = "org.keplerproject.ldt.ui.reconcilierScannerRules";

	// The shared instance.
	private static LDTUIPlugin plugin;

	private ResourceBundle resourceBundle;

	/**
	 * The constructor.
	 */
	public LDTUIPlugin() {
		plugin = this;
		resourceBundle = ResourceBundle
				.getBundle("org.keplerproject.ldt.ui.LDTPluginPluginResources");
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
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(
				"org.keplerproject.ldt.ui", path);
	}

	public ResourceBundle getResourceBundle() {
		return resourceBundle;
	}

	public IScannerRuleExtension[] getPartitionRuleExtension() {
		List extensionsList = new ArrayList();
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(
				PARTITION_SCANNER_POINT);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++) {
			IConfigurationElement[] elements = extensions[x]
					.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement next = elements[i];
				if (TAG_RULE.equals(next.getName())) 
				{
					try {
						IScannerRuleExtension ext = (IScannerRuleExtension) next
								.createExecutableExtension("contributor");
						extensionsList.add(ext);
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out.println("Problems opening Extension Rule Partitioner Scanner point ");
						e.printStackTrace();
					}
				} else
					continue;
			}
		}
		IScannerRuleExtension [] result = new IScannerRuleExtension[extensionsList.size()];
		extensionsList.toArray(result);
		return result;
	}

	public IScannerRuleExtension[] getReconcilierRuleExtension() {
		List extensionsList = new ArrayList();
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(
				CODE_SCANNER_POINT);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++) {
			IConfigurationElement[] elements = extensions[x]
					.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement next = elements[i];
				if (TAG_RULE.equals(next.getName())) 
				{
					try {
						IScannerRuleExtension ext = (IScannerRuleExtension) next
								.createExecutableExtension("contributor");
						extensionsList.add(ext);
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out.println("Problems opening Extension Rule Partitioner Scanner point ");
						e.printStackTrace();
					}
				} else
					continue;
			}
		}
		IScannerRuleExtension [] result = new IScannerRuleExtension[extensionsList.size()];
		extensionsList.toArray(result);
		return result;
	}
}
