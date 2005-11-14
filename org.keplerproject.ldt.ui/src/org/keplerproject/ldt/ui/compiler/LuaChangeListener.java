/***********************************
 * 
 * 
 * Thanks to Danilo Tuler
 ************************************/

package org.keplerproject.ldt.ui.compiler;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;

/**
 * A resource listener to the lua files. When the lua script is saved, the 
 * script is compiled and the compilation errors are created at the Problems View 
 * like IMarkes.
 * @author guilherme
 * @version 1.0
 */
public class LuaChangeListener
    implements IResourceChangeListener
{

    public LuaChangeListener()
    { }
    public void resourceChanged(IResourceChangeEvent event)
    {
        try
        {
        	// registry the delta visitor to check the 
            event.getDelta().accept(new LuaResourceDeltaVisitor());
        }
        catch(CoreException coreexception) { }
    }
}