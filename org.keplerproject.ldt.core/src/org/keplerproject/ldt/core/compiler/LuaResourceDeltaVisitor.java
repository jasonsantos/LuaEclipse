/******************************************
 * 
 ******************************************/
package org.keplerproject.ldt.core.compiler;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;

import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * The Lua Code DeltaVisitor
 * 
 * @author guilherme
 * @version 1.0
 */
public class LuaResourceDeltaVisitor implements IResourceDeltaVisitor,
		IResourceVisitor {
	private static final String _ALERT_FUNCTION = "_ALERT = function(err) string.gsub(err, ':(%d+):(.*)$', function(l, e) java_error(l, e) end) end";

	protected LuaState L;

	public LuaResourceDeltaVisitor() {
		L = LuaStateFactory.newLuaState();
		L.openBasicLibraries();
		return;
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		final IResource res = delta.getResource();
		if ("lua".equalsIgnoreCase(res.getFileExtension())) {

			compileFile(res, L);

			return false;
		} else {
			return true;
		}
	}

	private void compileFile(IResource res, LuaState L) {
		try {
			res.deleteMarkers("org.eclipse.core.resources.problemmarker", true,
					2);
		} catch (CoreException coreexception) {
		}
		String code = readFile(res);
		code = "return function() " + code + "\n end";
		LuaAlert alert = new LuaAlert(L, res);
		try {
			alert.register("java_error");
			L.doString(_ALERT_FUNCTION);
			L.doString(code);
		} catch (LuaException luaexception) {
		}
	}

	private String readFile(IResource res) {
		File f = new File(res.getLocation().toOSString());
		try {
			FileInputStream fis = new FileInputStream(f);
			byte b[] = new byte[(int) f.length()];
			fis.read(b);
			String content = new String(b);
			b = (byte[]) null;
			fis.close();
			return content;
		} catch (FileNotFoundException filenotfoundexception) {
		} catch (IOException ioexception) {
		}
		return null;
	}

	public boolean visit(final IResource res) throws CoreException {

		if ("lua".equalsIgnoreCase(res.getFileExtension())) {
			compileFile(res, L);
			return false;
		} else {
			return true;
		}
	}

}