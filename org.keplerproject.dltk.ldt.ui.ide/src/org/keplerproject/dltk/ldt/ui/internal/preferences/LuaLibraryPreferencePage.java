package org.keplerproject.dltk.ldt.ui.internal.preferences;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.preferences.UserLibraryPreferencePage;
import org.keplerproject.dltk.ldt.core.LuaLanguageToolkit;

public class LuaLibraryPreferencePage extends UserLibraryPreferencePage {

	public static final String PAGE = "org.keplerproject.dltk.ldt.preferences.libraries";

	@Override
	protected IDLTKLanguageToolkit getLanguageToolkit() {
		return LuaLanguageToolkit.getDefault();
	}

}
