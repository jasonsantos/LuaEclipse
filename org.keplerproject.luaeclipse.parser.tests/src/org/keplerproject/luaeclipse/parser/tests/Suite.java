/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Suite.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.tests;

import org.keplerproject.luaeclipse.parser.LuaSourceParser;
import org.keplerproject.luaeclipse.parser.internal.tests.TestASTValidity;
import org.keplerproject.luaeclipse.parser.internal.tests.TestExpressions;
import org.keplerproject.luaeclipse.parser.internal.tests.TestLuaBinaryOperations;
import org.keplerproject.luaeclipse.parser.internal.tests.TestLuaSourceParser;
import org.keplerproject.luaeclipse.parser.internal.tests.TestModuleDeclaration;
import org.keplerproject.luaeclipse.parser.internal.tests.TestSourceElementRequestVisitor;
import org.keplerproject.luaeclipse.parser.internal.tests.TestStatements;
import org.keplerproject.luaeclipse.parser.internal.tests.TestUnaryOperations;
import org.keplerproject.luaeclipse.parser.internal.tests.TestVisitor;

import junit.framework.TestCase;
import junit.framework.TestSuite;


/**
 * The Class Suite, groups all {@linkplain TestCase} for {@link LuaSourceParser}
 */
public class Suite extends TestSuite {

	/**
	 * Instantiates a new suite registering all {@link TestCase} of the plug-in.
	 * 
	 */
	public Suite() {
		super();
		setName("Lua Source parser");
		addTestSuite(TestASTValidity.class);
		addTestSuite(TestExpressions.class);
		addTestSuite(TestLuaBinaryOperations.class);
		addTestSuite(TestLuaSourceParser.class);
		addTestSuite(TestModuleDeclaration.class);
		addTestSuite(TestSourceElementRequestVisitor.class);
		addTestSuite(TestStatements.class);
		addTestSuite(TestUnaryOperations.class);
		addTestSuite(TestVisitor.class);
	}
}
