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
import java.util.Map;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.keplerproject.ldt.core.ILuaEntry;
import org.keplerproject.ldt.core.LuaProject;
import org.keplerproject.ldt.core.LuaScriptsSpecs;
import org.keplerproject.ldt.core.luadoc.LuadocGenerator;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * The Lua Code DeltaVisitor remade to fit Lua 5.1 API
 * 
 * @author guilherme
 * @author jasonsantos
 * @version $Id: LuaResourceDeltaVisitor.java,v 1.7 2007/11/16 21:37:52
 *          jasonsantos Exp $
 */
public class LuaResourceDeltaVisitor implements IResourceDeltaVisitor,
		IResourceVisitor {
	protected LuaState L;

	public LuaResourceDeltaVisitor() {
		L = LuaStateFactory.newLuaState();
		L.openLibs();
		return;
	}

	public boolean visit(IResourceDelta delta) throws CoreException {
		final IResource res = delta.getResource();
		if (LuaScriptsSpecs.getDefault().isValidLuaScriptFileName(res)) {
			if(LuaScriptsSpecs.getDefault().isLuaDocAutoGenerationActive())
				updateLuadocEntries(res);

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

		// Comment out the 'shabang' (#!) from the beginning of file if found
		code = code.replaceAll("^(\\s*)#!", "$1--#!");

		// enclose code in a function to avoid error
		// -- extra line break before 'end' avoids error when last line of code
		// is a -- comment
		code = "return function(...) " + code + " \nend";

		LuaAlert alert = new LuaAlert(res);

		int result = L.LdoString(code);

		if (result != 0) {
			String s = L.toString(-1);
			alert.reportLuaError(s);
		}
	}

	private String readFile(IResource res) {

		File f = new File(res.getLocation().toOSString());
		try {
			FileInputStream fis = new FileInputStream(f);
			byte b[] = new byte[(int) f.length()];
			fis.read(b);
			String content = new String(b);
			b = null;
			fis.close();
			return content;
		} catch (FileNotFoundException filenotfoundexception) {
		} catch (IOException ioexception) {
		}
		return null;
	}

	public boolean visit(final IResource res) throws CoreException {

		if (LuaScriptsSpecs.getDefault().isValidLuaScriptFileName(res)) {
			if(LuaScriptsSpecs.getDefault().isLuaDocAutoGenerationActive())
				updateLuadocEntries(res);

			compileFile(res, L);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * Updates all luadocs entries for a resource. All entries already stored on
	 * the project file will be replaced by the new entries found through the
	 * execution of luadoc.
	 * 
	 * @param res
	 *            the resource where to run luadoc
	 */
	private void updateLuadocEntries(final IResource res) {
		IProject prj = res.getProject();

		LuaProject lp = new LuaProject();
		lp.setProject(prj);

		String resourceFileName = res.getLocation().toOSString();
		// TODO: create a wrapper class for this map to better illustrate
		// semantics
		Map<String, ILuaEntry> resourceStoredEntries = lp
				.getLuaEntries(resourceFileName);

		resourceStoredEntries.clear();

		LuadocGenerator lg = LuadocGenerator.getInstance();

		Map<String, ILuaEntry> generatedEntries = lg.generate(resourceFileName);

		resourceStoredEntries.putAll(generatedEntries); // puts the
		// documentation
		// information into the
		// resource storage

		lg.generateIndexes(generatedEntries);

		lp.saveLuaDocEntries();
	}

}