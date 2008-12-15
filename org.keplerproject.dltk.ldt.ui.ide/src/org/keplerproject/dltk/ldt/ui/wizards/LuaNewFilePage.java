package org.keplerproject.dltk.ldt.ui.wizards;

import org.eclipse.dltk.ui.wizards.NewSourceModulePage;
import org.keplerproject.dltk.ldt.core.LuaNature;

public abstract class LuaNewFilePage extends NewSourceModulePage {

	@Override
	protected String getRequiredNature() {
		return LuaNature.LUA_NATURE;
	}

}
