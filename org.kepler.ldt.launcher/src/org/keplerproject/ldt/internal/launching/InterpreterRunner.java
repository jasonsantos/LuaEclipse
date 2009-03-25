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

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.keplerproject.ldt.core.LuaProject;
/**
 * 
 * Class that launch the lua intepreter
 * @author guilherme
 * @version $Id$
 */
public class InterpreterRunner
{

    public InterpreterRunner()
    {
    }
    /**
     * Executes a lua process configured at the lua runners configuration
     * @param configuration
     * @param launch
     * @return
     * @throws CoreException 
     */
    public IProcess run(InterpreterRunnerConfiguration configuration, ILaunch launch) throws CoreException
    {
        String commandLine = renderCommandLine(configuration);
        java.io.File workingDirectory = configuration.getAbsoluteWorkingDirectory();
        LuaInterpreter luaInterpreter =null;
        Process nativeLuaProcess = null;
        try
        {
      
        	luaInterpreter =configuration.getInterpreter();
            nativeLuaProcess = luaInterpreter.exec(commandLine, workingDirectory);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Unable to execute interpreter: " + commandLine + workingDirectory);
        }
        IProcess process = DebugPlugin.newProcess(launch, nativeLuaProcess, renderLabel(configuration));
        process.setAttribute("org.keplerproject.ldt.core.launcher.cmdline", commandLine);
        return process;
    }

    protected String renderLabel(InterpreterRunnerConfiguration configuration) throws CoreException
    {
        LuaInterpreter interpreter = configuration.getInterpreter();
        return interpreter.getCommand() + " " + configuration.getFileName();
    }

    protected String renderCommandLine(InterpreterRunnerConfiguration configuration)
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append(getDebugCommandLineArgument());
        buffer.append(" " + configuration.getInterpreterArguments());
        buffer.append(osDependentPath(configuration.getAbsoluteFileName()));
        buffer.append(" " + configuration.getProgramArguments());
        return buffer.toString();
    }

    protected String renderLoadPath(InterpreterRunnerConfiguration configuration)
    {
        StringBuffer loadPath = new StringBuffer();
        LuaProject project = configuration.getProject();
        addToLoadPath(loadPath, project.getProject());
        for(Iterator referencedProjects = project.getReferencedProjects().iterator(); referencedProjects.hasNext(); addToLoadPath(loadPath, (IProject)referencedProjects.next()));
        return loadPath.toString();
    }

    protected void addToLoadPath(StringBuffer loadPath, IProject project)
    {
        loadPath.append(" -I " + osDependentPath(project.getLocation().toOSString()));
    }

    protected String osDependentPath(String aPath)
    {
        if(Platform.getOS().equals("win32"))
            aPath = "\"" + aPath + "\"";
        return aPath;
    }

    protected String getDebugCommandLineArgument()
    {
        return "";
    }
}