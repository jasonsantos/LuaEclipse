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

import java.io.BufferedInputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;

import org.eclipse.core.runtime.Platform;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.osgi.framework.Bundle;

/**
 * @author jasonsantos
 *
 */
public class LuaModuleLoader {

	public static int register(LuaState L) {
		if(L==null)
			return -1;
		
		try {
			
			L.getGlobal("package");
			L.getField(-1, "loaders");
			
			L.pushNumber(1);
			L.pushJavaFunction(new JavaFunction(L) {

				@Override
				public int execute() throws LuaException {
					
					LuaObject objModuleName = getParam(2);
					
					if(objModuleName!=null) {
						Bundle b = Platform.getBundle("org.keplerproject.ldt.core");
						String moduleLocation = "/lua/5.1/" + objModuleName.toString().replace('.', '/') + ".lua";
						
						try {
							InputStreamReader is = new InputStreamReader(b.getResource(moduleLocation).openStream());
							char[] cbuf = null;
							is.read(cbuf);
							
							String moduleBody = new String(cbuf);
							
							L.LloadString(moduleBody);
							return 1;
							
						} catch (IOException e) {
							e.printStackTrace();
						}
						
					}
					
					return 0;
				}
				
			});
			
			L.setTable(-3);
			
		} catch (Exception e) {
			// do nothing
		}
		
		return 0;
	}
	
}
