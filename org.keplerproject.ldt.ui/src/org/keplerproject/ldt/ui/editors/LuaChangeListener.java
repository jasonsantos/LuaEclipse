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

package org.keplerproject.ldt.ui.editors;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.runtime.CoreException;
import org.keplerproject.ldt.core.compiler.LuaResourceDeltaVisitor;


/**
 * A resource listener to the lua files. When the lua script is saved, the 
 * script is compiled and the compilation errors are created at the Problems View 
 * like IMarkes.
 * Thanks to Danilo Tuler
 * @author guilherme
 * @version $Id$
 */
public class LuaChangeListener
    implements IResourceChangeListener
{

   LuaEditor editor;
	public LuaChangeListener(LuaEditor editor)
    { 
    	this.editor = editor;
    }
    public void resourceChanged(IResourceChangeEvent event)
    {
    	try{
        	// registry the delta visitor to check the 
            event.getDelta().accept(new LuaResourceDeltaVisitor());
        }
        catch(CoreException coreexception) { }
    }
}