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
package org.keplerproject.ldt.internal.launching.ui.preferences;

import org.eclipse.core.runtime.IStatus;
import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.layout.RowData;
import org.eclipse.swt.layout.RowLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
import org.keplerproject.ldt.internal.launching.LuaInterpreter;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class EditInterpreterDialog extends Dialog
{
	protected LuaInterpreter interpreterToEdit;

	protected Text interpreterNameText;

	protected Text interpreterLocationText;
	
	protected Text interpreterInitFile;

	protected IStatus allStatus[];

    public EditInterpreterDialog(Shell parentShell, String aDialogTitle)
    {
        super(parentShell);
        allStatus = new IStatus[2];
    }

    public void setInterpreterToEdit(LuaInterpreter anInterpreter)
    {
        interpreterToEdit = anInterpreter;
        String interpreterName = interpreterToEdit.getName();
        interpreterNameText.setText(interpreterName == null ? "" : interpreterName);
        String installLocation = interpreterToEdit.getFileName();
        interpreterLocationText.setText(installLocation == null ? "" : installLocation);
       // interpreterInitFile.setText();
    }

    protected void createLocationEntryField(Composite composite)
    {
        (new Label(composite, 0)).setText("Path:");
        Composite locationComposite = new Composite(composite, 0);
        RowLayout locationLayout = new RowLayout();
        locationLayout.marginLeft = 0;
        locationComposite.setLayout(locationLayout);
        interpreterLocationText = new Text(locationComposite, 2052);
        interpreterLocationText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e)
            {
                allStatus[1] = validateInterpreterLocationText();
                updateStatusLine();
            }

        });
        interpreterLocationText.setLayoutData(new RowData(200, -1));
        Button browseButton = new Button(composite, 8);
        browseButton.setLayoutData(new GridData(128));
        browseButton.setText("Browse...");
        browseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
                browseForInstallDir();
            }

        });
    }

    protected void updateStatusLine()
    {
    }

    protected IStatus getMostSevereStatus()
    {
        return null;
    }

    protected IStatus validateInterpreterLocationText()
    {
        return null;
    }

    protected void createNameEntryField(Composite composite)
    {
        (new Label(composite, 0)).setText("Name:");
        GridData gridData = new GridData(256);
        gridData.horizontalSpan = 2;
        interpreterNameText = new Text(composite, 2052);
        interpreterNameText.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e)
            {
                allStatus[0] = validateInterpreterNameText();
                updateStatusLine();
            }

        });
        interpreterNameText.setLayoutData(gridData);
    }

    protected IStatus validateInterpreterNameText()
    {
        /*int status = 0;
        String message = "";
        if(interpreterNameText.getText() == null || interpreterNameText.getText().length() <= 0)
        {
            status = 4;
            message = "Error";
        }*/
        return null;
    }

    protected void browseForInstallDir()
    {
        FileDialog dialog = new FileDialog(getShell());
        dialog.setFilterPath(interpreterLocationText.getText());
        String newPath = dialog.open();
        if(newPath != null)
            interpreterLocationText.setText(newPath);
    }

    protected void okPressed()
    {
        if(interpreterToEdit == null)
            interpreterToEdit = new LuaInterpreter(null, null);
        interpreterToEdit.setName(interpreterNameText.getText());
        interpreterToEdit.setFileName(interpreterLocationText.getText());
        super.okPressed();
    }

    protected Control createDialogArea(Composite parent)
    {
        Composite composite = (Composite)super.createDialogArea(parent);
        GridLayout layout = new GridLayout();
        layout.numColumns = 3;
        composite.setLayout(layout);
        createNameEntryField(composite);
        createLocationEntryField(composite);
        return composite;
    }

}