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
import org.keplerproject.luaeclipse.parser.Activator;
import org.keplerproject.luaeclipse.parser.LuaSourceParser;
import org.keplerproject.luaeclipse.parser.internal.tests.utils.DummyReporter;
import org.keplerproject.luaeclipse.parser.internal.tests.utils.SpyVisitor;


/**
 * Put AST reliability to the test. Checks if function declarations in AST match
 * declarations on code.
 * 
 * @author kkinfoo
 * 
 */
public class TestASTValidity extends TestCase {

	private SpyVisitor visitor;
	private LuaSourceParser parser;
	private ModuleDeclaration ast;
	private String error = null;

	// Initialize packages names
	private static String _AST;
	private static String _EXPRESSION;
	private static String _STATEMENT;
	static {
		// Retrieve package name
		_AST = Activator.class.getName();
		_AST = _AST.substring(0, _AST.lastIndexOf('.')) + ".ast.";

		// Compose sub packages names
		_EXPRESSION = _AST + "expressions";
		_STATEMENT = _AST + "statements";
	};

	/** Gather errors that occurs during parsing. */
	private String getError() {
		return error == null ? new String() : error;
	}

	public void setUp() {
		visitor = new SpyVisitor();
		parser = new LuaSourceParser();
	}

	/** Checks if visitor is fairly cleared. */
	public void testClear() {

		// Parse a local variable declaration
		String code = "local var";
		String typeName = _STATEMENT + ".Local";
		assertTrue(code + "\n" + getError(), traverse(code));
		boolean assertion = visitor.hasVisitedType(typeName);
		assertTrue("Unable to locate: " + typeName, assertion);

		// Verify that previous parsing has been deleted
		visitor.clear();
		assertion = visitor.hasVisitedType(typeName);
		assertFalse("Able to locate: " + typeName, assertion);
	}

	public void testFor() {

		// Regular numeric for
		String code = "for i=1,10 do end";
		String typeName = _EXPRESSION + ".Identifier";
		assertTrue(code + "\n" + getError(), traverse(code));
		assertEquals("For statement AST nodes count.", 8, visitor.nodesCount());
		boolean assertion = visitor.hasVisitedType(typeName);
		assertTrue("Unable to locate: " + typeName, assertion);

		// Same with step indication
		code = "for i=1,10,2 do end";
		assertTrue(code + "\n" + getError(), traverse(code));
		assertEquals("For statement with step AST nodes count.", 9, visitor
				.nodesCount());
	}

	/**
	 * Indicates if a function declaration is considered only once in AST.
	 */
	public void testFunction() {

		// Check function declaration
		String typeName = _STATEMENT + ".FunctionDeclaration";
		String code = "m = function ()end";
		assertTrue(code + "\n" + getError(), traverse(code));
		assertTrue("Unable to find required type.", visitor
				.hasVisitedType(typeName));
		assertEquals("Wrong declaration count.", 1, visitor.typeCount(typeName));
	}

	/**
	 * Check in pair function declaration {@code table = 'method', function
	 * ()end} .
	 */
	public void testFunctionInPair() {
		
		// Check function declaration
		String code = "table['method'] = function ()end";
		boolean traverseStatus = traverse(code);
		assertTrue(code + "\n" + getError(), traverseStatus);

		// The AST for the code "table['method'] = function ()end" is:
		// `Set{ { `Index{ `Id "table", `String "method" } },
		// { `Function{ { }, { } } } }

		/*
		 * Count check
		 */
		// Set: 1
		String typeName = _STATEMENT + ".Set";
		int typeCount = visitor.typeCount(typeName);
		assertEquals("Wrong assignement count.", 1, typeCount);

		// Index: 1
		typeName = _EXPRESSION + ".Index";
		typeCount = visitor.typeCount(typeName);
		assertEquals("Wrong table count.", 1, typeCount);

		// Id: 1
		typeName = _EXPRESSION + ".Identifier";
		typeCount = visitor.typeCount(typeName);
		assertEquals("Wrong identifier count.", 1, typeCount);

		// String: 1
		typeName = _EXPRESSION + ".String";
		typeCount = visitor.typeCount(typeName);
		assertEquals("Wrong string count.", 1, typeCount);

		// Function: 1
		typeName = _EXPRESSION + ".Funtion";
		typeCount = visitor.typeCount(typeName);
		assertEquals("Wrong function count.", 1, typeCount);

		typeName = _STATEMENT + ".FunctionDeclaration";
		typeCount = visitor.typeCount(typeName);
		assertEquals("Wrong declaration function count.", 1, typeCount);
		assertTrue("Unable to find required type.", visitor
				.hasVisitedType(typeName));
	}

	/**
	 * Check imbricated function declarations {@code first=function()
	 * second=function() end return second end}
	 */
	public void testImbricatedFuntionDeclarations() {
		String code = "first=function() second=function() end return second end";
		String typeName = _STATEMENT + ".FunctionDeclaration";
		assertTrue(code + "\n" + getError(), traverse(code));
		assertTrue("Unable to find required type.", visitor
				.hasVisitedType(typeName));
		assertEquals("Wrong declaration count.", 2, visitor.typeCount(typeName));
	}

	/**
	 * Targets to verify if there is only one Set node in AST while parsing a
	 * single assignment
	 */
	public void testSet() {
		// Here is the Metalua AST for the following code:
		// `Set{ { `Id "m" }, { `Number 1 } }
		String code = "m = 1";
		boolean traverseStatus = traverse(code);
		assertTrue(code + "\n" + getError(), traverseStatus);

		// Try to find type and type count for every node type
		String[] typeNames = { _EXPRESSION + ".Number",
				_EXPRESSION + ".Identifier", _STATEMENT + ".Set" };
		int[] expectedCount = { 1, 1, 1 };
		assertTrue(typeNames.length == expectedCount.length);
		for (int k = 0; k < typeNames.length; k++) {
			int currentTypeCount = visitor.typeCount(typeNames[k]);
			boolean encountredType = visitor.hasVisitedType(typeNames[k]);
			assertTrue("Unable to find " + typeNames[k], encountredType);
			assertEquals("Wrong count for " + typeNames[k], expectedCount[k],
					currentTypeCount);
		}
	}

	/** Check if several function declaration is handled. */
	public void testSeveralFunction() {
		String typeName = _STATEMENT + ".FunctionDeclaration";
		String code = "m = function ()end function l() end";
		assertTrue(code + "\n" + getError(), traverse(code));
		assertTrue("Unable to find required type.", visitor
				.hasVisitedType(typeName));
		assertEquals("Wrong declaration count.", 2, visitor.typeCount(typeName));
	}

	/**
	 * Traverse ASTs nodes
	 * 
	 * @param String
	 *            code The AST is created from this Lua code
	 * @return true on
	 */
	private boolean traverse(String code) {

		// Try to run visitor in AST
		try {
			assertNotNull("Valid string is required.", code);
			char[] fileName = (getClass() + ".java").toCharArray();
			ast = parser.parse(fileName, code.toCharArray(),
					new DummyReporter());

			// Forget previous parsing
			visitor.clear();
			ast.traverse(visitor);
		} catch (Exception e) {
			// If parsing fails bear the reason in mind
			error = e.getMessage();
			return false;
		}
		return true;
	}
}
