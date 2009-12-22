/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:55:03 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: SuiteProvider.java 1841 2009-06-15 15:55:03Z kkinfoo $
 */
package org.keplerproject.luaeclipse.tests;

import junit.framework.TestSuite;

import org.eclipse.core.runtime.IExtension;
import org.eclipse.core.runtime.IExtensionPoint;
import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

// TODO: Auto-generated Javadoc
/**
 * The Class SuiteProvider.
 */
public class SuiteProvider {
	
	/**
	 * Gets the.
	 * 
	 * @return the test suite
	 */
	public static TestSuite get() {

		// Get plug-in's contributors
		TestSuite suite = new TestSuite("LDT");
		IExtensionPoint extensionPoint = Platform.getExtensionRegistry()
				.getExtensionPoint(Activator.EXTENSION_POINT);

		// Append every TestSuite to the current one
		for (IExtension ext : extensionPoint.getExtensions() ) {

			// Get the good extension point from schema
			String className = ext.getConfigurationElements()[Activator.EXTENSION_POINT_ID]
					.getAttribute("class");

			// Retrieve instance of contributor's plug-in 
			String bundleId = ext.getContributor().getName();
			Bundle bundle = Platform.getBundle(bundleId);

			// Load the TestSuite
			try {

				// Retrieve instance of contributor through it's plug-in  
				Object newInstance = bundle.loadClass(className).newInstance();
				if (newInstance instanceof TestSuite) {
					suite.addTest((TestSuite) newInstance);
				}

			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return suite;
	}
}
