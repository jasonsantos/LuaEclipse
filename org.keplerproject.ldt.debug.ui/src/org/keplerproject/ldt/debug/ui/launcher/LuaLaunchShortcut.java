/**
 * 
 */
package org.keplerproject.ldt.debug.ui.launcher;

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
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;

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
					DebugUITools.launch(configuration, mode);
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
			DebugUITools.launch(configuration, mode);
		} catch (CoreException e1) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchShortcut#launch(org.eclipse.ui.IEditorPart,
	 *      java.lang.String)
	 */

	public void launch(IEditorPart editor, String mode) {
	}

}
