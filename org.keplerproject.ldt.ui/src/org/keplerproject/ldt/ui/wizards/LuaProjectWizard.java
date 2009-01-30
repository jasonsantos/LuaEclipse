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
package org.keplerproject.ldt.ui.wizards;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectDescription;
import org.eclipse.core.resources.IWorkspace;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.SubProgressMonitor;
import org.eclipse.jface.operation.IRunnableWithProgress;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.wizard.Wizard;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.dialogs.WizardNewProjectCreationPage;
import org.eclipse.ui.wizards.newresource.BasicNewResourceWizard;
import org.keplerproject.ldt.core.project.LuaProjectNature;
import org.keplerproject.ldt.ui.LDTUIPlugin;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaProjectWizard extends Wizard implements INewWizard {
	
	public static final String WIZARD_ID = "org.keplerproject.ldt.ui.luaProject.Wizard"; //$NON-NLS-1$
	
	protected WizardNewProjectCreationPage prjCreationPage;
	protected IProject newProject;
	protected IWorkbench workbench;
	protected IStructuredSelection selection;
	
	public LuaProjectWizard() {
		super();
	}
    
	protected IRunnableWithProgress getProjectCreationRunnable()
    {
        return new IRunnableWithProgress() {

            public void run(IProgressMonitor monitor)
                throws InvocationTargetException, InterruptedException
            {
                int remainingWorkUnits = 10;
                monitor.beginTask("Creating new project", remainingWorkUnits);
                IWorkspace workspace = LDTUIPlugin.getWorkspace();
                newProject = prjCreationPage.getProjectHandle();
                IProjectDescription description = workspace.newProjectDescription(newProject.getName());
                IPath path = Platform.getLocation();
                IPath customPath = prjCreationPage.getLocationPath();
                if(!path.equals(customPath))
                {
                    path = customPath;
                    description.setLocation(path);
                }
                try
                {
                    if(!newProject.exists())
                    {
                        newProject.create(description, new SubProgressMonitor(monitor, 1));
                        monitor.worked(2);
                    }
                    if(!newProject.isOpen())
                    {
                        newProject.open(new SubProgressMonitor(monitor, 1));
                        monitor.worked(5);
                       
                    }
                    addNature(newProject,monitor);
                    monitor.worked(10);
                }
                catch(CoreException e)
                {
                    throw new InvocationTargetException(e);
                }
                finally
                {
                    monitor.done();
                }
            }

        };
    }

	public boolean performFinish() {
		
		try
		{
			getContainer().run(false,false,getProjectCreationRunnable());
		}catch(Exception ex)
		{
			ex.printStackTrace();
			return false;
		}
		BasicNewResourceWizard.selectAndReveal(newProject,workbench.getActiveWorkbenchWindow());
		return true;
	}

	public void init(IWorkbench workbench, IStructuredSelection selection) {
		this.prjCreationPage = new WizardNewProjectCreationPage("luaProjectCreationPage");
		//prjCreationPage.setTitle("Lua Project Creation Wizard");
		//prjCreationPage.setDescription("Creates a new Lua Project");
		setWindowTitle("New Lua Project Wizard");
		setDefaultPageImageDescriptor(LDTUIPlugin.getImageDescriptor("icons/luapwiz.gif"));
		prjCreationPage.setWizard(this);
		this.workbench = workbench;
		this.selection = selection;
	}
	public void addPages() {
		super.addPages();		
		addPage(this.prjCreationPage);

	}
	 /** Toggles sample nature on a project
	 * 
	 * @param project
	 *            to have sample nature added or removed
	 * @param monitor 
	 */
	private void addNature(IProject project, IProgressMonitor monitor) {
		try {
			IProjectDescription description = project.getDescription();
			String[] natures = description.getNatureIds();

			// Add the nature
			String[] newNatures = new String[natures.length + 1];
			System.arraycopy(natures, 0, newNatures, 0, natures.length);
			monitor.worked(8);
			newNatures[natures.length] = LuaProjectNature.NATURE_ID;
			description.setNatureIds(newNatures);
			project.setDescription(description, null);
			
		} catch (CoreException e) {
		}
	}

}
