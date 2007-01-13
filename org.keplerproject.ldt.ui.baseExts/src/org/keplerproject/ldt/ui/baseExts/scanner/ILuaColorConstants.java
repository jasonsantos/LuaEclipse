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

import org.eclipse.swt.graphics.RGB;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public interface ILuaColorConstants {
	public static final RGB LUA_MULTI_LINE_COMMENT = new RGB(64, 128, 128);
    public static final RGB LUA_SINGLE_LINE_COMMENT = new RGB(64, 128, 128);
    public static final RGB LUA_DEFAULT = new RGB(0, 0, 0);
    public static final RGB LUA_KEYWORD = new RGB(127, 0, 85);
    public static final RGB LUA_CONSTANTS = new RGB(127, 0, 85);
    public static final RGB LUA_FUNCTION = new RGB(64, 0, 200);
    public static final RGB LUA_STRING = new RGB(0, 0, 255);
	public static final RGB LUA_METHOD_NAME = new RGB(0, 0, 0);
	public static final RGB LUA_OTHER_PREDICATE = new RGB(0, 0, 0);
	
}
