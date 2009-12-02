package com.anwrt.metalua.tests.internal.cases;

import java.io.IOException;

import junit.framework.TestCase;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.keplerproject.luajava.LuaException;
import org.osgi.framework.Bundle;

import com.anwrt.metalua.Metalua;
import com.anwrt.metalua.tests.Suite;

/**
 * Make sure that calls to Metalua work
 * 
 * @author kkinfoo
 * 
 */
public class TestMetalua extends TestCase {
	private static Bundle BUNDLE;
	static {
		BUNDLE = Platform.getBundle(Suite.PLUGIN_ID);
	}

	/** Make sure that syntax errors are catchable by Lua exception */
	public void testHandleErrors(){
		boolean error = false;
		String message = new String(); 
		try {
			Metalua.run("for");
		} catch (LuaException e) {
			error = true;
			message = e.getMessage();
		}
		assertTrue(message, error);
	}
	
	/** Run from source */
	public void testRunLuaCode() {

		boolean success = true;
		String message = new String();

		// Proofing valid code
		try {
			Metalua.run("var = 1+1");
		} catch (LuaException e) {
			message = e.getMessage();
			success = false;
		}
		assertTrue("Single assignment does not work: " + message, success);

		// Proofing wrong code
		try {
			Metalua.run("var local = 'trashed'");
			success = true;
		} catch (LuaException e) {
			success = false;
		}
		assertFalse("Wrong code is accepted", success);
	}

	/** Run Lua source file */
	public void testRunLuaFile() {

		boolean success = false;
		String message = new String();

		// Proofing valid file
		try {
			Metalua.runFile(path("/scripts/assignment.lua"));
			success = true;
		} catch (LuaException e) {
			message = e.getMessage();
		}
		assertTrue("Single assignment from a file does not work: " + message,
				success);

		// Proofing wrong file
		try {
			Metalua.runFile(path("/scripts/john.doe"));
		} catch (LuaException e) {
			success = false;
		}
		assertFalse("Inexistant file call works.", success);
	}

	/** Run from source */
	public void testRunMetaluaCode() {

		boolean success = true;
		String message = "";

		// Proofing valid code
		try {
			Metalua.run("ast = mlc.luastring_to_ast ( \"var = 1 + 2\" )");
		} catch (LuaException e) {
			message = e.getMessage();
			success = false;
		}
		assertTrue("Single assignment does not work: " + message, success);
	}

	/** Run Metalua source file */
	public void testRunMetaluaFile() {
		boolean success = true;
		String message = new String();

		// Proofing valid file
		try {
			Metalua.runFile(path("/scripts/introspection.mlua"));
		} catch (LuaException e) {
			message = e.getMessage();
			success = false;
		}
		assertTrue("Code from a file does not work: " + message, success);
	}

	/** Ensure access to  portable file locations */
	private String path(String uri) throws LuaException {
		try {
			return FileLocator.toFileURL( BUNDLE.getEntry(uri) ).getPath();
		} catch (IOException e) {
			throw new LuaException("Unable to provide URI for: " + uri);
		} catch (NullPointerException npe) {
			throw new LuaException("File not found: " + uri + ". "
					+ npe.getMessage());
		}
	}
}
