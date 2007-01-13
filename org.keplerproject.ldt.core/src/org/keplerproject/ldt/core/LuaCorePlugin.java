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
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.Plugin;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.osgi.framework.BundleContext;

/**
 * The Core Plugin Class. This plugin provide the common infra-structure to the
 * LDT plugins.
 * 
 * @author Guilherme Martins
 * @version $Id$
 */
public class LuaCorePlugin extends Plugin {

	private static final String PLUGIN_CORE_ID = "org.keplerproject.ldt.core";

	// The shared instance.
	private static LuaCorePlugin plugin;
	private ImageRegistry imgReg = new ImageRegistry();

	/**
	 * The constructor.
	 */
	public LuaCorePlugin() {
		plugin = this;
	}

	/**
	 * This method is called upon plug-in activation
	 */
	public void start(BundleContext context) throws Exception {
		super.start(context);
		initializeImageRegistry(imgReg);
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

	/**
	 * Returns an image descriptor for the image file at the given plug-in
	 * relative path.
	 * 
	 * @param path
	 *            the path
	 * @return the image descriptor
	 */
	public static ImageDescriptor getImageDescriptor(String path) {
		return AbstractUIPlugin.imageDescriptorFromPlugin(PLUGIN_CORE_ID, path);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.ui.plugin.AbstractUIPlugin#initializeImageRegistry(org.eclipse.jface.resource.ImageRegistry)
	 */
	protected void initializeImageRegistry(ImageRegistry reg) {
		String[] images = { "function", "table", "string", "userdata" };

		for (int i = 0; i < images.length; i++) {
			ImageDescriptor image = null;

			image = LuaCorePlugin.getImageDescriptor("icons/" + images[i]
					+ ".gif");
			reg.put(images[i],(ImageDescriptor) image);
		}

		//super.initializeImageRegistry(reg);
	}
	
	 public static IProject[] getLuaProjects()
	    {
	        List rubyProjectsList = new ArrayList();
	        IProject workspaceProjects[] = ResourcesPlugin.getWorkspace().getRoot().getProjects();
	        for(int i = 0; i < workspaceProjects.length; i++)
	        {
	            IProject iProject = workspaceProjects[i];
	            if(isLuaProject(iProject))
	                rubyProjectsList.add(iProject);
	        }

	        IProject rubyProjects[] = new IProject[rubyProjectsList.size()];
	        return (IProject[])rubyProjectsList.toArray(rubyProjects);
	    }

	    public static LuaProject getLuaProject(String name)
	    {
	        IProject aProject = ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	        if(isLuaProject(aProject))
	        {
	            LuaProject theLuaProject = new LuaProject();
	            theLuaProject.setProject(aProject);
	            return theLuaProject;
	        } else
	        {
	            return null;
	        }
	    }
	    public static boolean isLuaProject(IProject aProject)
	    {
	        try
	        {
	            return aProject.hasNature("org.keplerproject.ldt.core.project.LuaProjectNature");
	        }
	        catch(CoreException coreexception)
	        {
	            return false;
	        }
	    }

		public ImageRegistry getImageRegistry() {
			return imgReg;
		}

}
