package org.keplerproject.luaeclipse.editor.internal.text;

import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.keplerproject.luaeclipse.editor.Activator;


public class LuaContentAssistPreference extends ContentAssistPreference {

	private static LuaContentAssistPreference sDefault;

	public static ContentAssistPreference getDefault() {
		if (sDefault == null) {
			sDefault = new LuaContentAssistPreference();
		}
		return sDefault;
	}

	@Override
	protected ScriptTextTools getTextTools() {
		return Activator.getDefault().getTextTools();
	}
}
