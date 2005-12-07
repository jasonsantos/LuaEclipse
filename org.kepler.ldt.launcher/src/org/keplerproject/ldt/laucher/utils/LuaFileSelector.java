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