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
