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

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.keplerproject.ldt.ui.LDTUIPlugin;
/**
 *  The Platform runner delegate for lua intepreters
 * @author guilherme
 * @version $Id$
 */
public class LuaApplicationLaunchConfigurationDelegate
    implements ILaunchConfigurationDelegate
{

	protected static final InterpreterRunner interpreterRunner = new InterpreterRunner();
	public static final String TYPE_ID = "org.keplerproject.ldt.launching.LaunchConfigurationTypeLuaApplication";
    public LuaApplicationLaunchConfigurationDelegate()
    {
    }

    public void launch(ILaunchConfiguration configuration, String mode, ILaunch launch, IProgressMonitor monitor)
        throws CoreException
    {
		if (LuaRuntime.getDefault().getSelectedInterpreter() == null) {
			final Status interpreterStatus = new Status(4,"org.keplerproject.launching.ui", 0, "You must define an interpreter before running Lua Applications.", null);
			LDTUIPlugin.getDefault().getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ErrorDialog.openError(null, "LuaEclipse Error", null, interpreterStatus);				
				}
			});
			return;
		}
        if(!mode.equals("debug"))
            interpreterRunner.run(new InterpreterRunnerConfiguration(configuration), launch);
    }



}