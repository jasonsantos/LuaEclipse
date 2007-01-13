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

package org.keplerproject.ldt.ui.baseExts.scanner;

/**
 * Lua syntax tokens
 * 
 * @author guilherme
 * @version $Id$
 * 
 */
public interface ILuaSyntax {
	public static final String[] reservedwords = { "for", "while", "do", "end",
			"repeat", "until", "if", "elseif", "then", "else", "return",
			"break", "function", "local", "in" };

	public static final String[] predicates = { "+", "-", "*", "/", "^", "..",
			"<", "<=", ">", ">=", "==", "~=" };

	public static final String[] constants = { "false", "true", "nil" };

	public static final String[] otherpredicates = { "not", "and", "or" };

	public static final String[] functions = { "assert", "collectgarbage",
			"dofile", "error", "getfenv", "getmetatable", "gcinfo", "ipairs",
			"loadfile", "loadstring", "next", "pairs", "pcall", "print",
			"rawequal", "rawget", "rawset", "require", "setfenv",
			"setmetatable", "tonumber", "tostring", "type", "unpack", "xpcall" };

	Object[] allWords = { reservedwords, predicates, constants, otherpredicates };

	// The string type for functions
	public static final String FUNCTION_TYPE_NAME = "function";

}
