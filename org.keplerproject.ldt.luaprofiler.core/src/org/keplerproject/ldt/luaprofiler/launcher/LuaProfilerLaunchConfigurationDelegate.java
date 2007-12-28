package org.keplerproject.ldt.luaprofiler.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.keplerproject.ldt.internal.launching.InterpreterRunner;
import org.keplerproject.ldt.internal.launching.InterpreterRunnerConfiguration;
import org.keplerproject.ldt.internal.launching.LuaRuntime;
import org.keplerproject.ldt.luaprofiler.core.Activator;
import org.keplerproject.ldt.luaprofiler.core.LuaProfiler;

public class LuaProfilerLaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate {

	protected static final InterpreterRunner interpreterRunner = new LuaProfilerInterpreterRunner();
	public static final String TYPE_ID = "org.keplerproject.ldt.luaprofiler.launcher.ProfilerLaunchConfigurationTypeLuaApplication";

	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		if (LuaRuntime.getDefault().getSelectedInterpreter() == null) {
			final Status interpreterStatus = new Status(4,"org.keplerproject.launching.ui", 0, "You must define an interpreter before running Lua Applications.", null);
			Activator.getDefault().getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ErrorDialog.openError(null, "LuaProfiler Error", null, interpreterStatus);				
				}
			});
			return;
		}
		if (LuaProfiler.getDefault().getSelectedProfiler() == null) {
			final Status profilerStatus = new Status(Status.ERROR, Activator.PLUGIN_ID, "You must define a LuaProfiler");
			Activator.getDefault().getWorkbench().getDisplay().asyncExec(new Runnable() {
				public void run() {
					ErrorDialog.openError(null, "LuaProfiler Error", null, profilerStatus);				
				}
				
			});
			return;
		}
		interpreterRunner.run(
				new InterpreterRunnerConfiguration(configuration), launch);

	}
}
