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
package org.keplerproject.ldt.internal.launching.ui;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.keplerproject.ldt.internal.launching.LuaLaunchConfigurationAttribute;
import org.keplerproject.ldt.laucher.utils.DirectorySelector;

/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaArgumentsTab extends AbstractLaunchConfigurationTab implements LuaLaunchConfigurationAttribute
{
	

    protected Text interpreterArgsText;
    protected Text programArgsText;
    protected DirectorySelector workingDirectorySelector;
    protected Button useDefaultWorkingDirectoryButton;

    public LuaArgumentsTab()
    {
    }

    public void createControl(Composite parent)
    {
        Composite composite = createPageRoot(parent);
        (new Label(composite, 0)).setText("Working Directory:");
        workingDirectorySelector = new DirectorySelector(composite);
        workingDirectorySelector.setBrowseDialogMessage("Select a working directory for the launch configuration");
        workingDirectorySelector.setLayoutData(new GridData(768));
        workingDirectorySelector.addModifyListener(new ModifyListener() {

            public void modifyText(ModifyEvent e)
            {
                updateLaunchConfigurationDialog();
            }

        });
        Composite defaultWorkingDirectoryComposite = new Composite(composite, 0);
        GridLayout layout = new GridLayout();
        layout.numColumns = 2;
        defaultWorkingDirectoryComposite.setLayout(layout);
        useDefaultWorkingDirectoryButton = new Button(defaultWorkingDirectoryComposite, 32);
        useDefaultWorkingDirectoryButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent e)
            {
                setUseDefaultWorkingDirectory(((Button)e.getSource()).getSelection());
            }

        });
        (new Label(defaultWorkingDirectoryComposite, 0)).setText("Use default working directory");
        defaultWorkingDirectoryComposite.pack();
       // Label verticalSpacer = new Label(composite, 0);
        (new Label(composite, 0)).setText("Interpreter Arguments:");
        interpreterArgsText = new Text(composite, 2562);
        interpreterArgsText.setLayoutData(new GridData(1808));
        (new Label(composite, 0)).setText("Program Arguments:");
        programArgsText = new Text(composite, 2562);
        programArgsText.setLayoutData(new GridData(1808));
    }

    protected void setUseDefaultWorkingDirectory(boolean useDefault)
    {
        if(useDefaultWorkingDirectoryButton.getSelection() != useDefault)
            useDefaultWorkingDirectoryButton.setSelection(useDefault);
        if(useDefault)
            workingDirectorySelector.setSelectionText(ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
        workingDirectorySelector.setEnabled(!useDefault);
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy configuration)
    {
        configuration.setAttribute(USE_DEFAULT_WORKING_DIRECTORY, true);
        configuration.setAttribute(WORKING_DIRECTORY, ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
        configuration.setAttribute(ILaunchConfiguration.ATTR_SOURCE_LOCATOR_ID, LuaSourceLocator.ID);
    }

    public void initializeFrom(ILaunchConfiguration configuration)
    {
        String workingDirectory = "";
        String interpreterArgs = "";
        String programArgs = "";
        boolean useDefaultWorkDir = true;
        try
        {
            workingDirectory = configuration.getAttribute(WORKING_DIRECTORY, "");
            interpreterArgs = configuration.getAttribute(INTERPRETER_ARGUMENTS, "");
            programArgs = configuration.getAttribute(PROGRAM_ARGUMENTS, "");
            useDefaultWorkDir = configuration.getAttribute(USE_DEFAULT_WORKING_DIRECTORY, true);
        }
        catch(CoreException e)
        {
            log(e);
        }
        workingDirectorySelector.setSelectionText(workingDirectory);
        interpreterArgsText.setText(interpreterArgs);
        programArgsText.setText(programArgs);
        setUseDefaultWorkingDirectory(useDefaultWorkDir);
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration)
    {
        configuration.setAttribute(WORKING_DIRECTORY, workingDirectorySelector.getValidatedSelectionText());
        configuration.setAttribute(INTERPRETER_ARGUMENTS, interpreterArgsText.getText());
        configuration.setAttribute(PROGRAM_ARGUMENTS, programArgsText.getText());
        configuration.setAttribute(USE_DEFAULT_WORKING_DIRECTORY, useDefaultWorkingDirectoryButton.getSelection());
    }

    protected Composite createPageRoot(Composite parent)
    {
        Composite composite = new Composite(parent, 0);
        GridLayout compositeLayout = new GridLayout();
        compositeLayout.marginWidth = 0;
        compositeLayout.numColumns = 1;
        composite.setLayout(compositeLayout);
        setControl(composite);
        return composite;
    }

    public String getName()
    {
        return "Arguments";
    }

    public boolean isValid(ILaunchConfiguration launchConfig)
    {
        try
        {
            String workingDirectory = launchConfig.getAttribute(WORKING_DIRECTORY, "");
            if(workingDirectory.length() == 0)
            {
                setErrorMessage("Invalid working directory");
                return false;
            }
        }
        catch(CoreException e)
        {
            log(e);
        }
        setErrorMessage(null);
        return true;
    }

    protected void log(Throwable throwable)
    {
    }

    public Image getImage()
    {
        return null;
    }


}