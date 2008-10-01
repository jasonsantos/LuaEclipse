package org.keplerproject.dltk.ldt.ui;

import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.dltk.ui.CodeFormatterConstants;
import org.eclipse.dltk.ui.PreferenceConstants;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.preference.PreferenceConverter;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.editors.text.EditorsUI;
import org.keplerproject.dltk.ldt.ui.editor.LuaEditorUIPlugin;
import org.keplerproject.dltk.ldt.ui.editor.text.ILuaColorConstants;

public class LuaUIPreferenceInitializer extends AbstractPreferenceInitializer {

	@Override
	public void initializeDefaultPreferences() {
		IPreferenceStore store = LuaEditorUIPlugin.getDefault().getPreferenceStore();
		EditorsUI.useAnnotationsPreferencePage(store);
		EditorsUI.useQuickDiffPreferencePage(store);
		
		PreferenceConstants.initializeDefaultValues(store);
		
		PreferenceConverter.setDefault(store, ILuaColorConstants.LUA_COMMENT, new RGB(64, 128, 128));
		PreferenceConverter.setDefault(store, ILuaColorConstants.LUA_KEYWORD, new RGB(127, 0, 85));
		PreferenceConverter.setDefault(store, ILuaColorConstants.LUA_STRING,  new RGB(0, 0, 255));
		
		store.setDefault(ILuaColorConstants.LUA_COMMENT + PreferenceConstants.EDITOR_BOLD_SUFFIX, false);
		store.setDefault(ILuaColorConstants.LUA_COMMENT + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);
 
		store.setDefault(ILuaColorConstants.LUA_KEYWORD + PreferenceConstants.EDITOR_BOLD_SUFFIX, true);
		store.setDefault(ILuaColorConstants.LUA_KEYWORD + PreferenceConstants.EDITOR_ITALIC_SUFFIX, false);
 
		store.setDefault(PreferenceConstants.EDITOR_TAB_WIDTH, 4);
		store.setDefault(PreferenceConstants.EDITOR_SYNC_OUTLINE_ON_CURSOR_MOVE, true);
 
		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_CHAR, CodeFormatterConstants.TAB);
		store.setDefault(CodeFormatterConstants.FORMATTER_TAB_SIZE, "4");
		store.setDefault(CodeFormatterConstants.FORMATTER_INDENTATION_SIZE,"4");		
	}

}
