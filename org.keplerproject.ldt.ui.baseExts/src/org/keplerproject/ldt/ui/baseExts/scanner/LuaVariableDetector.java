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


package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;


/**
 * Used to scan and detect for lua variables
 * @author guilherme
 * @version $Id$
 */
public class LuaVariableDetector {
    String wordPart = "";
    String variable = null;
    int docOffset;

    /**
     * Method VariableDetector.
     * @param viewer is a text viewer
     * @param documentOffset into the document
     */
    public LuaVariableDetector(ITextViewer viewer, int documentOffset) {
        docOffset = documentOffset - 1;

        try {
            //scan to find a variable name
            //this is not fully correct, according to lua grammar
            while (docOffset >= viewer.getTopIndexStartOffset() &&
            		(Character.isJavaIdentifierPart(viewer.getDocument().getChar(docOffset)) || 
            		viewer.getDocument().getChar(docOffset) == '.')) {
            	docOffset--;
            }

            //we've been one step too far : increase the offset
            docOffset++;
            wordPart = viewer.getDocument().get(docOffset,
                    documentOffset - docOffset);

            int dot = wordPart.lastIndexOf('.');
            int bracket = wordPart.lastIndexOf('[');

            if (wordPart.lastIndexOf(']') > bracket) {
                bracket = -1;
            }

            if ((dot >= 0) || (bracket >= 0)) {
                if (dot > bracket) {
                    variable = wordPart.substring(0, dot);
                    wordPart = wordPart.substring(dot + 1, wordPart.length());
                    docOffset += (dot + 1);
                } else {
                    variable = wordPart.substring(0, bracket);
                    wordPart = wordPart.substring(bracket + 1, wordPart.length());
                    docOffset += (bracket + 1);
                }
            }
        } catch (BadLocationException e) {
            // do nothing
        }
    }

    /**
     * Method getString.
     * @return String
     */
    public String getString() {
        return wordPart;
    }

    public String getVariable() {
        return variable;
    }

    public int getOffset() {
        return docOffset;
    }
}
