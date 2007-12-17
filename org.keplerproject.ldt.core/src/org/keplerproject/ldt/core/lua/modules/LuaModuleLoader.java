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

package org.keplerproject.ldt.core.lua.modules;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.osgi.framework.Bundle;

/**
 * A Loader for modules installed within LuaEclipse
 * 
 * @author jasonsantos
 * @version $Id$
 */
public class LuaModuleLoader {
	private static final Object sentinel = "Sentinel";

	private static String dumpTable(LuaState L) {
		StringBuilder sb = new StringBuilder();
		if (L.isTable(-1)) {
			L.pushNil();
			while (L.next(-2) != 0) {
				String sKey = L.type(-2) == LuaState.LUA_TNUMBER ? new Integer(
						L.toInteger(-2)).toString() : L.toString(-2);
				String sValue = L.typeName(L.type(-1));
				sb.append(sKey + " - '" + sValue + "'\n");
				L.pop(1);
			}
		}
		return sb.toString();
	}

	private static URL findfile(LuaState L, String name, String pluginName) {
		Bundle b = Platform.getBundle(pluginName);
		name = name.replace('.', '/');
		String[] moduleLocations = { "lua/5.1/" + name + ".lua",
				"lua/5.1/" + name + "/init.lua" };

		URL u = null;

		for (String moduleLocation : moduleLocations) {
			if ((u = b.getResource(moduleLocation)) != null)
				break;
		}

		return u;
	}

	/**
	 * Loads the contents of an URL
	 * 
	 * @param u
	 *            URL to the file
	 * @return String with the file contetn
	 * @throws IOException
	 */
	private static String getFileContents(URL u) throws IOException {
		BufferedReader is = new BufferedReader(new InputStreamReader(u
				.openStream()));
		String s = null;

		StringBuilder moduleBody = new StringBuilder();
		while ((s = is.readLine()) != null) {
			moduleBody.append(s);
		}

		return moduleBody.toString();
	}

	public static int register(LuaState L) {
		if (L == null)
			return -1;

		try {

			L.pushJavaFunction(new JavaFunction(L) {

				@Override
				public int execute() throws LuaException {
					LuaObject o;
					String name = (o = getParam(2)) != null ? o.toString()
							: null;
					int i;
					L.setTop(1); /* _LOADED table will be at index 2 */
					L.getField(LuaState.LUA_REGISTRYINDEX, "_LOADED");
					int loadedTable = L.getTop();
					L.getField(2, name);
					if (L.toBoolean(-1)) { /* is it there? */
						if (L.toJavaObject(-1) == sentinel) { /* check loops */
							L
									.pushString("loop or previous error loading module "
											+ name);
							L.error();
						}
						return 1; /* package is already loaded */
					}
					/* else must load it; iterate over available loaders */
					L.getGlobal("package");
					L.getField(-1, "loaders");

					if (!L.isTable(-1)) {
						L.pushString("'package.loaders' must be a table");
						L.error();
					}

					String ss = dumpTable(L);

					L.pushString(""); /* error message accumulator */
					for (i = 1;; i++) {
						L.rawGetI(-2, i); /* get a loader */
						if (L.isNil(-1)) {
							L.pushString(String.format(
									"module '%s' not found:%s", name, L
											.toString(-2)));
							L.error();
						}
						int params = 1;
						if (L.isUserdata(-1)) {
							int userData = L.getTop();
							if (L.getMetaTable(-1) != 0) {
								L.getField(-1, "__call");
								L.pushValue(userData);
								L.remove(userData);
								L.remove(userData);
								params++;
							}

						}

						L.pushString(name);
						L.call(params, 1); /* call it */
						if (L.isFunction(-1)) /* did it find module? */
							break; /* module loaded successfully */
						else if (L.isString(-1)) /*
													 * loader returned error
													 * message?
													 */
							L.concat(2); /* accumulate it */
						else
							L.pop(1);
					}
					// L.pushJavaObject(sentinel);
					// L.setField(2, name); /* _LOADED[name] = sentinel */
					L.pushString(name); /* pass name as argument to module */
					L.call(1, 1); /* run loaded module */

					if (!L.isNil(-1)) /* non-nil return? */
						L.setField(2, name); /*
												 * _LOADED[name] = returned
												 * value
												 */
					L.getField(2, name);
					if (L.toJavaObject(-1) == sentinel) { /*
															 * module did not
															 * set a value?
															 */
						L.pushBoolean(true); /* use true as result */
						L.pushValue(-1); /* extra copy to be returned */
						L.setField(2, name); /* _LOADED[name] = true */
					}
					return 1;
				}

			});
			L.setGlobal("require");

			L.pushJavaFunction(new JavaFunction(L) {

				@Override
				public int execute() throws LuaException {
					LuaObject objModuleName = getParam(2);

					if (objModuleName != null) {

						try {
							if (loadModule(objModuleName) > 0) {
								return 1;
							} else
								return 0;
						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					return 0;
				}

				/**
				 * @param objModuleName
				 * @throws IOException
				 */
				private int loadModule(LuaObject objModuleName)
						throws IOException {
					String sFileName = objModuleName.toString();
					URL u = findfile(L, sFileName, "org.keplerproject.ldt.core");

					if (u == null)
						return 0;

					String body = getFileContents(u);

					int res = L.LloadString(body);
					if (res != 0) {
						String err = "error loading module " + sFileName
								+ " from resource " + u.getPath() + ":\n\t"
								+ L.toString(-1);
						System.out.println(err);
						L.pushString(err);
						L.error();
						return 1;
					}

					return 1;
				}

			});

			L.getGlobal("package");
			L.getField(-1, "loaders");
			String ss = dumpTable(L);
			L.pushNumber(2); // index 2
			L.pushValue(-4); // function
			L.setTable(-3); // Sets the Java Package Loader as the second
			// loader
		} catch (Exception e) {
			// do nothing
		}

		return 0;
	}
}
