package org.keplerproject.dltk.ldt.ui.wizards;

import org.eclipse.dltk.ui.DLTKUIPlugin;
import org.eclipse.dltk.ui.wizards.NewSourceModulePage;
import org.eclipse.dltk.ui.wizards.NewSourceModuleWizard;
import org.keplerproject.dltk.ldt.ui.internal.LuaEclipseImages;

public class LuaNewFileWizard extends NewSourceModuleWizard {

	public static final String WIZARD_ID = "org.keplerproject.dltk.ldt.ui.ide.wizards.newfile"; //$NON-NLS-1$

	public LuaNewFileWizard() {
		setDefaultPageImageDescriptor(LuaEclipseImages.DESC_WIZBAN_FILE_CREATION);
		setDialogSettings(DLTKUIPlugin.getDefault().getDialogSettings());
	}

	@Override
	protected NewSourceModulePage createNewSourceModulePage() {
		return new LuaNewFilePage() {
			protected String getPageTitle() {
				return "Create a new Lua script";
			}

			protected String getPageDescription() {
				return "Creates an empty script";
			}
		};
	}

}
