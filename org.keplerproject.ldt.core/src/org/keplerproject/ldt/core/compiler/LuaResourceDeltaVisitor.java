/*
* Copyright (C) 2003-2007 Kepler Project.
*
* Permission is hereby granted, free of charge, to any person obtaining
* a copy of this software and associated documentation files (the
* "Software"), to deal in the Software without restriction, including
* without limitation the rights to use, copy, modify, merge, publish,
* distribute, sublicense, and/or sell copies of the Software, and to
* permit persons to whom the Software is furnished to do so, subject to
* the following conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

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
 * @version $Id$
 */
public class LuaResourceDeltaVisitor implements IResourceDeltaVisitor,
		IResourceVisitor {
	private static final String _ALERT_FUNCTION = "_ALERT = function(err) string.gsub(err, ':(%d+):(.*)$', function(l, e) java_error(l, e) end) end";

	protected LuaState L;

	public LuaResourceDeltaVisitor() {
		L = LuaStateFactory.newLuaState();
		L.openLibs();
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
			L.LdoString(_ALERT_FUNCTION);
			L.LdoString(code);
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