package org.keplerproject.dltk.ldt.ui.editor;

import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;

public class LuaContentAssistPreference extends ContentAssistPreference {
	private static LuaContentAssistPreference instance;
	
	public static LuaContentAssistPreference getDefault() {
		if (instance == null) instance = new LuaContentAssistPreference();
		return instance;
	}
	
	@Override
	protected ScriptTextTools getTextTools() {
		return LuaEditorUIPlugin.getDefault().getTextTools();
	}

}
