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

import java.io.File;
import java.io.IOException;

/**
 *  Lua interpreter Bean. Represents a interpreteer instance that will run Lua Scripts
 * @author guilherme
 * @version $Id$
 */
public class LuaInterpreter
{

    protected File file;
    protected String name;
    protected String[] environment;
    public LuaInterpreter(String aName, String validFile)
    {
        if(validFile != null)
            file = new File(validFile);
        name = aName;
    }
    public LuaInterpreter(String aName,String validFile,String[] env)
    {
    	this(aName,validFile);
    	this.environment = env;    	
    }

    public String getFileName()
    {
        if(file != null)
            return file.getAbsolutePath();
        else
            return null;
    }

    public void setFileName(String fileName)
    {
        file = new File(fileName);
    }

    public String getName()
    {
        return name;
    }

    public String getLabel()
    {
        return getName() + " (" + getCommand() + ")";
    }

    public void setName(String newName)
    {
        name = newName;
    }

    public String getCommand()
    {
        return getFileName();
    }

    public Process exec(String arguments, File workingDirectory)
        throws IOException
    {
        String command = getCommand() + " " + arguments;
        return Runtime.getRuntime().exec(command, getEnvironment(), workingDirectory);
    }

    public String[] getEnvironment() {
	
		return environment;
	}
	public boolean equals(Object other)
    {
        if(other instanceof LuaInterpreter)
        {
            LuaInterpreter otherInterpreter = (LuaInterpreter)other;
            if(name.equals(otherInterpreter.getName()))
                return getFileName().equals(otherInterpreter.getFileName());
        }
        return false;
    }

	public void setEnvironment(String[] strings) {
		environment = strings;
		
	}

}