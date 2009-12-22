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
