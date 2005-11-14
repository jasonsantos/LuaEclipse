/***************************************
 * 
 * 
 ***********************************/
package org.keplerproject.ldt.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import luajava.LuaState;
import luajava.LuaStateFactory;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
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
 * 
 * The Plugin of the main LDT UI features. This plugin export most importants
 * Extension points of the UI.
 * 
 * @author Guilherme Martins
 * @version 1.0
 */
public class LDTUIPlugin extends AbstractUIPlugin {

	/** Partitioner Scanner Extension Point ID */
	private static final String PARTITION_SCANNER_POINT = "org.keplerproject.ldt.ui.ScannerRulesExtension";

	/** Source Configuration Extension Point ID */
	private static final String SOURCE_CONFIG_POINT = "org.keplerproject.ldt.ui.SourceConfigurationExtension";

	/** * The PARTITIONER tag name */
	private static final String PARTITIONER_TAG = "partitioner";

	/** * The RECONCILIER tag name */
	private static final String RECONCILIER_TAG = "reconcilier";

	/** * The ASSIST tag name */
	private static final String ASSIST_TAG = "assist";

	/** * The CONTENT tag name */
	private static final String CONTENT_TAG = "content";

	/** The Scanner extensions * */
	private List scannerExtensions = null;

	/** The Reconcilier Extensions */
	private List reconcilierExtensions = null;

	/** The assist Extesions */
	private List assistExtensions = null;

	/** The content Extensions */
	private List contentExtensions = null;

	// The shared instance.
	private static LDTUIPlugin plugin;

	private ResourceBundle resourceBundle;

	private LuaState luastate;

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
	 * This method recomputes all source viewer extensions of the ui plugin and
	 * prepare the plug-ins to be used.
	 * 
	 * @return true if the extensions was completely loaded;
	 */
	public boolean initializeSourceViewerExtension() {
		// Initialize the extension lists
		this.assistExtensions = new ArrayList();
		this.reconcilierExtensions = new ArrayList();
		this.contentExtensions = new ArrayList();

		// Fetch the Plug-in Registry for the extensions
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(
				SOURCE_CONFIG_POINT);
		IExtension[] extensions = p.getExtensions();
		// Separete the specific contributors.
		for (int x = 0; x < extensions.length; x++) {
			IConfigurationElement[] elements = extensions[x]
					.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement next = elements[i];
				// Reconcilier contributor
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
				}
				// Code Assist Contributor
				else if (ASSIST_TAG.equals(next.getName())) {
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
				// ContentType Contributor
				else if (CONTENT_TAG.equals(next.getName())) {
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

				} else
					continue;
			}
		}
		return true;
	}

	/**
	 * This method recomputes all Partitioner Scanner extensions of the ui
	 * plugin and prepare the plugins to be used.
	 * 
	 * @return true if the extensions was completely initialized
	 */
	public boolean initializeScannerExtension() {
		// init the extension collection
		scannerExtensions = new ArrayList();
		// Fetch the plug-in regitry
		IExtensionPoint p = Platform.getExtensionRegistry().getExtensionPoint(
				PARTITION_SCANNER_POINT);
		IExtension[] extensions = p.getExtensions();
		// for each extension Poit
		for (int x = 0; x < extensions.length; x++) {
			IConfigurationElement[] elements = extensions[x]
					.getConfigurationElements();
			for (int i = 0; i < elements.length; i++) {
				IConfigurationElement next = elements[i];
				// Add the Partitioner contributor.
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

	/**
	 * Reuturns the partitioner scanner Extensions. Initialize all of then if is
	 * needed.
	 * 
	 * @return
	 */
	public List getScannerRulesExtension() {
		if (scannerExtensions != null)
			return scannerExtensions;
		else {
			initializeScannerExtension();
			return scannerExtensions;
		}
	}

	/**
	 * Returns the Reconcilier Contributor extension. Init if needed
	 * 
	 * @return
	 */
	public List getReconcilierExtension() {
		if (reconcilierExtensions != null)
			return reconcilierExtensions;
		else {
			initializeSourceViewerExtension();
			return reconcilierExtensions;
		}
	}

	/**
	 * Return the content type Extensions. Init if Needed
	 * 
	 * @return
	 */
	public List getContentTypeExtension() {
		if (contentExtensions != null)
			return contentExtensions;
		else {
			initializeSourceViewerExtension();
			return contentExtensions;
		}
	}

	/**
	 * Return the content assist Extensions. Initialize if needed.
	 * 
	 * @return
	 */
	public List getAssistExtension() {
		if (assistExtensions != null)
			return assistExtensions;
		else {
			initializeSourceViewerExtension();
			return assistExtensions;
		}
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
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
