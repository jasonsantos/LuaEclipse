package org.keplerproject.ldt.core;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;


public class LuaFile
    implements LuaElement
{
    public static final String EXTENSION = "lua";
    protected IFile underlyingFile;
    public LuaFile(IFile theUnderlyingFile)
    {
        underlyingFile = theUnderlyingFile;
    }

    public IResource getUnderlyingResource()
    {
        return underlyingFile;
    }

}