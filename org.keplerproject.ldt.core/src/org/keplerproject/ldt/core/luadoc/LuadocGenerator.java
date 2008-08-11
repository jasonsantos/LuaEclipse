/*
 * Copyright (C) 2003-2007 Kepler Project. Permission is hereby granted, free of
 * charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the
 * Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.keplerproject.ldt.core.luadoc;

import java.util.HashMap;
import java.util.Map;

import org.keplerproject.ldt.core.ILuaEntry;
import org.keplerproject.ldt.core.lua.modules.LuaModuleLoader;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;
import org.keplerproject.luajava.luafilesystem.JLuaFileSystem;

/**
 * Runs a luadoc engine to generate entries.
 * 
 * @author jasonsantos
 * @version $Id$
 */

public class LuadocGenerator {
	private static LuadocGenerator		singleton;
	protected Map<String, ILuaEntry>	luaEntryIndex;

	public LuadocGenerator() {
		super();
		luaEntryIndex = new HashMap<String, ILuaEntry>();
	}

	public static LuadocGenerator getInstance() {
		if (singleton == null)
			singleton = new LuadocGenerator();
		return singleton;
	}

	// register the java functions to create the entries
	public Map<String, ILuaEntry> generate(String fileName) {
		final Map<String, ILuaEntry> m = new HashMap<String, ILuaEntry>();

		try {
			LuaState L = loadLuadocLuaState(m);

			final String CR = "\n";

			String sfile = fileName.replaceAll("\\\\", "\\\\\\\\");
			// System.out.println(sfile);

			String code = "" + "require 'luadoc' " + CR + "" + CR
					+ "local files = {'" + sfile + "'}" + CR
					+ "local options = require 'luadoc.config'" + CR + "" + CR
					+ "options.doclet = 'eclipse.doclet' " + CR + "" + CR
					+ "luadoc.main(files, options)";

			// L.LdoString("debug.sethook(function(...) __fdebug =
			// io.open([[C:\\Documents and
			// Settings\\jasonsantos\\Desktop\\out.log]], 'a')
			// table.foreach(debug.getinfo(2), function(n, o) __fdebug:write(n,
			// tostring(o) .. '\\n') end ) __fdebug:close() end,'cr')");

			int result = L.LloadString(code);

			if (result != 0) {

				String s = L.toString(-1);
				// System.out.println(s);
			}

			result = L.pcall(0, 0, 0);

			if (result != 0) {
				String s = L.toString(-1);
				// System.out.println(s);
			}

			L.close();

			return m;

		} catch (LuaException e) {
		}

		// collect the documentation blocks, indexed by name
		// load the documentation blocks into the resulting map
		return null;
	}

	public LuaState loadLuadocLuaState(final Map<String, ILuaEntry> m)
			throws LuaException {
		LuaState L = null;
		try {
			L = LuaStateFactory.newLuaState();

			L.openLibs();

			JLuaFileSystem.register(L);
			LuaModuleLoader.register(L);

			L.pushJavaFunction(new JavaFunction(L) {

				@Override
				public int execute() throws LuaException {
					// String t = getParam(1).toString();
					LuaObject fileOrModuleName = getParam(2);

					LuaObject entryName = getParam(3);
					LuaObject entryType = getParam(4);

					LuaObject entrySummary = getParam(5);
					LuaObject entryDescription = getParam(6);
					LuaObject entryComment = getParam(7);
					LuaObject entryHTML = getParam(8);

					LuadocEntry e = (LuadocEntry) createLuaEntry(
							fileOrModuleName.toString(), entryName.toString(),
							entryType.toString(), entrySummary.toString(),
							entryDescription.toString(), entryComment
									.toString(), entryHTML.toString());

					m.put(entryName.toString(), e);
					return 0;
				}

			});

			L.setGlobal("addDocumentationEntry");
		} catch (Exception e) {
			System.out.println("Could not initialize LuaState:"
					+ e.getMessage());
		}
		return L;
	}

	public Map<String, ILuaEntry> getLuaEntryIndex() {
		return luaEntryIndex;
	}

	public ILuaEntry getBestEntryIndex(String token) {
		Map<String, ILuaEntry> index = getLuaEntryIndex();
		ILuaEntry entry;
		token = token.replaceAll("[:]", ".");
		while ((entry = index.get(token)) == null && token.indexOf('.') > 1)
			token = token.substring(token.indexOf('.') + 1);

		return entry;
	}

	public void generateIndexes(Map<String, ILuaEntry> generatedEntries) {
		LuadocGenerator lg = LuadocGenerator.getInstance();
		for (String s : generatedEntries.keySet()) {
			// store every entry into the generator's flat index
			// --- "by my hand ish ill done"

			// TODO: create a way of navigating module dependencies to determine
			// priority for selecting symbols

			lg.getLuaEntryIndex().put(s, generatedEntries.get(s));
		}
	}

	public String getDocumentationText(String token) {
		LuadocEntry l = (LuadocEntry) getBestEntryIndex(token);
		String doc = null;
		if (l != null) {

			doc = l.getHtml();

			// TODO: enhance the non-summary value with module information
			if (doc == null || doc.length() == 0)
				doc = l.getComment();

			if (doc == null || doc.length() == 0)
				doc = l.getName();
		}
		return doc;
	}

	/**
	 * @param fileOrModuleName
	 * @param entryName
	 * @param entryType
	 * @param entrySummary
	 * @param entryDescription
	 * @param entryComment
	 * @param entryHTML
	 * @return
	 */
	public ILuaEntry createLuaEntry(String fileOrModuleName, String entryName,
			String entryType, String entrySummary, String entryDescription,
			String entryComment, String entryHTML) {
		LuadocEntry e = new LuadocEntry();

		e.setModule(fileOrModuleName);
		e.setEntryType(entryType);
		e.setName(entryName);
		e.setSummary(entrySummary);
		e.setDescription(entryDescription);
		e.setComment(entryComment);
		e.setHTML(entryHTML);
		return e;
	}
}
