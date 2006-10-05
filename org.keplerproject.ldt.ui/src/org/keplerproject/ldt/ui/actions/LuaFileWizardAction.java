package org.keplerproject.ldt.ui.actions;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.WizardDialog;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IWorkbenchWindow;
import org.eclipse.ui.IWorkbenchWindowActionDelegate;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.views.navigator.ResourceSelectionUtil;
import org.keplerproject.ldt.ui.wizards.LuaFileWizard;


public class LuaFileWizardAction extends Action implements IWorkbenchWindowActionDelegate {
    /** Called when the action is created. */
    public void init(IWorkbenchWindow window) {
    }

    /** Called when the action is discarded. */
    public void dispose() {
    }

    /** Called when the action is executed. */
    public void run(IAction action) {
        LuaFileWizard wizard= new LuaFileWizard();
        Shell shell =
          PlatformUI.getWorkbench().getActiveWorkbenchWindow().getShell();
        IStructuredSelection selection = (IStructuredSelection)PlatformUI.getWorkbench().getActiveWorkbenchWindow().getSelectionService().getSelection();
        wizard.init(PlatformUI.getWorkbench(),ResourceSelectionUtil.allResources(selection,0));
        WizardDialog dialog= new WizardDialog(shell, wizard);
        dialog.setTitle("Lua File Wizard");
        dialog.create();
        dialog.open();
    }

    /** Called when objects in the editor are selected or deselected. */
    public void selectionChanged(IAction action, ISelection selection) {
    }
    
}
