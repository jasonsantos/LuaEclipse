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


package org.keplerproject.luaeclipse.metalua.tests.internal.cases;

import junit.framework.TestCase;

import org.keplerproject.luaeclipse.metalua.MetaluaStateFactory;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;


public class TestMetaluaStateFactory extends TestCase {

    /** Detect error at LuaState allocation */
    public void testLoaded() {
	boolean loaded = true;
	String message = "";
	try {
	    LuaState state = MetaluaStateFactory.newLuaState();
	    message = state.getClass().getName();
	    // Check stack size in order to prevent bad configuration
	    int top = state.getTop();
	    assertEquals(message + " not stable after initialisation.", top, 0);
	} catch (LuaException e) {
	    loaded = false;
	    message = e.getMessage();
	    e.printStackTrace();
	}
	assertTrue("Metalua is not loaded: " + message, loaded);
    }

    /**
     * Assure that stack is free at allocation
     * 
     * To ensure any error message is hidden in the stack
     */
    public void testEmptyStack() {
	int problem = 1;
	String msg = "";

	try {
	    problem = MetaluaStateFactory.newLuaState().getTop();
	} catch (LuaException e) {
	    msg = e.getMessage();
	    e.printStackTrace();
	}
	assertTrue("State's stack is not empty at instanciation: " + problem
		+ ". " + msg, problem == 0);
    }
}
