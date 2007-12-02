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

package org.keplerproject.ldt.core.luadoc;

import java.util.HashMap;
import java.util.Map;

import org.keplerproject.ldt.core.ILuaEntry;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;


/**
 * Runs a luadoc engine to generate entries.
 * 
 * @author jasonsantos
 * @version $Id$
 */

public class LuadocGenerator {
	private static LuadocGenerator singleton;
	protected Map<String, ILuaEntry > luaEntryIndex;

	public LuadocGenerator() {
		super();
    	luaEntryIndex = new HashMap<String, ILuaEntry >();
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
			LuaState L = LuaStateFactory.newLuaState();
			
			L.openLibs();
			
			L.pushJavaFunction(new JavaFunction(L) {

				@Override
				public int execute() throws LuaException {
					//String t = getParam(1).toString();
					LuaObject fileOrModuleName = getParam(2); 
					
					LuaObject entryName = getParam(3);
					LuaObject entryType = getParam(4);

					LuaObject entrySummary = getParam(5);
					LuaObject entryDescription = getParam(6);
					LuaObject entryComment = getParam(7);
					LuaObject entryCode = getParam(8);
					
					LuadocEntry e = new LuadocEntry();
					
					e.setModule(fileOrModuleName.toString());
					e.setEntryType(entryType.toString());
					e.setName(entryName.toString());
					e.setSummary(entrySummary.toString());
					e.setDescription(entryDescription.toString());
					e.setComment(entryComment.toString());
					e.setCode(entryCode.toString());
					
					m.put(entryName.toString(), e);
					return 0;
				}

			});

			L.setGlobal("addDocumentationEntry");

		  	int result = L.LdoString("require 'lfs' "+ "\n"  
							+ "  require 'luadoc'" + "\n"
							+ "local files = {'"
							+ fileName 
							+ "'}"+ "\n"
							+ "local options = require 'luadoc.config'"+ "\n"
							+ "module ('loopback.doclet', package.seeall)"+ "\n"
							+ "t = {}"+ "\n"
							+ "function start(doc)"+ "\n"
								+ "local fileOrModuleName = ''"+ "\n"
								+ "r = function(d)"+ "\n"
									+ "for k, v in pairs(d) do"+ "\n"
										+ "if type(v)=='table' then"+ "\n"
											+ "if v.class=='function' then"+ "\n"
												+ "addDocumentationEntry(" +
														"fileOrModuleName, " +
														"v.name, " +
														"v.class, " +
														"v.summary," +
														"v.description," +
														"table.concat(v.comment, '\\n')," +
														"table.concat(v.code, '\\n')" +
													")"+ "\n"
											+ "elseif v.type == 'file' or v.type == 'module'  then"+ "\n"
												+ "fileOrModuleName = v.name"+ "\n" 
											+ "end" + "\n"
											+ "r(v)"+ "\n"
										+ "end" + "\n"
									+ "end" + "\n"
								+ "end"+ "\n" 
								+ "r(doc)" 
							+ "end"+ "\n"
							+ "options.doclet = 'loopback.doclet'"+ "\n"
							+ "luadoc.main(files, options)");

			

			
			if( result != 0) {
	            String s = L.toString(-1);
				System.out.println(s);
			}
			
			L.close();
			
			return m;
			
		} catch (LuaException e) {
		}

		// collect the documentation blocks, indexed by name
		// load the documentation blocks into the resulting map
		return null;
	}
	
    public Map<String,ILuaEntry > getLuaEntryIndex() {
    	return luaEntryIndex;
    }

	public String getDocumentationText(String token) {
		LuadocEntry l = (LuadocEntry)getLuaEntryIndex().get(token);
		String doc = null;
		if(l!=null) {
			// TODO: enhance the summary with HTML formatting
			doc = l.getComment();
			// TODO: enhance the non-summary value with module information
			if(doc==null || doc.length()==0)
				doc = l.getCode();

			if(doc==null || doc.length()==0)
				doc = l.getName();
		}
		return doc;
	}
}
