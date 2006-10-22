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
        Shell shell =
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        ISelection selection = null;
        for (int i =0 ; i < POSSIBLES_VIEWS.length ;i++)
        {
        	selection = window.getSelectionService().getSelection(POSSIBLES_VIEWS[i]);
        	if(selection != null) break;
        }        
        
        //IStructuredSelection selection = (IStructuredSelection)window.getSelectionService().getSelection("org.eclipse.jdt.ui.ProjectsView");
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
