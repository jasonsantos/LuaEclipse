package org.keplerproject.dltk.ldt.ui.ide.search;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.ui.search.ScriptSearchPage;
import org.keplerproject.dltk.ldt.core.LuaLanguageToolkit;

public class LuaSearchPage extends ScriptSearchPage {

	@Override
	protected IDLTKLanguageToolkit getLanguageToolkit() {
		return LuaLanguageToolkit.getDefault();
	}

}
