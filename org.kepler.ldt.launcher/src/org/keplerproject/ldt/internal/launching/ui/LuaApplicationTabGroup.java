package org.keplerproject.ldt.internal.launching.ui;

import org.eclipse.debug.ui.AbstractLaunchConfigurationTabGroup;
import org.eclipse.debug.ui.CommonTab;
import org.eclipse.debug.ui.ILaunchConfigurationDialog;
import org.eclipse.debug.ui.ILaunchConfigurationTab;

public class LuaApplicationTabGroup extends AbstractLaunchConfigurationTabGroup
{

    public LuaApplicationTabGroup()
    {
    }

    public void createTabs(ILaunchConfigurationDialog dialog, String mode)
    {
        ILaunchConfigurationTab tabs[] = {
            new LuaEntryPointTab(), new LuaArgumentsTab(), new LuaEnvironmentTab(), new CommonTab()
        };
        setTabs(tabs);
    }
}