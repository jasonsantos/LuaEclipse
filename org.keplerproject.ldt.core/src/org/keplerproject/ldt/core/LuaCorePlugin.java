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

package org.keplerproject.ldt.core;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.core.resources.IResourceDelta;
import org.eclipse.core.resources.IResourceDeltaVisitor;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.keplerproject.ldt.core.project.LuaProjectNature;
import org.osgi.framework.BundleContext;

/**
 * The Core Plugin Class. This plugin provide the common infra-structure to the
 * LDT plugins.
 * 
 * @author Guilherme Martins
 * @version $Id$
 */
public class LuaCorePlugin extends Plugin implements IResourceChangeListener {

	// The shared instance.
	private static LuaCorePlugin plugin;

	/**
	 * The constructor.
	 */
	public LuaCorePlugin() {
		plugin = this;

		for(IProject prj : ResourcesPlugin.getWorkspace().getRoot().getProjects()) {
			try {
				if(prj.isOpen() && prj.hasNature(LuaProjectNature.NATURE_ID))
					LuaProject.getLuaProject(prj);
			} catch (CoreException e) {
				e.printStackTrace();
			}
		}
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);

	}

	/**
	 * This method is called when the plug-in is stopped
	 */
	public void stop(BundleContext context) throws Exception {
		super.stop(context);
		plugin = null;
	}

	/**
	 * Returns the shared instance.
	 */
	public static LuaCorePlugin getDefault() {
		return plugin;
	}

	public static IProject[] getLuaProjects() {
		List<IProject> luaProjectsList = new ArrayList<IProject>();
		IProject workspaceProjects[] = ResourcesPlugin.getWorkspace().getRoot()
				.getProjects();
		for (int i = 0; i < workspaceProjects.length; i++) {
			IProject iProject = workspaceProjects[i];
			if (isLuaProject(iProject))
				luaProjectsList.add(iProject);
		}

		IProject luaProjects[] = new IProject[luaProjectsList.size()];
		return (IProject[]) luaProjectsList.toArray(luaProjects);
	}

	public static LuaProject getLuaProject(String name) {
		IProject aProject = ResourcesPlugin.getWorkspace().getRoot()
				.getProject(name);
		if (isLuaProject(aProject)) {
			LuaProject theLuaProject = new LuaProject();
			theLuaProject.setProject(aProject);
			return theLuaProject;
		} else {
			return null;
		}
	}

	public static boolean isLuaProject(IProject aProject) {
		try {
			return aProject
					.hasNature("org.keplerproject.ldt.core.project.LuaProjectNature");
		} catch (CoreException coreexception) {
			return false;
		}
	}

	public void resourceChanged(IResourceChangeEvent event) {
		System.out.println(this.getClass().getName()
				+ "  resource  changed  event");
		try {
			event.getDelta().accept(new IResourceDeltaVisitor() {
				public boolean visit(IResourceDelta delta) throws CoreException {
					if (delta != null
							&& delta.getResource() instanceof IProject) {
						
						IProject project = (IProject) delta.getResource();
						
						StringBuffer buf = new StringBuffer(80);
						
						switch (delta.getKind()) {
						case IResourceDelta.ADDED:
							buf.append("ADDED");
							break;
						case IResourceDelta.REMOVED:
							buf.append("REMOVED");
							break;
						case IResourceDelta.CHANGED:
							buf.append("CHANGED");
							break;
						default:
							buf.append("[");
							buf.append(delta.getKind());
							buf.append("]");
							break;
						}
						buf.append("  ");
						buf.append(delta.getResource());
						System.out.println(buf);
						return false; // no need to descend below projects
					} else {
						return false;
					}
				}
			});
		} catch (CoreException e) {
			e.printStackTrace(System.out);
		}

	}

}
