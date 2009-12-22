package com.anwrt.ldt.editor;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.ui.IEditorInput;
import org.keplerproject.luaeclipse.core.LuaLanguageToolkit;

import com.anwrt.ldt.internal.editor.text.ILuaPartitions;
import com.anwrt.ldt.internal.editor.text.LuaTextTools;

public class LuaEditor extends ScriptEditor {

	public static final String EDITOR_ID = Activator.PLUGIN_ID + ".LuaEditor"; //$NON-NLS-1$
	public static final String EDITOR_CONTEXT = "#LuaEditorContext";

	protected void connectPartitioningToElement(IEditorInput input,
			IDocument document) {
		if (document instanceof IDocumentExtension3) {
			IDocumentExtension3 extension = (IDocumentExtension3) document;
			if (extension.getDocumentPartitioner(ILuaPartitions.LUA_PARTITIONING) == null) {
				LuaTextTools tools = Activator.getDefault().getTextTools();
				tools.setupDocumentPartitioner(document,
						ILuaPartitions.LUA_PARTITIONING);
			}
		}
	}

	@Override
	protected void initializeEditor() {
		super.initializeEditor();
		setEditorContextMenuId(EDITOR_CONTEXT);
	}

	@Override
	public String getEditorId() {
		return EDITOR_ID;
	}

	@Override
	public IDLTKLanguageToolkit getLanguageToolkit() {
		return LuaLanguageToolkit.getDefault();
	}

	@Override
	protected IPreferenceStore getScriptPreferenceStore() {
		return Activator.getDefault().getPreferenceStore();

	}

	@Override
	public ScriptTextTools getTextTools() {
		return Activator.getDefault().getTextTools();
	}
}
