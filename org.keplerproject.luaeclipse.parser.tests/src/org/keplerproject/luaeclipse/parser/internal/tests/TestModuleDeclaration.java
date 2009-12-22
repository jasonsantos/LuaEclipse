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


package org.keplerproject.luaeclipse.parser.internal.tests;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.keplerproject.luaeclipse.parser.LuaSourceParser;
import org.keplerproject.luaeclipse.parser.internal.tests.utils.DummyReporter;


public class TestModuleDeclaration extends TestCase {

	/**
	 * Mainly tests if ASTs are smartly cached
	 */
	public void testParse() {
		
		LuaSourceParser parser = new LuaSourceParser();
		DummyReporter reporter = new DummyReporter();

		// Local variable declaration
		ModuleDeclaration start = parser.parse(null, "local var".toCharArray(),
				reporter);

		// Fuzzy state between two stables ones
		ModuleDeclaration fuzzy = parser.parse(null,
				"local var=".toCharArray(), reporter);

		// Assignment
		ModuleDeclaration stable = parser.parse(null, "local var=1"
				.toCharArray(), reporter);

		// Check if faulty ASTs are ignored
		assertEquals("AST from cache should have be provided.", start, fuzzy);

		// Check if new valid AST is cached
		assertNotSame(
				"Stable AST from cache should have been replaced by a newer one.",
				start, stable);
	}
}
