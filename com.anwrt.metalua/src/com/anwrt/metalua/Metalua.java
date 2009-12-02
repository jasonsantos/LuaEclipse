/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-17 14:32:31 +0200 (ven., 17 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: Metalua.java 2112 2009-07-17 12:32:31Z kkinfoo $
 */
package com.anwrt.metalua;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;

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
	static {
		try {
			state = MetaluaStateFactory.newLuaState();
		} catch (LuaException e) {
			e.printStackTrace();
		}
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
			MetaluaStateFactory.raise(state);
		}
	}
}
