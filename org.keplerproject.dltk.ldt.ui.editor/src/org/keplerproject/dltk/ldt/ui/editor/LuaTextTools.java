package org.keplerproject.dltk.ldt.ui.editor;

import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.rules.IPartitionTokenScanner;
import org.eclipse.ui.texteditor.ITextEditor;
import org.keplerproject.dltk.ldt.ui.editor.scanner.LuaPartitionScanner;

public class LuaTextTools extends ScriptTextTools {
	private IPartitionTokenScanner partitionScanner;

	private static final String[] LEGAL_CONTENT_TYPES = new String[] {
			ILuaPartitions.LUA_STRING, ILuaPartitions.LUA_COMMENT };

	protected LuaTextTools(boolean autoDisposeOnDisplayDispose) {
		super(ILuaPartitions.LUA_PARTITIONING, LEGAL_CONTENT_TYPES,
				autoDisposeOnDisplayDispose);
		partitionScanner = new LuaPartitionScanner();
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
		return partitionScanner;
	}
}
