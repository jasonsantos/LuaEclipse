package org.keplerproject.ldt.internal.launching.ui;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchConfigurationType;
import org.eclipse.debug.core.ILaunchConfigurationWorkingCopy;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.ui.ILaunchShortcut;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.StructuredSelection;
import org.eclipse.ui.IEditorPart;
import org.keplerproject.ldt.internal.launching.LuaApplicationLaunchConfigurationDelegate;
import org.keplerproject.ldt.internal.launching.LuaLaunchConfigurationAttribute;

public class LuaApplicationShortcut
    implements ILaunchShortcut,LuaLaunchConfigurationAttribute
{

    public LuaApplicationShortcut()
    {
    }

    public void launch(ISelection selection, String mode)
    {
        if(selection instanceof IStructuredSelection)
        {
            Object firstSelection = ((IStructuredSelection)selection).getFirstElement();
            if((firstSelection instanceof IFile) && ((IFile)firstSelection).getFileExtension().equals("lua"))
            {
                ILaunchConfiguration config = findLaunchConfiguration((IFile)firstSelection, mode);
                try
                {
                    if(config != null)
                        config.launch(mode, null);
                }
                catch(CoreException coreexception) { }
                return;
            }
        }
    }

    public void launch(IEditorPart editor, String mode)
    {
        org.eclipse.ui.IEditorInput input = editor.getEditorInput();
        ISelection selection = new StructuredSelection(input.getAdapter(org.eclipse.core.resources.IFile.class));
        launch(selection, mode);
    }

    protected ILaunchConfiguration findLaunchConfiguration(IFile luaFile, String mode)
    {
        ILaunchConfigurationType configType = getLuaLaunchConfigType();
        List candidateConfigs = null;
        try
        {
            ILaunchConfiguration configs[] = getLaunchManager().getLaunchConfigurations(configType);
            candidateConfigs = new ArrayList(configs.length);
            for(int i = 0; i < configs.length; i++)
            {
                ILaunchConfiguration config = configs[i];
                if(config.getAttribute(FILE_NAME, "").equals(luaFile.getProjectRelativePath().toString()))
                    candidateConfigs.add(config);
            }

        }
        catch(CoreException coreexception) { }
        switch(candidateConfigs.size())
        {
        case 0: // '\0'
            return createConfiguration(luaFile);

        case 1: // '\001'
            return (ILaunchConfiguration)candidateConfigs.get(0);
        }
        return null;
    }

    protected ILaunchConfiguration createConfiguration(IFile luaFile)
    {
        ILaunchConfiguration config = null;
        try
        {
            ILaunchConfigurationType configType = getLuaLaunchConfigType();
            ILaunchConfigurationWorkingCopy wc = configType.newInstance(null, getLaunchManager().generateUniqueLaunchConfigurationNameFrom(luaFile.getName()));
            wc.setAttribute(PROJECT_NAME, luaFile.getProject().getName());
            wc.setAttribute(FILE_NAME, luaFile.getProjectRelativePath().toString());
            wc.setAttribute(WORKING_DIRECTORY, ResourcesPlugin.getWorkspace().getRoot().getLocation().toString());
            wc.setAttribute(ILaunchConfiguration.ATTR_SOURCE_LOCATOR_ID, LuaSourceLocator.ID);
            config = wc.doSave();
        }
        catch(CoreException coreexception) { }
        return config;
    }

    protected ILaunchConfigurationType getLuaLaunchConfigType()
    {
        return getLaunchManager().getLaunchConfigurationType(LuaApplicationLaunchConfigurationDelegate.TYPE_ID);
    }

    protected ILaunchManager getLaunchManager()
    {
        return DebugPlugin.getDefault().getLaunchManager();
    }
}