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
 * @date $Date: 2009-07-17 14:32:31 +0200 (ven., 17 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Metalua.java 2112 2009-07-17 12:32:31Z kkinfoo $
 */
package org.keplerproject.luaeclipse.metalua;

import java.io.File;
import java.io.IOException;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.osgi.framework.Bundle;

// TODO: Auto-generated Javadoc
/**
 * Enables to run Metalua code and source files quickly.
 * 
 * It works with an unique inner {@link LuaState} instance as loading Metalua
 * could be pretty time costly.
 * 
 * @author kkinfoo
 */
public class Metalua {

	/*
	 * Load Metalua
	 */
	/** The state. */
	private static LuaState state;
	private static String sourcePath = null;
	static {
		try {
			state = MetaluaStateFactory.newLuaState();
		} catch (LuaException e) {
			Activator.log(e);
		}
	}

	public static LuaState get() {
		return state;
	}

	public static String sourcesPath() {

		// Define source path at first call
		if (sourcePath == null) {
			/**
			 * Locate plug-in root, it will be Metalua's include path
			 */
			Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
			// Ensure to have source available on disk, not from jar or any kind of archive
			bundle.getEntry("/");

			// Stop when plug-in's root can't be located
			try {
				sourcePath = FileLocator.getBundleFile(bundle).getPath();
			} catch (IOException e) {
				return new String();
			}
		}
		return sourcePath + File.separator;
	}

	/**
	 * Reloads inner {@link LuaState}
	 * 
	 * If runtime behavior turns weird, call this method to reset it.
	 * 
	 * @throws LuaException
	 *             the lua exception
	 */
	public static void refreshState() throws LuaException {
		state = MetaluaStateFactory.newLuaState();
	}

	/**
	 * Runs Metalua code
	 * 
	 * {@code Metalua.run("print 'hello world'")}
	 * 
	 * @param code
	 *            the code
	 * 
	 * @return True if Lua accept code, false else way
	 * 
	 * @throws LuaException
	 *             the lua exception
	 */
	public static void run(String code) throws LuaException {
		if (state.LdoString(code) != 0) {
			refreshState();
			MetaluaStateFactory.raise(state);
		}
	}

	/**
	 * Runs Metalua code from a file
	 * 
	 * {@code Metalua.runFile("call/me.mlua")}
	 * 
	 * @param fileURI
	 *            the file uri
	 * 
	 * @return True if Lua accept code, false else way
	 * 
	 * @throws LuaException
	 *             the lua exception
	 */
	public static void runFile(String fileURI) throws LuaException {
		if (state.LdoFile(fileURI) != 0) {
			refreshState();
			MetaluaStateFactory.raise(state);
		}
	}
}
