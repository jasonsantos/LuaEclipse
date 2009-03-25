/**
 * 
 */
package org.keplerproject.ldt.debug.core.source;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IContainer;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.sourcelookup.ISourceContainer;
import org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate;
import org.eclipse.debug.core.sourcelookup.containers.DirectorySourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.FolderSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.ProjectSourceContainer;
import org.eclipse.debug.core.sourcelookup.containers.WorkspaceSourceContainer;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;

/**
 * @author jasonsantos
 */
public class SourcePathComputerDelegate implements ISourcePathComputerDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourcePathComputerDelegate#computeSourceContainers(org.eclipse.debug.core.ILaunchConfiguration,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	public ISourceContainer[] computeSourceContainers(
			ILaunchConfiguration configuration, IProgressMonitor monitor)
			throws CoreException {

		List<ISourceContainer> containers = new ArrayList<ISourceContainer>();
		
		String project = configuration.getAttribute(
				LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE, (String) null);
		if (project != null) {
			ISourceContainer sourceContainer = null;
			IProject projectObj = ResourcesPlugin.getWorkspace().getRoot().getProject(project);
			if (projectObj.exists())
				sourceContainer = new ProjectSourceContainer(projectObj, false);
			else
				sourceContainer = new FolderSourceContainer(projectObj, false);
			containers.add(sourceContainer);
		}
		containers.add(new WorkspaceSourceContainer());
		containers.add(new DirectorySourceContainer(File.listRoots()[0], true));
		return containers.toArray(new ISourceContainer[]{});
	}

}
