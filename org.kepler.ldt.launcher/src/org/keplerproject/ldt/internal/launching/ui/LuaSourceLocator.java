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

import java.io.File;

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
/**
 * 
 * @author guilherme
 * @version $Id$
 */
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
			filename = getAbsoluteWorkingDirectory() + File.pathSeparator + filename;
			eclipseFile = ResourcesPlugin.getWorkspace().getRoot()
					.getFileForLocation(new Path(filename));
			if (eclipseFile == null)
				return null;
		}
		return new FileEditorInput(eclipseFile);
	}

}