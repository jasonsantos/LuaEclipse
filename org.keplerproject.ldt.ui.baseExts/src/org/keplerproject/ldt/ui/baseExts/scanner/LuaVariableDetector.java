/*******************************************************************************
*
*
*******************************************************************************/

package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.ITextViewer;


/**
 * Used to scan and detect for lua variables
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
