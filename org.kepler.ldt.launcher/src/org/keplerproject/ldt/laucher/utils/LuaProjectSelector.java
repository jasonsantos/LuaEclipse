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
package org.keplerproject.ldt.laucher.utils;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;
import org.keplerproject.ldt.core.LuaCorePlugin;

/**
 * Resource Selector to Lua Project. Used at the "Browse" button at 
 * launch configurations
 * @author guilherme
 * @version $Id$
 */
public class LuaProjectSelector extends ResourceSelector
{

    public LuaProjectSelector(Composite parent)
    {
        super(parent);
        super.browseDialogTitle = "Project Selection";
    }

    public IProject getSelection()
    {
        String projectName = getSelectionText();
        if(projectName != null && !projectName.equals(""))
            return ResourcesPlugin.getWorkspace().getRoot().getProject(projectName);
        else
            return null;
    }

    protected void handleBrowseSelected()
    {
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new WorkbenchLabelProvider());
        dialog.setTitle(super.browseDialogTitle);
        dialog.setMessage(super.browseDialogMessage);
        dialog.setElements(LuaCorePlugin.getLuaProjects());
        if(dialog.open() == 0)
            super.textField.setText(((IProject)dialog.getFirstResult()).getName());
    }

    protected String validateResourceSelection()
    {
        IProject project = getSelection();
        return project != null ? project.getName() : "";
    }
}