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

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.keplerproject.ldt.internal.launching.LuaInterpreter;
import org.keplerproject.ldt.internal.launching.LuaLaunchConfigurationAttribute;
import org.keplerproject.ldt.internal.launching.LuaRuntime;
import org.keplerproject.ldt.internal.launching.ui.preferences.EditInterpreterDialog;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaEnvironmentTab extends AbstractLaunchConfigurationTab implements LuaLaunchConfigurationAttribute
{
	protected List installedInterpretersWorkingCopy;  
    protected Combo interpreterCombo;
    protected Text enVar;


    public LuaEnvironmentTab()
    {
    }

    public void createControl(Composite parent)
    {
        Composite composite = createPageRoot(parent);
        (new Label(composite, 0)).setText("&Interpreter:");
        interpreterCombo = new Combo(composite, 8);
        interpreterCombo.setLayoutData(new GridData(768));
        initializeInterpreterCombo(interpreterCombo);
        interpreterCombo.addModifyListener(getEnvironmentModifyListener());
        Button interpreterAddButton = new Button(composite, 8);
        interpreterAddButton.setText("N&ew...");
        interpreterAddButton.addSelectionListener(new SelectionAdapter() {

            public void widgetSelected(SelectionEvent evt)
            {
                LuaInterpreter newInterpreter = new LuaInterpreter(null, null);
                EditInterpreterDialog editor = new EditInterpreterDialog(getShell(), "Add Interpreter");
                editor.create();
                editor.setInterpreterToEdit(newInterpreter);
                if(editor.open() == 0)
                {
                    LuaRuntime.getDefault().addInstalledInterpreter(newInterpreter);
                    interpreterCombo.add(newInterpreter.getName());
                    interpreterCombo.select(interpreterCombo.indexOf(newInterpreter.getName()));
                }
            }

        });
        (new Label(composite, 0)).setText("&Variables (line separated):");
        enVar = new Text(composite,SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL | SWT.BORDER);
        GridData gd = new GridData(768);
        gd.heightHint = 200;
        gd.minimumHeight=150;
        enVar.setLayoutData(gd);
        
        enVar.addModifyListener(getEnvironmentModifyListener());
           
    }

    protected void initializeLuaInitText(ILaunchConfiguration configuration) {
    	String enVarsStr = null;
        try
        {
        	enVarsStr = configuration.getAttribute(ENVIRONMENT_VARS, "");
        }
        catch(CoreException e)
        {
            log(e);
        }
        if(enVarsStr != null && !enVarsStr.equals(""))
        {
        	this.enVar.setText(enVarsStr);
        }
	}

	protected ModifyListener getEnvironmentModifyListener()
    {
        return new ModifyListener() {

            public void modifyText(ModifyEvent evt)
            {
                updateLaunchConfigurationDialog();
            }

        };
    }

    public void setDefaults(ILaunchConfigurationWorkingCopy ilaunchconfigurationworkingcopy)
    {
    }

    public void initializeFrom(ILaunchConfiguration configuration)
    {
        initializeInterpreterSelection(configuration);
        initializeLuaInitText(configuration);
    }

    protected void initializeInterpreterSelection(ILaunchConfiguration configuration)
    {
        String interpreterName = null;
        try
        {
            interpreterName = configuration.getAttribute(SELECTED_INTERPRETER, "");
        }
        catch(CoreException e)
        {
            log(e);
        }
        if(interpreterName != null && !interpreterName.equals(""))
            interpreterCombo.select(interpreterCombo.indexOf(interpreterName));
    }

    protected void initializeInterpreterCombo(Combo interpreterCombo)
    {
        installedInterpretersWorkingCopy = new ArrayList();
        installedInterpretersWorkingCopy.addAll(LuaRuntime.getDefault().getInstalledInterpreters());
        String interpreterNames[] = new String[installedInterpretersWorkingCopy.size()];
        for(int interpreterIndex = 0; interpreterIndex < installedInterpretersWorkingCopy.size(); interpreterIndex++)
        {
            LuaInterpreter interpreter = (LuaInterpreter)installedInterpretersWorkingCopy.get(interpreterIndex);
            interpreterNames[interpreterIndex] = interpreter.getName();
        }

        interpreterCombo.setItems(interpreterNames);
        LuaInterpreter selectedInterpreter = LuaRuntime.getDefault().getSelectedInterpreter();
        if(selectedInterpreter != null)
            interpreterCombo.select(interpreterCombo.indexOf(selectedInterpreter.getName()));
    }

    public void performApply(ILaunchConfigurationWorkingCopy configuration)
    {
        int selectionIndex = interpreterCombo.getSelectionIndex();
        if(selectionIndex >= 0)
            configuration.setAttribute(SELECTED_INTERPRETER, interpreterCombo.getItem(selectionIndex));
        
        String envVarsstr = enVar.getText();
        if(envVarsstr ==null )
        	configuration.setAttribute(ENVIRONMENT_VARS,"");
        else
        	configuration.setAttribute(ENVIRONMENT_VARS, envVarsstr);
    }

    protected Composite createPageRoot(Composite parent)
    {
        Composite composite = new Composite(parent, 0);
        GridLayout layout = new GridLayout();
        layout.marginWidth = 0;
        composite.setLayout(layout);
        setControl(composite);
        return composite;
    }

    public String getName()
    {
        return "Environment";
    }

    public boolean isValid(ILaunchConfiguration launchConfig)
    {
        try
        {
            String selectedInterpreter = launchConfig.getAttribute(SELECTED_INTERPRETER, "");
            if(selectedInterpreter.length() == 0)
            {
                setErrorMessage("No interpreter has been selected");
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