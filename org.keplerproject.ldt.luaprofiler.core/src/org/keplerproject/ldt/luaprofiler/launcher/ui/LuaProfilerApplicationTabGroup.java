package org.keplerproject.ldt.luaprofiler.launcher.ui;

import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.keplerproject.ldt.internal.launching.ui.LuaApplicationTabGroup;
import org.keplerproject.ldt.internal.launching.ui.LuaArgumentsTab;
import org.keplerproject.ldt.internal.launching.ui.LuaEntryPointTab;
import org.keplerproject.ldt.internal.launching.ui.LuaEnvironmentTab;

public class LuaProfilerApplicationTabGroup extends LuaApplicationTabGroup {
	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		ILaunchConfigurationTab tabs[] = { new LuaEntryPointTab(),
				new LuaArgumentsTab(), new LuaEnvironmentTab(), new CommonTab() };
		setTabs(tabs);
	}
}
