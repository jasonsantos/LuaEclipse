package com.anwrt.ldt.internal.editor.text;

import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.texteditor.ITextEditor;

import com.anwrt.ldt.editor.internal.partition.LuaPartitionScanner;

public class LuaTextTools extends ScriptTextTools {

	private final static String[] LEGAL_CONTENT_TYPES = new String[] {
			ILuaPartitions.LUA_STRING, ILuaPartitions.LUA_COMMENT,
			ILuaPartitions.LUA_SINGLE_QUOTE_STRING,
			ILuaPartitions.LUA_MULTI_LINE_COMMENT };

	private IPartitionTokenScanner fPartitionScanner;

	public LuaTextTools(boolean autoDisposeOnDisplayDispose) {
		super(ILuaPartitions.LUA_PARTITIONING, LEGAL_CONTENT_TYPES,
				autoDisposeOnDisplayDispose);
		fPartitionScanner = new LuaPartitionScanner();
	}

	@Override
	public ScriptSourceViewerConfiguration createSourceViewerConfiguraton(
			IPreferenceStore preferenceStore, ITextEditor editor,
			String partitioning) {
		return new LuaSourceViewerConfiguration(getColorManager(),
				preferenceStore, editor, partitioning);
	}

	@Override
	public IPartitionTokenScanner getPartitionScanner() {
		return fPartitionScanner;
	}
}
