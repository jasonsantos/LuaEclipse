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
 *
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