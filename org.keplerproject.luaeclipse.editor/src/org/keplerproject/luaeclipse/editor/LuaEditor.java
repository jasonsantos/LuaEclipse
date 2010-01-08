/******************************************************************************
 * Copyright (c) 2009 KeplerProject, Sierra Wireless.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 *          - initial API and implementation and initial documentation
 *****************************************************************************/


package org.keplerproject.luaeclipse.editor;

import org.eclipse.dltk.core.IDLTKLanguageToolkit;
import org.eclipse.dltk.internal.ui.editor.ScriptEditor;
import org.eclipse.dltk.ui.text.ScriptTextTools;
import org.eclipse.dltk.ui.text.folding.IFoldingStructureProvider;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentExtension3;
import org.eclipse.ui.IEditorInput;
import org.keplerproject.luaeclipse.core.LuaLanguageToolkit;
import org.keplerproject.luaeclipse.editor.internal.text.ILuaPartitions;
import org.keplerproject.luaeclipse.editor.internal.text.LuaASTFoldingStructureProvider;
import org.keplerproject.luaeclipse.editor.internal.text.LuaTextTools;


public class LuaEditor extends ScriptEditor {

	public static final String EDITOR_ID = Activator.PLUGIN_ID + ".LuaEditor"; //$NON-NLS-1$
	public static final String EDITOR_CONTEXT = "#LuaEditorContext";
	private IFoldingStructureProvider foldingStructureProvider = null;

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
	protected IFoldingStructureProvider getFoldingStructureProvider() {
		if (foldingStructureProvider == null) {
			foldingStructureProvider = new LuaASTFoldingStructureProvider();
		}
		return foldingStructureProvider;
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
