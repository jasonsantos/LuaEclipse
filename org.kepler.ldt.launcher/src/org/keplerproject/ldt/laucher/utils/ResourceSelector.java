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

import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Text;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public abstract class ResourceSelector
{

    protected static final String EMPTY_STRING = "";
    protected Composite composite;
    protected Button browseButton;
    protected Text textField;
    protected String browseDialogMessage;
    protected String browseDialogTitle;
    protected String validatedSelectionText;
    
    public ResourceSelector(Composite parent)
    {
        browseDialogMessage = "";
        browseDialogTitle = "";
        validatedSelectionText = "";
        composite = new Composite(parent, 0);
        GridLayout compositeLayout = new GridLayout();
        compositeLayout.marginWidth = 0;
        compositeLayout.marginHeight = 0;
        compositeLayout.numColumns = 2;
        composite.setLayout(compositeLayout);
        textField = new Text(composite, 2052);
        textField.setLayoutData(new GridData(768));
        textField.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e)
            {
                validatedSelectionText = validateResourceSelection();
            }

        });
        browseButton = new Button(composite, 8);
        browseButton.setText("Browse...");
        browseButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
                handleBrowseSelected();
            }

        });
    }

    protected abstract void handleBrowseSelected();

    protected abstract String validateResourceSelection();

    protected Shell getShell()
    {
        return composite.getShell();
    }

    public void setLayoutData(Object layoutData)
    {
        composite.setLayoutData(layoutData);
    }

    public void addModifyListener(ModifyListener aListener)
    {
        textField.addModifyListener(aListener);
    }

    public void setBrowseDialogMessage(String aMessage)
    {
        browseDialogMessage = aMessage;
    }

    public void setBrowseDialogTitle(String aTitle)
    {
        browseDialogTitle = aTitle;
    }

    public void setEnabled(boolean enabled)
    {
        composite.setEnabled(enabled);
        textField.setEnabled(enabled);
        browseButton.setEnabled(enabled);
    }

    public String getSelectionText()
    {
        return textField.getText();
    }

    public String getValidatedSelectionText()
    {
        return validatedSelectionText;
    }

    public void setSelectionText(String newText)
    {
        textField.setText(newText);
    }

}