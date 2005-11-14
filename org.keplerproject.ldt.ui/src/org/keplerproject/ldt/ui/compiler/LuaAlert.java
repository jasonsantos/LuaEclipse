// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 27/9/2005 10:30:05
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   LuaAlert.java

package org.keplerproject.ldt.ui.compiler;

import luajava.JavaFunction;
import luajava.LuaState;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

public class LuaAlert extends JavaFunction
{

    public LuaAlert(LuaState arg0, IResource res)
    {
        super(arg0);
        resource = res;
    }

    public int foo()
    {
        try
        {
            IMarker marker = resource.createMarker(IMarker.PROBLEM);
            
            double line = super.L.toNumber(-2);
            String error = super.L.toString(-1);
            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
            marker.setAttribute(IMarker.MESSAGE, error);
            marker.setAttribute(IMarker.LINE_NUMBER, (int)line);
            
        }
        catch(CoreException coreexception) { }
        return 0;
    }

    private IResource resource;
}