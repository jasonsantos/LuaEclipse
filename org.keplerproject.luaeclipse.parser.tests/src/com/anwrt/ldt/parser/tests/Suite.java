/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Suite.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package com.anwrt.ldt.parser.tests;

import junit.framework.TestCase;
import junit.framework.TestSuite;

import com.anwrt.ldt.parser.LuaSourceParser;
import com.anwrt.ldt.parser.internal.tests.TestASTValidity;
import com.anwrt.ldt.parser.internal.tests.TestExpressions;
import com.anwrt.ldt.parser.internal.tests.TestLuaBinaryOperations;
import com.anwrt.ldt.parser.internal.tests.TestLuaSourceParser;
import com.anwrt.ldt.parser.internal.tests.TestModuleDeclaration;
import com.anwrt.ldt.parser.internal.tests.TestSourceElementRequestVisitor;
import com.anwrt.ldt.parser.internal.tests.TestStatements;
import com.anwrt.ldt.parser.internal.tests.TestUnaryOperations;
import com.anwrt.ldt.parser.internal.tests.TestVisitor;

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
