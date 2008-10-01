package org.keplerproject.dltk.ldt.ui;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.AbstractDLTKUILanguageToolkit;
import org.eclipse.ui.plugin.AbstractUIPlugin;
import org.keplerproject.dltk.ldt.core.LuaCorePlugin;
import org.keplerproject.dltk.ldt.core.LuaLanguageToolkit;

public class LuaUILanguageToolkit extends AbstractDLTKUILanguageToolkit {

	@Override
	protected AbstractUIPlugin getUIPLugin() {
		return LuaCorePlugin.getDefault();
	}

	public IDLTKLanguageToolkit getCoreToolkit() {
		return LuaLanguageToolkit.getDefault();
	}
}
