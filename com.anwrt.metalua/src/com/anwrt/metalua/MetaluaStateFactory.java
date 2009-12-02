/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:55:03 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: MetaluaStateFactory.java 1841 2009-06-15 15:55:03Z kkinfoo $
 */
package com.anwrt.metalua;

import java.io.IOException;
import java.net.URL;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import org.osgi.framework.Bundle;

// TODO: Auto-generated Javadoc
/**
 * Provides {@link LuaState} loaded with Metalua.
 * 
 * @author Kévin KIN-FOO <kkinfoo@anwyware-tech.com>
 * {@linkplain http://metalua.luaforge.net/manual000.html}
 */
public class MetaluaStateFactory {

	/**
	 * Provides a LuaState that can run Metalua code
	 * 
	 * Just gives a LuaState loaded with the Metalua library.
	 * 
	 * @return LuaState able to run Metalua code
	 * 
	 * @throws LuaException the lua exception
	 * 
	 * @see		{@link LuaState}
	 * @since	1.0
	 */
	public static LuaState newLuaState() throws LuaException {

		/**
		 * Locate plug-in root, it will be Metalua's include path
		 */
		String metaluaPath;
		Bundle bundle = Platform.getBundle(Activator.PLUGIN_ID);
		URL url = bundle.getEntry("/");

		// Stop when plug-in's root can't be located
		try {
			metaluaPath = FileLocator.toFileURL(url).getPath();
		} catch (IOException e) {
			e.printStackTrace();
			throw new LuaException("Unable to locate plugin root");
		}

		/*
		 * Create a regular LuaState, then enable it to run Metalua
		 */
		LuaState l = LuaStateFactory.newLuaState();

		// Load default libraries, in order to modify PATH
		l.openLibs();

		// Update path in order to be able to load Metalua
		String path = "package.path = package.path  .. ';" + metaluaPath
				+ "?.luac;" + metaluaPath + "?.lua'";

		// Load Metalua's byte code
		String require = "require 'metalua.compiler'";

		// Detect problems
		switch (l.LdoString(path)  +l.LdoString(require)) {
			default:
				raise(l);
			case 0:
		}

		// State is ready
		return l;
	}

	/**
	 * Retrieve error message from a LuaState.
	 * 
	 * @param l the l
	 * 
	 * @throws LuaException the lua exception
	 */
	protected static void raise(LuaState l) throws LuaException {

		// Get message at top of stack
		String msg = l.toString(-1);

		// Clean stack
		l.pop(-1);
		throw new LuaException(msg);
	}
}
