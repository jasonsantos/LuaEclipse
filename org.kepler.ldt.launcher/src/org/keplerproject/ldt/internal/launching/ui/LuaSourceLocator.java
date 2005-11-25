package org.keplerproject.ldt.internal.launching.ui;

import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.IPersistableSourceLocator;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.ui.ISourcePresentation;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.FileEditorInput;
import org.keplerproject.ldt.internal.launching.LuaLaunchConfigurationAttribute;

public class LuaSourceLocator implements IPersistableSourceLocator,
		ISourcePresentation, LuaLaunchConfigurationAttribute {

	public static final String ID = "org.keplerproject.ldt.launching.ui.luaSourceLocator";
	private String absoluteWorkingDirectory;

	public LuaSourceLocator() {
	}

	public String getAbsoluteWorkingDirectory() {
		return absoluteWorkingDirectory;
	}

	public String getMemento() throws CoreException {
		return null;
	}

	public void initializeFromMemento(String s) throws CoreException {
	}

	public void initializeDefaults(ILaunchConfiguration configuration)
			throws CoreException {
		absoluteWorkingDirectory = configuration.getAttribute(
				WORKING_DIRECTORY, "");
	}

	public Object getSourceElement(IStackFrame stackFrame) {
		return null;
	}

	public String getEditorId(IEditorInput input, Object element) {
		return PlatformUI.getWorkbench().getEditorRegistry().getDefaultEditor(
				(String) element).getId();
	}

	public IEditorInput getEditorInput(Object element) {
		String filename = (String) element;
		org.eclipse.core.resources.IFile eclipseFile = ResourcesPlugin
				.getWorkspace().getRoot()
				.getFileForLocation(new Path(filename));
		if (eclipseFile == null) {
			filename = getAbsoluteWorkingDirectory() + "/" + filename;
			eclipseFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFileForLocation(new Path(filename));
			if (eclipseFile == null)
				return null;
		}
		return new FileEditorInput(eclipseFile);
	}

}