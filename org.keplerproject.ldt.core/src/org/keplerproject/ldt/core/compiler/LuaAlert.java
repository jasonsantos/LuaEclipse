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
package org.keplerproject.ldt.core.compiler;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;

/**
 * Lua error parser
 *  creates error markers on workspace given lua error messages
 *  
 * @author Jason Santos
 * @version $Id$
 */
public class LuaAlert 
{

	private IResource resource;
    public LuaAlert(IResource res)
    {
        resource = res;
    }

    public int reportLuaError(String luaerror)
    {
        try
        {
            IMarker marker = resource.createMarker(IMarker.PROBLEM);            
            Pattern p = Pattern.compile(".*:(\\d+):(.*)$");
			
			Matcher m = p.matcher(luaerror);
			m.matches();
			if(m.matches()) {
				int line = Integer.parseInt(m.group(1));
				String errorMsg = m.group(2);
	            marker.setAttribute(IMarker.SEVERITY, IMarker.SEVERITY_ERROR);
	            marker.setAttribute(IMarker.MESSAGE, errorMsg);
	            marker.setAttribute(IMarker.LINE_NUMBER, (int)line);				
			} else {
				 // assert false : "Invalid format for lua error message ";
			}
            
        }
        catch(CoreException coreexception) { }
        return 0;
    }
}