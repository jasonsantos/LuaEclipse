/*
 * Copyright (C) 2003-2007 Kepler Project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.keplerproject.ldt.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.keplerproject.ldt.core.LuaScriptsSpecs;
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
 * @version $Id$
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

	// private LuaState luastate;

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
		LuaScriptsSpecs.getDefault().setPreferenceStore(getPreferenceStore());
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
	public boolean initializeAssistExtension(String editorId) {
		// Initialize the extension lists
		this.assistExtensions = new ArrayList();

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
				// Code Assist Contributor
				if (ASSIST_TAG.equals(next.getName())) {
					try {
						String nextEditId = next.getAttribute("editor-ref");
						// Add the contributor if the id matchs or no id
						// especified
						if ((nextEditId == null || nextEditId.equals(""))
								|| nextEditId.equals(editorId)) {

							ILuaContentAssistExtension ext = (ILuaContentAssistExtension) next
									.createExecutableExtension("contributor");
							assistExtensions.add(ext);
						}
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out
								.println("Problems opening Extension Content Assist point ");
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
	 * @param editorId
	 * 
	 * @return true if the extensions was completely initialized
	 */
	public boolean initializeScannerExtension(String editorId) {

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
						String nextEditId = next.getAttribute("editor-ref");
						// Add the contributor if the id matchs or no id
						// especified
						if ((nextEditId == null || nextEditId.equals(""))
								|| nextEditId.equals(editorId)) {
							IScannerRuleExtension ext = (IScannerRuleExtension) next
									.createExecutableExtension("contributor");
							scannerExtensions.add(ext);
						}
					} catch (CoreException e) {
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
	public synchronized List getScannerRulesExtension(String editorId) {

		initializeScannerExtension(editorId);
		return scannerExtensions;

	}

	/**
	 * Returns the Reconcilier Contributor extension. Init if needed
	 * 
	 * @return
	 */
	public synchronized List getReconcilierExtension(String editorId) {
		initializeReconcilierExtension(editorId);
		return this.reconcilierExtensions;
	}

	private boolean initializeReconcilierExtension(String editorId) {
		this.reconcilierExtensions = new ArrayList();

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
						String nextEditId = next.getAttribute("editor-ref");
						// Add the contributor if the id matchs or no id
						// especified
						if ((nextEditId == null || nextEditId.equals(""))
								|| nextEditId.equals(editorId)) {
							ILuaReconcilierExtension ext = (ILuaReconcilierExtension) next
									.createExecutableExtension("contributor");
							reconcilierExtensions.add(ext);
						}
					} catch (CoreException e) {
						// TODO LOG THIS AND HANDLE EXCEPTION
						System.out
								.println("Problems opening Extension Reconcilier point ");
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
	 * Return the content type Extensions. Init if Needed
	 * 
	 * @return
	 */
	public synchronized List getContentTypeExtension(String editorId) {
		initializeContentTypeExtension(editorId);
		return contentExtensions;
	}

	private boolean initializeContentTypeExtension(String editorId) {
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

				// ContentType Contributor
				if (CONTENT_TAG.equals(next.getName())) {
					try {
						String nextEditId = next.getAttribute("editor-ref");
						// Add the contributor if the id matchs or no id
						// especified
						if ((nextEditId == null || nextEditId.equals(""))
								|| nextEditId.equals(editorId)) {
							ILuaContentTypeExtension ext = (ILuaContentTypeExtension) next
									.createExecutableExtension("contributor");
							contentExtensions.add(ext);
						}
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
	 * Return the content assist Extensions. Initialize if needed.
	 * 
	 * @return
	 */
	public synchronized List getAssistExtension(String editorId) {
		initializeAssistExtension(editorId);
		return assistExtensions;
	}

	public static IWorkspace getWorkspace() {
		return ResourcesPlugin.getWorkspace();
	}

}
