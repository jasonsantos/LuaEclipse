package org.keplerproject.ldt.internal.launching;

import java.io.IOException;
import java.util.Iterator;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.runtime.Platform;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.keplerproject.ldt.core.LuaProject;

public class InterpreterRunner
{

    public InterpreterRunner()
    {
    }

    public IProcess run(InterpreterRunnerConfiguration configuration, ILaunch launch)
    {
        String commandLine = renderCommandLine(configuration);
        java.io.File workingDirectory = configuration.getAbsoluteWorkingDirectory();
        Process nativeLuaProcess = null;
        try
        {
            nativeLuaProcess = configuration.getInterpreter().exec(commandLine, workingDirectory);
        }
        catch(IOException e)
        {
            throw new RuntimeException("Unable to execute interpreter: " + commandLine + workingDirectory);
        }
        IProcess process = DebugPlugin.newProcess(launch, nativeLuaProcess, renderLabel(configuration));
        process.setAttribute("org.keplerproject.ldt.core.launcher.cmdline", commandLine);
        return process;
    }

    protected String renderLabel(InterpreterRunnerConfiguration configuration)
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