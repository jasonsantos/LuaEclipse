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

					LuaObject objModuleName = getParam(2);

					if (objModuleName != null) {

						try {
							URL u = findfile(L, objModuleName.toString(),
									"org.keplerproject.ldt.core");

							if (u == null)
								return 0;

							String body = getFileContents(u);

							int res = L.LloadString(body);
							if (res != 0) {
								String err = L.toString(-1);
								System.out.println(err);
								return 1;
							}

							return 1;

						} catch (IOException e) {
							e.printStackTrace();
						}

					}

					return 0;
				}

			});
			L.setGlobal("__JavaResourceModuleLoader");

			L.getGlobal("package");
			L.getField(-1, "loaders");

			int res = L.LloadString("return __JavaResourceModuleLoader(...)");
			if (res != 0) {
				String err = L.toString(-1);
				System.out.println(err);
			}
			L.rawSetI(-2, 4);

		} catch (Exception e) {
			// do nothing
		}

		return 0;
	}
}
