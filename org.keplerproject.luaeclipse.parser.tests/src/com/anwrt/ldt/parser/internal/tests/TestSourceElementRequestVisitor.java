/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date$
 * $Author$
 * $Id$
 */
package com.anwrt.ldt.parser.internal.tests;

import junit.framework.TestCase;

import com.anwrt.ldt.parser.LuaSourceElementParser;
import com.anwrt.ldt.parser.LuaSourceParser;

/**
 * The Class TestSourceElementRequestVisitor aims to provide a way to trace how
 * AST from {@linkplain LuaSourceParser} behaves.
 */
public class TestSourceElementRequestVisitor extends TestCase {

	/**
	 * Test source element request visitor.
	 */
	public void testSourceElementRequestVisitor() {
		LuaSourceElementParser visitor = new LuaSourceElementParser();
		assertNotNull("Visitor from element parser is not defined.", visitor
				.createVisitor());
	}
}
