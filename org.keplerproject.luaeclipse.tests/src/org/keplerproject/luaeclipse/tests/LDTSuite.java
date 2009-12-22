/******************************************************************************
 * Copyright (c) 2009 KeplerProject, Sierra Wireless.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 *          - initial API and implementation and initial documentation
 *****************************************************************************/


/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:55:03 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: LDTSuite.java 1841 2009-06-15 15:55:03Z kkinfoo $
 */
package org.keplerproject.luaeclipse.tests;

import junit.framework.TestCase;
import junit.framework.TestSuite;

// TODO: Auto-generated Javadoc
/**
 * Gathers all tests for LDT.
 * 
 * @author kkinfoo
 */
public class LDTSuite extends TestSuite {
	
	/** The asserts enabled. */
	static boolean assertsEnabled;
	static{
		assert assertsEnabled = true; // Intentional side effect!!!
	}


	/**
	 * All {@link TestCase} registered from  contributed {@link TestSuite}.
	 * 
	 * @return {@link TestSuite} to be run
	 */
	public static TestSuite suite() {
		return SuiteProvider.get();
	}
}
