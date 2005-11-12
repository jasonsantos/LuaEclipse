package org.keplerproject.ldt.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.core.internal.registry.ExtensionPoint;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentAssistExtension;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentTypeExtension;
import org.keplerproject.ldt.ui.editors.ext.ILuaReconcilierExtension;
import org.keplerproject.ldt.ui.editors.ext.IScannerRuleExtension;
import org.osgi.framework.BundleContext;

/**
 * The main plugin class to be used in the desktop.
 */
public class LDTUIPlugin extends AbstractUIPlugin {

	private static final String PARTITION_SCANNER_POINT = "org.keplerproject.ldt.ui.ScannerRulesExtension";
	private static final String SOURCE_CONFIG_POINT     = "org.keplerproject.ldt.ui.SourceConfigurationExtension";
	
	private static final String PARTITIONER_TAG = "partitioner";
	private static final String RECONCILIER_TAG = "reconcilier";
	private static final String ASSIST_TAG      = "assit";
	private static final String CONTENT_TAG     = "content";
	

	private List scannerExtensions     = null;
	private List reconcilierExtensions = null;
	private List assistExtensions      = null;
	private List contentExtensions     = null;

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

	/**
	 * This method recomputes all extensions of the ui plugin and prepare the
	 * plugins to be used.
	 * 
	 * @return
	 */
	public boolean initializeSourceViewerExtension() {
		this.assistExtensions      = new ArrayList();
		this.reconcilierExtensions = new ArrayList();
		this.contentExtensions     = new ArrayList();
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(
				SOURCE_CONFIG_POINT);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++) {
			IConfigurationElement[] elements = extensions[x]
					.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement next = elements[i];
				if (RECONCILIER_TAG.equals(next.getName())) {
					try {
						ILuaReconcilierExtension ext = (ILuaReconcilierExtension) next
								.createExecutableExtension("contributor");
						reconcilierExtensions.add(ext);
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out
								.println("Problems opening Extension Reconcilier point ");
						e.printStackTrace();
						continue;
					}
				} else if(ASSIST_TAG.equals(next.getName()))
				{
					try {
						ILuaContentAssistExtension ext = (ILuaContentAssistExtension) next
								.createExecutableExtension("contributor");
						assistExtensions.add(ext);
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out
								.println("Problems opening Extension Content Assist point ");
						e.printStackTrace();
						continue;
					}
				}
				else if(CONTENT_TAG.equals(next.getName()))
				{
					try {
						ILuaContentTypeExtension ext = (ILuaContentTypeExtension) next
								.createExecutableExtension("contributor");
						contentExtensions.add(ext);
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out
								.println("Problems opening Extension ContentType point ");
						e.printStackTrace();
						continue;
					}
					
				}else
					continue;
			}
		}
		return true;
	}

	
	
	/**
	 * This method recomputes all extensions of the ui plugin and prepare the
	 * plugins to be used.
	 * 
	 * @return
	 */
	public boolean initializeScannerExtension() {
		scannerExtensions = new ArrayList();
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(
				PARTITION_SCANNER_POINT);
		IExtension[] extensions = p.getExtensions();
		for (int x = 0; x < extensions.length; x++) {
			IConfigurationElement[] elements = extensions[x]
					.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement next = elements[i];
				if (PARTITIONER_TAG.equals(next.getName())) {
					try {
						IScannerRuleExtension ext = (IScannerRuleExtension) next
								.createExecutableExtension("contributor");
						scannerExtensions.add(ext);
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out
								.println("Problems opening Extension Rule Partitioner Scanner point ");
						e.printStackTrace();
						return false;
					}
				} else
					continue;
			}
		}
		return true;
	}
	
	
	
	public List getScannerRulesExtension() {
		if (scannerExtensions != null)
			return scannerExtensions;
		else
		{
			
			initializeScannerExtension();
			return scannerExtensions;
		}
	}
	public List getReconcilierExtension() {
		if (reconcilierExtensions != null)
			return reconcilierExtensions;
		else
		{
			initializeSourceViewerExtension();
			return reconcilierExtensions;
		}
	}
	public List getContentTypeExtension() {
		if (contentExtensions != null)
			return contentExtensions;
		else
		{
			initializeSourceViewerExtension();
			return contentExtensions;
		}
	}
	public List getAssistExtension() {
		if (assistExtensions != null)
			return assistExtensions;
		else
		{
			initializeSourceViewerExtension();
			return assistExtensions;
		}
	}
}
