package org.keplerproject.ldt.laucher.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;

public class LuaElementVisitor
    implements IResourceVisitor
{

    public LuaElementVisitor()
    {
        luaFiles = new ArrayList();
    }

    public boolean visit(IResource resource)
        throws CoreException
    {
        switch(resource.getType())
        {
        case 4: // '\004'
            return true;

        case 2: // '\002'
            return true;

        case 1: // '\001'
            IFile fileResource = (IFile)resource;
            if("lua".equals(fileResource.getFileExtension()))
            {
                luaFiles.add(fileResource);
                return true;
            }
            // fall through

        case 3: // '\003'
        default:
            return false;
        }
    }

    public Object[] getCollectedLuaFiles()
    {
        return luaFiles.toArray();
    }

    protected List luaFiles;
}