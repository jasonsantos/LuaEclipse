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

package org.keplerproject.ldt.internal.launching;

import java.io.File;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.keplerproject.ldt.core.LuaProject;
/**
 * Lua interpreter configuration. Encapsulate the runner configuration.
 * @author guilherme
 * @version $Id$
 */
public class InterpreterRunnerConfiguration implements
		LuaLaunchConfigurationAttribute {
	protected ILaunchConfiguration configuration;

	public InterpreterRunnerConfiguration(ILaunchConfiguration aConfiguration) {
		configuration = aConfiguration;
	}

	public String getAbsoluteFileName() {
		IProject project = getProject().getProject();
		return project.getLocation().toOSString() + File.separator
				+ getFileName();
	}

	public String getFileName() {
		String fileName = "";
		try {
			fileName = configuration.getAttribute(FILE_NAME,
					"No file specified in configuration");
		} catch (CoreException coreexception) {
		}
		return fileName;
	}

	public LuaProject getProject() {
		String projectName = "";
		try {
			projectName = configuration.getAttribute(PROJECT_NAME, "");
		} catch (CoreException coreexception) {
		}
		org.eclipse.core.resources.IProject project = ResourcesPlugin
				.getWorkspace().getRoot().getProject(projectName);
		LuaProject luaProject = new LuaProject();
		luaProject.setProject(project);
		return luaProject;
	}

	public File getAbsoluteWorkingDirectory() {
		String file = null;
		try {
			file = configuration.getAttribute(WORKING_DIRECTORY, "");
		} catch (CoreException coreexception) {
		}
		return new File(file);
	}

	public String getInterpreterArguments() {
		try {
			return configuration.getAttribute(INTERPRETER_ARGUMENTS, "");
		} catch (CoreException coreexception) {
			return "";
		}
	}

	public String getProgramArguments() {
		try {
			return configuration.getAttribute(PROGRAM_ARGUMENTS, "");
		} catch (CoreException coreexception) {
			return "";
		}
	}

	public LuaInterpreter getInterpreter() throws CoreException {
		String selectedInterpreter = null;
		try {
			selectedInterpreter = configuration.getAttribute(
					SELECTED_INTERPRETER, "");
		} catch (CoreException coreexception) {
		}
		LuaInterpreter li = LuaRuntime.getDefault().getInterpreter(
				selectedInterpreter);

		// parse the environment variables..
		try {
			StringTokenizer tk = new StringTokenizer(configuration
					.getAttribute(ENVIRONMENT_VARS, ""), System
					.getProperty("line.separator"));
			String[] envs = new String[tk.countTokens()];
			int k = 0;
			while (tk.hasMoreTokens())
				envs[k++] = tk.nextToken();
			li.setEnvironment(envs);

		} catch (CoreException e) {
			li.setEnvironment(new String[] {});
		}
		return li;
	}

}