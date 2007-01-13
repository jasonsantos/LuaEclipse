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
package org.keplerproject.ldt.internal.launching;

/**
 * Lua Configuration Attributes Names
 * @author guilherme
 * @version $Id$ 
 */
public interface LuaLaunchConfigurationAttribute
{

    public static final String LUA_LAUNCH_CONFIGURATION_TYPE = "org.keplerproject.ldt.launching.LaunchConfigurationTypeLuaApplication";
    public static final String FILE_NAME = "org.keplerproject.ldt.launching.FILE_NAME";
    public static final String INTERPRETER_ARGUMENTS = "org.keplerproject.ldt.launching.INTERPRETER_ARGUMENTS";
    public static final String ENVIRONMENT_VARS= "org.keplerproject.ldt.launching.ENVIRONMENT_VARS";
    public static final String PROGRAM_ARGUMENTS = "org.keplerproject.ldt.launching.PROGRAM_ARGUMENTS";
    public static final String PROJECT_NAME = "org.keplerproject.ldt.launching.PROJECT_NAME";
    public static final String SELECTED_INTERPRETER = "org.keplerproject.ldt.launching.SELECTED_INTERPRETER";
    public static final String WORKING_DIRECTORY = "org.keplerproject.ldt.launching.WORKING_DIRECTORY";
    public static final String USE_DEFAULT_WORKING_DIRECTORY = "org.keplerproject.ldt.launching.USE_DEFAULT_WORKING_DIRECTORY";
}