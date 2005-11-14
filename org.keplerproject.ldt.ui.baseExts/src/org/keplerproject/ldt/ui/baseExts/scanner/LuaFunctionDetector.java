/*******************************************************************************
 * Copyright (c) 2003 Ideais Tecnologia LTDA.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.rules.IWordDetector;

public class LuaFunctionDetector implements IWordDetector, ILuaSyntax {
    /* (non-Javadoc)
     * @see org.eclipse.jface.text.rules.IWordDetector#isWordStart(char)
     */
    public boolean isWordStart(char c) {
        for (int i = 0; i < functions.length; i++)
        {
            if (((String) functions[i]).charAt(0) == c) {
                return true;
            }
        }

        return false;
    }

    /* (non-Javadoc)
     * @see org.eclipse.jface.text.rules.IWordDetector#isWordPart(char)
     */
    public boolean isWordPart(char c) {
        for (int i = 0; i < functions.length; i++)
        {
            if (((String) functions[i]).indexOf(c) != -1) {
                return true;
            }
        }
        return false;
    }
}
