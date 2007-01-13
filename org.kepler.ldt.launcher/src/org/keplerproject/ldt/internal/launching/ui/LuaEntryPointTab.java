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

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPage;
import org.kepler.ldt.laucher.LauncherPlugin;
import org.keplerproject.ldt.internal.launching.LuaLaunchConfigurationAttribute;
import org.keplerproject.ldt.laucher.utils.LuaFileSelector;
import org.keplerproject.ldt.laucher.utils.LuaProjectSelector;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaEntryPointTab extends AbstractLaunchConfigurationTab implements LuaLaunchConfigurationAttribute{

	protected String originalFileName;

	protected String originalProjectName;

	protected LuaProjectSelector projectSelector;

	protected LuaFileSelector fileSelector;

	public LuaEntryPointTab() {
	}

	public void createControl(Composite parent) {
		Composite composite = createPageRoot(parent);
		(new Label(composite, 0)).setText("Project:");
		projectSelector = new LuaProjectSelector(composite);
		projectSelector
				.setBrowseDialogMessage("Choose the project containing the application entry point:");
		projectSelector.setLayoutData(new GridData(768));
		projectSelector.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}

		});
		(new Label(composite, 0)).setText("File:");
		fileSelector = new LuaFileSelector(composite, projectSelector);
		fileSelector
				.setBrowseDialogMessage("Choose the Lua file that represents the application entry point:");
		fileSelector.setLayoutData(new GridData(768));
		fileSelector.addModifyListener(new ModifyListener() {

			public void modifyText(ModifyEvent evt) {
				updateLaunchConfigurationDialog();
			}

		});
	}

	protected IProject getContext() {
		IWorkbenchPage page = LauncherPlugin.getDefault().getWorkbench()
				.getActiveWorkbenchWindow().getActivePage();
		if (page != null) {
			ISelection selection = page.getSelection();
			if (selection instanceof IStructuredSelection) {
				IStructuredSelection ss = (IStructuredSelection) selection;
				if (!ss.isEmpty()) {
					Object obj = ss.getFirstElement();
					if (obj instanceof IResource)
						return ((IResource) obj).getProject();
				}
			}
			IEditorPart part = page.getActiveEditor();
			if (part != null) {
				org.eclipse.ui.IEditorInput input = part.getEditorInput();
				IResource file = (IResource) input
						.getAdapter(org.eclipse.core.resources.IResource.class);
				return file.getProject();
			}
		}
		return null;
	}

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		IProject project = getContext();
		if (project != null)
			configuration.setAttribute(PROJECT_NAME, project
					.getName());
	}

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			originalProjectName = configuration.getAttribute(
					PROJECT_NAME, "");
			originalFileName = configuration.getAttribute(
					FILE_NAME, "");
		} catch (CoreException e) {
			log(e);
		}
		projectSelector.setSelectionText(originalProjectName);
		if (!"".equals(originalFileName))
			fileSelector.setSelectionText((new Path(originalFileName))
					.toOSString());
	}

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(PROJECT_NAME,
				projectSelector.getSelectionText());
		org.eclipse.core.resources.IFile file = fileSelector.getSelection();
		configuration.setAttribute(FILE_NAME,
				file != null ? file.getProjectRelativePath().toString() : "");
	}

	protected Composite createPageRoot(Composite parent) {
		Composite composite = new Composite(parent, 0);
		GridLayout layout = new GridLayout();
		layout.marginWidth = 0;
		composite.setLayout(layout);
		setControl(composite);
		return composite;
	}

	public String getName() {
		return "File";
	}

	public boolean isValid(ILaunchConfiguration launchConfig) {
		try {
			String projectName = launchConfig.getAttribute(
					PROJECT_NAME, "");
			if (projectName.length() == 0) {
				setErrorMessage("Invalid project selection.");
				return false;
			}
			String fileName = launchConfig.getAttribute(
					FILE_NAME, "");
			if (fileName.length() == 0) {
				setErrorMessage("Invalid Lua file.");
				return false;
			}
		} catch (CoreException e) {
			log(e);
		}
		setErrorMessage(null);
		return true;
	}

	protected void log(Throwable throwable) {
	}

	public boolean canSave() {
		return getErrorMessage() == null;
	}

	public Image getImage() {
		return LauncherPlugin.getImageDescriptor("icons/run_lua.gif").createImage();
	}

}