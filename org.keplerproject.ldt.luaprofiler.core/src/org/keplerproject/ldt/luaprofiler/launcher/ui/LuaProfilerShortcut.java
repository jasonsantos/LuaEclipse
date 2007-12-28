package org.keplerproject.ldt.luaprofiler.launcher.ui;

import org.eclipse.debug.core.ILaunchConfigurationType;
import org.keplerproject.ldt.internal.launching.ui.LuaApplicationShortcut;
import org.keplerproject.ldt.luaprofiler.launcher.LuaProfilerLaunchConfigurationDelegate;

public class LuaProfilerShortcut extends LuaApplicationShortcut {
	@Override
	protected ILaunchConfigurationType getLuaLaunchConfigType() {
		return getLaunchManager().getLaunchConfigurationType(LuaProfilerLaunchConfigurationDelegate.TYPE_ID);
	}
}
