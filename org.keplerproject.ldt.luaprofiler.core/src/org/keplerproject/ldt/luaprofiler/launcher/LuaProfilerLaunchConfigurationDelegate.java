package org.keplerproject.ldt.luaprofiler.launcher;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.keplerproject.ldt.internal.launching.InterpreterRunner;
import org.keplerproject.ldt.internal.launching.InterpreterRunnerConfiguration;
import org.keplerproject.ldt.internal.launching.LuaRuntime;

public class LuaProfilerLaunchConfigurationDelegate implements
		ILaunchConfigurationDelegate {

	protected static final InterpreterRunner interpreterRunner = new LuaProfilerInterpreterRunner();
	public static final String TYPE_ID = "org.keplerproject.ldt.launching.LaunchConfigurationTypeLuaApplication";

	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		if (LuaRuntime.getDefault().getSelectedInterpreter() == null)
			throw new CoreException(
					new Status(
							4,
							"org.keplerproject.launching.ui",
							0,
							"You must define an interpreter before running Lua Applications.",
							null));
		interpreterRunner.run(
				new InterpreterRunnerConfiguration(configuration), launch);

	}
}
