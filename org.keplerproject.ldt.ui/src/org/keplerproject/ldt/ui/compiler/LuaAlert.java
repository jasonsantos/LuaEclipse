package org.keplerproject.ldt.ui.compiler;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.keplerproject.luajava.JavaFunction;
import org.keplerproject.luajava.LuaState;

public class LuaAlert extends JavaFunction 
{

	private IResource resource;
    public LuaAlert(LuaState arg0, IResource res)
    {
        super(arg0);
        resource = res;
    }

    public int execute()
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
}