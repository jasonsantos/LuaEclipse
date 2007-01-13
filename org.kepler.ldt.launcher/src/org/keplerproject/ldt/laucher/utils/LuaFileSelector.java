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

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.jface.util.Assert;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.model.WorkbenchLabelProvider;

/**
 * resource selector to Lua Files.
 *  Used at the "Browse" button at 
 * launch configurations
 * @author guilherme
 * @version $Id$
 *
 */
public class LuaFileSelector extends ResourceSelector
{

    public LuaFileSelector(Composite parent, LuaProjectSelector aProjectSelector)
    {
        super(parent);
        Assert.isNotNull(aProjectSelector);
        luaProjectSelector = aProjectSelector;
        super.browseDialogTitle = "File Selection";
    }

    protected Object[] getLuaFiles()
    {
        org.eclipse.core.resources.IProject luaProject = luaProjectSelector.getSelection();
        if(luaProject == null)
            return new Object[0];
        LuaElementVisitor visitor = new LuaElementVisitor();
        try
        {
            luaProject.accept(visitor);
        }
        catch(CoreException coreexception) { }
        return visitor.getCollectedLuaFiles();
    }

    public IFile getSelection()
    {
        String fileName = getSelectionText();
        if(fileName != null && !fileName.equals(""))
        {
            IPath filePath = new Path(fileName);
            org.eclipse.core.resources.IProject project = luaProjectSelector.getSelection();
            if(project != null && project.exists(filePath))
                return project.getFile(filePath);
        }
        return null;
    }

    protected void handleBrowseSelected()
    {
        ElementListSelectionDialog dialog = new ElementListSelectionDialog(getShell(), new WorkbenchLabelProvider());
        dialog.setTitle(super.browseDialogTitle);
        dialog.setMessage(super.browseDialogMessage);
        dialog.setElements(getLuaFiles());
        if(dialog.open() == 0)
            super.textField.setText(((IResource)dialog.getFirstResult()).getProjectRelativePath().toString());
    }

    protected String validateResourceSelection()
    {
        IFile selection = getSelection();
        return selection != null ? selection.getProjectRelativePath().toString() : "";
    }

    protected LuaProjectSelector luaProjectSelector;
}