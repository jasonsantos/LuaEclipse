package org.keplerproject.ldt.internal.launching;

import java.io.File;
import java.io.IOException;

public class LuaInterpreter
{

    protected File file;
    protected String name;
    public LuaInterpreter(String aName, String validFile)
    {
        if(validFile != null)
            file = new File(validFile);
        name = aName;
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
        return Runtime.getRuntime().exec(command, null, workingDirectory);
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

}