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
package org.keplerproject.ldt.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.keplerproject.ldt.ui.LDTUIPlugin;
/**
 * Lua Preference page Extension. The Blank root page.
 * @author guilherme
 * @version $Id$
 */
public class LuaPreferencePage extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage
{

    public LuaPreferencePage()
    {
        super(1);
        setPreferenceStore(LDTUIPlugin.getDefault().getPreferenceStore());
        setDescription(" Lua General Properties");
        initializeDefaults();
    }

    private void initializeDefaults()
    {
       // org.eclipse.jface.preference.IPreferenceStore store = getPreferenceStore();
    }

    public void init(IWorkbench iworkbench)
    {
    }

    protected void createFieldEditors()
    {
    }
}