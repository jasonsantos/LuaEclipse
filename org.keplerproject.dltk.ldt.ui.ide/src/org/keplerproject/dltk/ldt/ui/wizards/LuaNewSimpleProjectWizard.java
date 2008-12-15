/**
 * 
 */
package org.keplerproject.dltk.ldt.ui.wizards;

import java.util.Observable;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IConfigurationElement;
import org.eclipse.core.runtime.IExecutableExtension;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.launching.IInterpreterInstall;
import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.util.BusyIndicatorRunnableContext;
import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.dltk.ui.wizards.NewElementWizard;
import org.eclipse.dltk.ui.wizards.ProjectWizardFirstPage;
import org.eclipse.dltk.ui.wizards.ProjectWizardSecondPage;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.INewWizard;
import org.eclipse.ui.wizards.newresource.BasicNewProjectResourceWizard;
import org.keplerproject.dltk.ldt.core.LuaNature;
import org.keplerproject.dltk.ldt.ui.ide.LuaEclipseIDEPlugin;
import org.keplerproject.dltk.ldt.ui.internal.LuaEclipseImages;
import org.keplerproject.dltk.ldt.ui.internal.preferences.LuaBuildpathsBlock;

/**
 * @author jasonsantos
 * 
 */
public class LuaNewSimpleProjectWizard extends NewElementWizard implements
		INewWizard, IExecutableExtension {

	public static final String WIZARD_ID = "org.keplerproject.dltk.ldt.wizard"; //$NON-NLS-1$

	private IConfigurationElement fConfigElement;

	private ProjectWizardFirstPage fFirstPage;
	private ProjectWizardSecondPage fSecondPage;

	public LuaNewSimpleProjectWizard() {
		setDefaultPageImageDescriptor(LuaEclipseImages.DESC_WIZBAN_PROJECT_CREATION);
		setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
	}

	@Override
	public void addPages() {
		super.addPages();

		fFirstPage = new ProjectWizardFirstPage() {

			LuaInterpreterGroup luaInterpreterGroup;

			final class LuaInterpreterGroup extends AbstractInterpreterGroup {

				LuaInterpreterGroup(Composite composite) {
					super(composite);
				}

				@Override
				protected String getCurrentLanguageNature() {
					return LuaNature.LUA_NATURE;
				}

				@Override
				protected String getIntereprtersPreferencePageId() {
					return "org.keplerproject.dltk.ldt.preferences.interpreters";
				}
			}

			@Override
			protected void createInterpreterGroup(Composite container) {
				luaInterpreterGroup = new LuaInterpreterGroup(container);
			}

			@Override
			protected IInterpreterInstall getInterpreter() {
				return luaInterpreterGroup.getSelectedInterpreter();
			}

			@Override
			protected Observable getInterpreterGroupObservable() {
				return luaInterpreterGroup;
			}

			@Override
			protected void handlePossibleInterpreterChange() {
				luaInterpreterGroup.handlePossibleInterpreterChange();
			}

			@Override
			protected boolean interpeterRequired() {
				return true;
			}

			@Override
			protected boolean supportInterpreter() {
				return true;
			}

		};

		fFirstPage.setTitle("Create a Simple Lua Project");
		fFirstPage.setDescription("Create a project containing lua scripts");
		addPage(fFirstPage);

		fSecondPage = new ProjectWizardSecondPage(fFirstPage) {

			@Override
			protected IPreferenceStore getPreferenceStore() {
				return LuaEclipseIDEPlugin.getDefault().getPreferenceStore();
			}

			@Override
			protected BuildpathsBlock createBuildpathBlock(
					IStatusChangeListener arg0) {
				return new LuaBuildpathsBlock(
						new BusyIndicatorRunnableContext(), arg0, 0,
						useNewSourcePage(), null);
			}

			@Override
			protected String getScriptNature() {
				return LuaNature.LUA_NATURE;
			}

		};

		addPage(fSecondPage);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.dltk.ui.wizards.NewElementWizard#finishPage(org.eclipse.core
	 * .runtime.IProgressMonitor)
	 */
	@Override
	protected void finishPage(IProgressMonitor arg)
			throws InterruptedException, CoreException {

		fSecondPage.performFinish(arg);

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.ui.wizards.NewElementWizard#getCreatedElement()
	 */
	@Override
	public IModelElement getCreatedElement() {
		return DLTKCore.create(fFirstPage.getProjectHandle());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * org.eclipse.core.runtime.IExecutableExtension#setInitializationData(org
	 * .eclipse.core.runtime.IConfigurationElement, java.lang.String,
	 * java.lang.Object)
	 */
	@Override
	public void setInitializationData(IConfigurationElement config,
			String propertyName, Object data) throws CoreException {

		fConfigElement = config;

	}

	@Override
	public boolean performFinish() {
		boolean res = super.performFinish();
		if (res) {
			BasicNewProjectResourceWizard.updatePerspective(fConfigElement);
			selectAndReveal(fSecondPage.getScriptProject().getProject());
		}
		return res;
	}

	@Override
	public boolean performCancel() {
		fSecondPage.performCancel();
		return super.performCancel();
	}
}
