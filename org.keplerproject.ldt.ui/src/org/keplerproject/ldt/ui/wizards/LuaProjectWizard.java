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

public class LuaProjectWizard extends Wizard implements INewWizard {
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
