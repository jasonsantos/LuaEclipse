/**
 * 
 */
package org.keplerproject.ldt.debug.ui.launcher;

import java.util.Map;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.DebugUITools;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.dialogs.IDialogConstants;
import org.eclipse.jface.dialogs.MessageDialogWithToggle;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;
import org.keplerproject.ldt.debug.ui.LuaDebugUIPlugin;

/**
 * @author jasonsantos
 */
public class LuaLaunchShortcut implements ILaunchShortcut {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.jface.viewers.ISelection,
	 *      java.lang.String)
	 */
	public void launch(ISelection selection, String mode) {
		// must be a structured selection with one file selected
		IFile file = (IFile) ((IStructuredSelection) selection)
				.getFirstElement();

		// check for an existing launch config for the pda file
		String path = file.getFullPath().toString();
		String proj = file.getProject().getName();
		ILaunchManager launchManager = DebugPlugin.getDefault()
				.getLaunchManager();
		ILaunchConfigurationType type = launchManager
				.getLaunchConfigurationType(LuaDebuggerPlugin.ID_LUA_LAUNCH_CONFIGURATION_TYPE);
		try {
			ILaunchConfiguration[] configurations = launchManager
					.getLaunchConfigurations(type);
			for (int i = 0; i < configurations.length; i++) {
				ILaunchConfiguration configuration = configurations[i];
				String projectName = configuration.getAttribute(
						LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE, (String) null);
				String scriptName = configuration.getAttribute(
						LuaDebuggerPlugin.LUA_SCRIPT_ATTRIBUTE, (String) null);
				if (path.equals(scriptName) && proj.equals(projectName)) {
					verifyPerspectiveAndLaunch(configuration, mode);
					return;
				}
			}
		} catch (CoreException e) {
			return;
		}

		try {
			// create a new configuration for the Lua file
			ILaunchConfigurationWorkingCopy workingCopy = type.newInstance(
					null, file.getName());

			workingCopy.setAttribute(LuaDebuggerPlugin.LUA_SCRIPT_ATTRIBUTE,
					path);

			workingCopy.setAttribute(LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE,
					file.getProject().getName());

			workingCopy.setMappedResources(new IResource[] { file });
			ILaunchConfiguration configuration = workingCopy.doSave();
			
			verifyPerspectiveAndLaunch(configuration, mode);
		} catch (CoreException e1) {
		}
	}
	
	private void verifyPerspectiveAndLaunch(ILaunchConfiguration configuration, String mode)
	{
		String debugId = "org.eclipse.debug.ui.DebugPerspective";
		IWorkbenchWindow window = LuaDebugUIPlugin.getDefault().getWorkbench().getActiveWorkbenchWindow();
		String currentId = window.getActivePage().getPerspective().getId();
		if (!debugId.equals(currentId))
		{
			IPreferenceStore store = LuaDebugUIPlugin.getDefault().getPreferenceStore();
			MessageDialogWithToggle dialog = MessageDialogWithToggle.openYesNoQuestion(
					LuaDebugUIPlugin.getShell(), "Switch to Debug Perspective?", 
					"This action is associated with the debug perspective.  Would you like to switch now?", 
					null, false, store, ".wait_for_build");
			switch (dialog.getReturnCode()) {
				case IDialogConstants.YES_ID:
					try
					{
						window.getWorkbench().showPerspective(debugId, window);
					}
					catch (Exception e)
					{
						e.printStackTrace();
					} 
					break;
			}
		}
		
		DebugUITools.launch(configuration, mode);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
	 *      java.lang.String)
	 */

	public void launch(IEditorPart editor, String mode) {
		// TODO: find where this constant is defined
		ILaunchConfiguration lastLaunch = DebugUITools.getLastLaunch("org.eclipse.debug.ui.launchGroup.debug");
		if (lastLaunch != null) {
			Map map = null;
			try {
				map = lastLaunch.getAttributes();
			} catch (CoreException e) { }
			if (map != null) {
				if (map.containsKey(LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE)) {
					verifyPerspectiveAndLaunch(lastLaunch, mode);
					return;
				}
			}
		}
		
		editor.getEditorSite().getPage().saveEditor(editor,true);
        org.eclipse.ui.IEditorInput input = editor.getEditorInput();
        ISelection selection = new StructuredSelection(input.getAdapter(org.eclipse.core.resources.IFile.class));
        launch(selection, mode);
	}

}
