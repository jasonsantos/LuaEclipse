package org.keplerproject.ldt.debug.ui.launcher;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.EnvironmentTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;
import org.eclipse.debug.ui.sourcelookup.SourceLookupTab;

public class LuaLaunchConfigurationTabGroup extends AbstractLaunchConfigurationTabGroup {

	public void createTabs(ILaunchConfigurationDialog dialog, String mode) {
		setTabs(new ILaunchConfigurationTab[] {
				new LuaMainTab(),
				new EnvironmentTab(),
				new SourceLookupTab(),
				new CommonTab()
		});
		
	}

}
