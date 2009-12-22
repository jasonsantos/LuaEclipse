package com.anwrt.metalua.tests;

import junit.framework.TestSuite;

import com.anwrt.metalua.tests.internal.cases.TestMetalua;
import com.anwrt.metalua.tests.internal.cases.TestMetaluaStateFactory;

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
