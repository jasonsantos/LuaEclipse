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


package org.keplerproject.luaeclipse.metalua.tests;

import org.keplerproject.luaeclipse.metalua.tests.internal.cases.TestMetalua;
import org.keplerproject.luaeclipse.metalua.tests.internal.cases.TestMetaluaStateFactory;

import junit.framework.TestSuite;


/** Gathers tests about Metalua */
public class Suite extends TestSuite {

	// The plug-in ID
	public static final String PLUGIN_ID = "org.keplerproject.luaeclipse.metalua.tests";

	/** Registers all tests to run */
	public Suite(){
		super();
		addTestSuite(TestMetaluaStateFactory.class);
		addTestSuite(TestMetalua.class);
		setName("Metalua");
	}
}
