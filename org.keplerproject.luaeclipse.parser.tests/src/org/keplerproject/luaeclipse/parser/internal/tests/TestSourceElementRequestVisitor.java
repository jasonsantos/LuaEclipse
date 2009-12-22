/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date$
 * $Author$
 * $Id$
 */
package org.keplerproject.luaeclipse.parser.internal.tests;

import org.keplerproject.luaeclipse.parser.LuaSourceElementParser;
import org.keplerproject.luaeclipse.parser.LuaSourceParser;

import junit.framework.TestCase;


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
