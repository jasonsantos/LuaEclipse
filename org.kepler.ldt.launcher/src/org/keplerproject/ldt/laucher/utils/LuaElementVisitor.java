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
package org.keplerproject.ldt.laucher.utils;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IResourceVisitor;
import org.eclipse.core.runtime.CoreException;
import org.keplerproject.ldt.core.LuaScriptsSpecs;
/**
 * Resource Visitor to the Lua Elements
 * @author guilherme
 * @version $Id$
 */
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
            if(LuaScriptsSpecs.getDefault().isValidLuaScriptFileName(fileResource))
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