/**
 * 
 */
package org.keplerproject.ldt.debug.ui.launcher;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.internal.ui.DefaultLabelProvider;
import org.eclipse.debug.internal.ui.SWTFactory;
import org.eclipse.debug.ui.AbstractLaunchConfigurationTab;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Text;
import org.eclipse.ui.dialogs.ElementListSelectionDialog;
import org.eclipse.ui.dialogs.ResourceListSelectionDialog;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;

/**
 * Main tab for Debugging Lua Files
 * 
 * @author jasonsantos
 */
public class LuaMainTab extends AbstractLaunchConfigurationTab implements
		ILaunchConfigurationTab {

	private class WidgetListener implements ModifyListener, SelectionListener {

		private Text		fTextField	= null;
		private IContainer	fProject	= ResourcesPlugin.getWorkspace()
												.getRoot();

		public WidgetListener(Text textField) {
			fTextField = textField;
		}

		public void browseLuaProjects() {
			ILabelProvider labelProvider = new DefaultLabelProvider();
			ElementListSelectionDialog dialog = new ElementListSelectionDialog(
					getShell(), labelProvider);
			dialog.setTitle(MSG_PROJECT_DLG_TITLE);
			dialog.setMessage(MSG_PROJECT_DLG_MESSAGE);

			dialog.setElements(ResourcesPlugin.getWorkspace().getRoot()
					.getProjects());

			if (dialog.open() == Window.OK) {
				Object[] files = dialog.getResult();
				IFile file = (IFile) files[0];
				fTextField.setText(file.getFullPath().toString());
				fProject = (IContainer) files[0];
			}
		}

		public void browseLuaScripts() {
			ResourceListSelectionDialog dialog = new ResourceListSelectionDialog(
					getShell(), fProject, IResource.FILE);
			dialog.setTitle(MSG_SCRIPT_DLG_TITLE);
			dialog.setMessage(MSG_SCRIPT_DLG_MESSAGE);

			if (dialog.open() == Window.OK) {
				Object[] files = dialog.getResult();
				IFile file = (IFile) files[0];
				fTextField.setText(file.getFullPath().toString());
			}
		}


		public void modifyText(ModifyEvent e) {
			updateLaunchConfigurationDialog();
		}


		public void widgetDefaultSelected(SelectionEvent e) {
			System.out.println(e.detail);
		}

		public void remoteDbgSelected(boolean isEnabled) {
			mainGroup.setEnabled(!isEnabled);
			argsGroup.setEnabled(!isEnabled);
			remoteDbgText.setEnabled(isEnabled);
		}
		
		public void widgetSelected(SelectionEvent e) {
			if (e.widget == fProjButton) {
				browseLuaProjects();
			} else if (e.widget == remoteDbgBox) {
				remoteDbgSelected(remoteDbgBox.getSelection());
				updateLaunchConfigurationDialog();
			} else {
				browseLuaScripts();
			}
		}
	}

	protected static final String	MSG_PROJECT_TITLE		= "&Project:";
	protected static final String	MSG_ARGS_TITLE			= "&Command-line Arguments:";
	protected static final String	REMOTE_DBG_TITLE		= "&Remote Debug:";
	protected static final String	MSG_PROJECT_BUTTON		= "&Browse...";

	protected static final String	MSG_PROJECT_DLG_TITLE	= "Lua Project";
	protected static final String	MSG_PROJECT_DLG_MESSAGE	= "Select Lua Project";

	protected static final String	EMPTY_STRING			= "";					//$NON-NLS-1$

	// Project UI widgets
	protected Text					fProjText;
	
	protected Text					fArgsText;

	private Button					fProjButton;

	private Button 					remoteDbgBox;
	
	private Text 					remoteDbgText;
	
	private Group projectGroup;
	private Group mainGroup;
	private Group argsGroup;
	private Group remoteDbgGroup;
	
	private final WidgetListener	fProjectListener		= new WidgetListener(
																	fProjText);

	protected void createProjectEditor(Composite parent) {
		Font font = parent.getFont();
		projectGroup = new Group(parent, SWT.NONE);
		projectGroup.setText(MSG_PROJECT_TITLE);
		GridData gd = new GridData(GridData.FILL_HORIZONTAL);
		projectGroup.setLayoutData(gd);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		projectGroup.setLayout(layout);
		projectGroup.setFont(font);
		
		gd = new GridData(GridData.FILL_HORIZONTAL);
		fProjText = new Text(projectGroup, SWT.SINGLE | SWT.BORDER);
		fProjText.setLayoutData(gd);
		fProjText.setFont(font);
		fProjText.addModifyListener(fProjectListener);
		
		fProjButton = createPushButton(projectGroup, MSG_PROJECT_BUTTON, null);
		fProjButton.addSelectionListener(fProjectListener);
	}

	protected static final String	MSG_SEARCH_BUTTON		= "&Search...";

	protected static final String	MSG_SCRIPT_DLG_TITLE	= "Lua Script";
	protected static final String	MSG_SCRIPT_DLG_MESSAGE	= "Select a Lua Script";

	protected Text					fMainText;
	private Button					fSearchButton;

	private final WidgetListener	fMainListener			= new WidgetListener(fMainText);
	
	private final WidgetListener	fArgsListener			= new WidgetListener(fArgsText);
	
	private WidgetListener			fRemoteDbgListener;

	protected void createMainTypeEditor(Composite parent, String text) {
		Font font = parent.getFont();
		mainGroup = SWTFactory.createGroup(parent, text, 2, 1,
				GridData.FILL_HORIZONTAL);
		Composite comp = SWTFactory.createComposite(mainGroup, font, 2, 2,
				GridData.FILL_BOTH, 0, 0);

		fMainText = SWTFactory.createSingleText(comp, 1);
		fMainText.addModifyListener(fMainListener);

		fSearchButton = createPushButton(comp, MSG_SEARCH_BUTTON, null);
		fSearchButton.addSelectionListener(fMainListener);
		// createMainTypeExtensions(comp);
		
		argsGroup = SWTFactory.createGroup(parent, MSG_ARGS_TITLE, 1, 1, GridData.FILL_HORIZONTAL);
		fArgsText = SWTFactory.createSingleText(argsGroup, 1);
		fArgsText.addModifyListener(fArgsListener);
		
		remoteDbgGroup = SWTFactory.createGroup(parent, REMOTE_DBG_TITLE, 1, 1, GridData.FILL_HORIZONTAL);
		remoteDbgBox = SWTFactory.createCheckButton(remoteDbgGroup, "Enable remote debug", null, false, 0);
		SWTFactory.createLabel(remoteDbgGroup, "TCP Port", 1);
		remoteDbgText = SWTFactory.createSingleText(remoteDbgGroup, 1);
		fRemoteDbgListener = new WidgetListener(remoteDbgText);
		remoteDbgBox.addSelectionListener(fRemoteDbgListener);
		fArgsText.addModifyListener(fArgsListener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#createControl(org.eclipse.swt.widgets.Composite)
	 */

	public void createControl(Composite parent) {
		Composite comp = SWTFactory.createComposite(parent, parent.getFont(),
				1, 1, GridData.FILL_BOTH);
		((GridLayout) comp.getLayout()).verticalSpacing = 0;

		createProjectEditor(comp);
		createVerticalSpacer(comp, 1);
		createMainTypeEditor(comp, "&Main script file:");
		setControl(comp);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#getName()
	 */
	public String getName() {
		return "Main";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#initializeFrom(org.eclipse.debug.core.ILaunchConfiguration)
	 */

	public void initializeFrom(ILaunchConfiguration configuration) {
		try {
			String project = configuration.getAttribute(
					LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE, (String) null);

			String script = configuration.getAttribute(
					LuaDebuggerPlugin.LUA_SCRIPT_ATTRIBUTE, (String) null);

			String args = configuration.getAttribute(
					LuaDebuggerPlugin.LUA_ARGS_ATTRIBUTE, (String) null);

			boolean remoteDbgEnabled = configuration.getAttribute(
					LuaDebuggerPlugin.LUA_REMOTE_DBG_ENABLED_ATTRIBUTE, false);
			
			int remoteDbgPort = configuration.getAttribute(
					LuaDebuggerPlugin.LUA_REMOTE_DBG_PORT_ATTRIBUTE, 8171);
			
			if (project != null) {
				fProjText.setText(project);
			}
			if (script != null) {
				fMainText.setText(script);
			}
			if (args != null) {
				fArgsText.setText(args);
			}
			remoteDbgBox.setSelection(remoteDbgEnabled);
			remoteDbgText.setText(""+remoteDbgPort);
			fRemoteDbgListener.remoteDbgSelected(remoteDbgEnabled);
			
		} catch (CoreException e) {
			setErrorMessage(e.getMessage());
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#performApply(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */

	public void performApply(ILaunchConfigurationWorkingCopy configuration) {
		String project = fProjText.getText().trim();
		String script = fMainText.getText().trim();
		String args = fArgsText.getText().trim();
		boolean remoteDbgEnabled = remoteDbgBox.getSelection();
		String remoteDbgPortString = remoteDbgText.getText();
		int remoteDbgPort = Integer.parseInt(remoteDbgPortString);

		if (project.length() == 0) {
			project = null;
		}
		if (script.length() == 0) {
			script = null;
		}
		if (args.length() == 0) {
			args = null;
		}
		configuration.setAttribute(LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE,
				project);

		configuration.setAttribute(LuaDebuggerPlugin.LUA_SCRIPT_ATTRIBUTE,
				script);

		configuration.setAttribute(LuaDebuggerPlugin.LUA_ARGS_ATTRIBUTE,
				args);
		
		configuration.setAttribute(LuaDebuggerPlugin.LUA_REMOTE_DBG_ENABLED_ATTRIBUTE, remoteDbgEnabled);

		configuration.setAttribute(LuaDebuggerPlugin.LUA_REMOTE_DBG_PORT_ATTRIBUTE, remoteDbgPort);
		
		// perform resource mapping for contextual launch
		IResource[] resources = null;
		if (script != null) {
			IPath path = new Path(script);
			IResource res = ResourcesPlugin.getWorkspace().getRoot()
					.findMember(path);
			if (res != null) {
				resources = new IResource[] { res };
			}
		}
		configuration.setMappedResources(resources);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ILaunchConfigurationTab#setDefaults(org.eclipse.debug.core.ILaunchConfigurationWorkingCopy)
	 */

	public void setDefaults(ILaunchConfigurationWorkingCopy configuration) {
		configuration.setAttribute(LuaDebuggerPlugin.LUA_REMOTE_DBG_PORT_ATTRIBUTE, false);
		configuration.setAttribute(LuaDebuggerPlugin.LUA_REMOTE_DBG_PORT_ATTRIBUTE, 8171);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.AbstractLaunchConfigurationTab#isValid(org.eclipse.debug.core.ILaunchConfiguration)
	 */
	@Override
	public boolean isValid(ILaunchConfiguration launchConfig) {
		setErrorMessage(null);
		setMessage(null);
		String text = fMainText.getText();

		if (remoteDbgBox.getSelection() && remoteDbgText.getText().length() > 0) {
			try {
				Integer.parseInt(remoteDbgText.getText());
			} catch (NumberFormatException e) {
				setErrorMessage("Invalid TCP port - must be a valid number");
				return false;
			}
		} else if (text.length() > 0) {
			IPath path = new Path(text);
			if (ResourcesPlugin.getWorkspace().getRoot().findMember(path) == null) {
				setErrorMessage("Specified script does not exist");
				return false;
			}
		} else {
			setMessage("Specify a script");
		}
		
		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace().getRoot();
		String projectString = fProjText.getText();
		if (projectString == null || projectString.length() == 0) {
			setErrorMessage("Project must be specified");
			return false;
		}
		
		boolean badProject = false;
		try {
			IProject project = myWorkspaceRoot.getProject(projectString);
			if (project == null || !project.exists())
				badProject = true;
		} catch (Exception e) {
			badProject = true;
		}
		if (badProject) {
			setErrorMessage("Specified project cannot be found");
			return false;
		}
			
		return true;
	}
}
