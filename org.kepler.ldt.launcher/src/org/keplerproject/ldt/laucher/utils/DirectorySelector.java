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

import java.io.File;

import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.DirectoryDialog;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
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