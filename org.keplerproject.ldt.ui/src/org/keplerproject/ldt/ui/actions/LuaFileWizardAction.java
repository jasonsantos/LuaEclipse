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
package org.keplerproject.ldt.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.keplerproject.ldt.ui.wizards.LuaFileWizard;

/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaFileWizardAction extends Action implements IWorkbenchWindowActionDelegate {
    private IWorkbenchWindow window;
    
    private static String[] POSSIBLES_VIEWS = {"org.eclipse.ui.views.ResourceNavigator","org.eclipse.jdt.ui.PackageExplorer"};

	/** Called when the action is created. */
    public void init(IWorkbenchWindow window) {
    	this.window = window;
    }

    /** Called when the action is discarded. */
    public void dispose() {
    }

    /** Called when the action is executed. */
    public void run(IAction action) {
        LuaFileWizard wizard= new LuaFileWizard();
        if(window == null)
        {
        	//XXX for a unknown reason.. wen we call the action from de popup
        	// the window field is null
        	window = PlatformUI.getWorkbench().getActiveWorkbenchWindow();
        }
        Shell shell =
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ISelection selection = null;
        for (int i =0 ; i < POSSIBLES_VIEWS.length ;i++)
        {
        	selection = window.getSelectionService().getSelection(POSSIBLES_VIEWS[i]);
        	if(selection != null) break;
        }        
        
        if(selection == null)
        	selection = new StructuredSelection();
        
        wizard.init(window.getWorkbench(),(IStructuredSelection)selection);
        WizardDialog dialog= new WizardDialog(shell, wizard);
        dialog.setTitle("Lua File Wizard");
        dialog.create();
        dialog.open();
    }

    /** Called when objects in the editor are selected or deselected. */
    public void selectionChanged(IAction action, ISelection selection) {
    }
    
}
