package org.keplerproject.ldt.laucher.utils;

import java.io.File;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;

public class DirectorySelector extends ResourceSelector
{

    public DirectorySelector(Composite parent)
    {
        super(parent);
    }

    protected void handleBrowseSelected()
    {
        DirectoryDialog dialog = new DirectoryDialog(getShell());
        dialog.setMessage(super.browseDialogMessage);
        String currentWorkingDir = super.textField.getText();
        if(!currentWorkingDir.trim().equals(""))
        {
            File path = new File(currentWorkingDir);
            if(path.exists())
                dialog.setFilterPath(currentWorkingDir);
        }
        String selectedDirectory = dialog.open();
        if(selectedDirectory != null)
            super.textField.setText(selectedDirectory);
    }

    protected String validateResourceSelection()
    {
        String directory = super.textField.getText();
        File directoryFile = new File(directory);
        if(directoryFile.exists() && directoryFile.isDirectory())
            return directory;
        else
            return "";
    }
}