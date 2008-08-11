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

import java.lang.reflect.Constructor;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.internal.text.html.HTMLTextPresenter;
import org.eclipse.jface.text.*;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Shell;
import org.keplerproject.ldt.core.luadoc.*;
import org.keplerproject.ldt.ui.text.lua.*;

/**
 * The lua source Hover Text. This class provides text to 
 * be displayed when the user hovers his mouse pointer 
 * on a Lua identifier.
 * 
 * @author Jason Santos
 * @since 1.2
 * @version $Id$
 * 
 */

public class LuaTextHover implements ITextHover, IInformationProviderExtension2 {

	/* (non-Javadoc)
	 * Method declared on ITextHover
	 */
	public String getHoverInfo(ITextViewer textViewer, IRegion hoverRegion) {
		if (hoverRegion != null) {
			try {
				
				if (hoverRegion.getLength() > -1) {
					//	TODO: determine exactly which code element is this instead of just grabbing the word 
					
					IRegion hoverWord = LuaWordFinder.findWord(textViewer.getDocument(), hoverRegion.getOffset());
					
					String token = textViewer.getDocument().get(hoverWord.getOffset(), hoverWord.getLength());
					
					// TODO: obtain from a central engine the right documentation for this code element
					LuadocGenerator lg = LuadocGenerator.getInstance();
					String documentationText = lg.getDocumentationText(token);
					//String documentationText = token;
					
					return documentationText;
				}
				
				return textViewer.getDocument().get(hoverRegion.getOffset(), hoverRegion.getLength());
				
			} catch (BadLocationException x) {
			}
		}
		return ""; //$NON-NLS-1$
	}
	
	/* (non-Javadoc)
	 * Method declared on ITextHover
	 */
	public IRegion getHoverRegion(ITextViewer textViewer, int offset) {
		Point selection= textViewer.getSelectedRange();
		if (selection.x <= offset && offset < selection.x + selection.y)
			return new Region(selection.x, selection.y);
		return new Region(offset, 0);
	}

	/*
    * @see IInformationProviderExtension2#getInformationPresenterControlCreator()
    * @since 3.1
    */
    public IInformationControlCreator getInformationPresenterControlCreator() {
    	return new IInformationControlCreator() {
    		@SuppressWarnings("restriction")
			public IInformationControl createInformationControl(Shell parent) {
    			int shellStyle= SWT.RESIZE | SWT.TOOL;
                int style= SWT.V_SCROLL | SWT.H_SCROLL;
                if (BrowserInformationControl.isAvailable(parent))
                	 try {
                 		 Class BI = Class.forName("BrowserInformationControl");
                 		 Class params[] = {Shell.class, Integer.class, Integer.class};
                 		 Constructor c = BI.getConstructor(params);
                 		 return (IInformationControl) c.newInstance(parent, shellStyle, style);
                 	 } catch(Exception e) {
                 		return new DefaultInformationControl(parent, shellStyle, style, new HTMLTextPresenter(false));
                 	 }
                else
                	return new DefaultInformationControl(parent, shellStyle, style, new HTMLTextPresenter(false));
    		}
    	};
    }
}
