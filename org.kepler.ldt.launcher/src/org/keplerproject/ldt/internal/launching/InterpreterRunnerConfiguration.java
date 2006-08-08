package org.keplerproject.ldt.internal.launching;

import java.io.File;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.keplerproject.ldt.core.LuaProject;


public class InterpreterRunnerConfiguration implements LuaLaunchConfigurationAttribute
{
    protected ILaunchConfiguration configuration;
    public InterpreterRunnerConfiguration(ILaunchConfiguration aConfiguration)
    {
        configuration = aConfiguration;
    }

    public String getAbsoluteFileName()
    {
        IProject project = getProject().getProject();
        return project.getLocation().toOSString() + File.separator + getFileName();
    }

    public String getFileName()
    {
        String fileName = "";
        try
        {
            fileName = configuration.getAttribute(FILE_NAME, "No file specified in configuration");
        }
        catch(CoreException coreexception) { }
        return fileName;
    }

    public LuaProject getProject()
    {
        String projectName = "";
        try
        {
            projectName = configuration.getAttribute(PROJECT_NAME, "");
        }
        catch(CoreException coreexception) { }
        org.eclipse.core.resources.IProject project = ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        LuaProject luaProject = new LuaProject();
        luaProject.setProject(project);
        return luaProject;
    }

    public File getAbsoluteWorkingDirectory()
    {
        String file = null;
        try
        {
            file = configuration.getAttribute(WORKING_DIRECTORY, "");
        }
        catch(CoreException coreexception) { }
        return new File(file);
    }

    public String getInterpreterArguments()
    {
        try
        {
            return configuration.getAttribute(INTERPRETER_ARGUMENTS, "");
        }
        catch(CoreException coreexception)
        {
            return "";
        }
    }

    public String getProgramArguments()
    {
        try
        {
            return configuration.getAttribute(PROGRAM_ARGUMENTS, "");
        }
        catch(CoreException coreexception)
        {
            return "";
        }
    }

    public LuaInterpreter getInterpreter()
    {
        String selectedInterpreter = null;
        try
        {
            selectedInterpreter = configuration.getAttribute(SELECTED_INTERPRETER, "");
        }
        catch(CoreException coreexception) { }
        return LuaRuntime.getDefault().getInterpreter(selectedInterpreter);
    }


}