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

package org.keplerproject.luaeclipse.editor.internal.text;

import org.eclipse.dltk.internal.ui.typehierarchy.HierarchyInformationControl;
import org.eclipse.dltk.ui.text.AbstractScriptScanner;
import org.eclipse.dltk.ui.text.IColorManager;
import org.eclipse.dltk.ui.text.ScriptPresentationReconciler;
import org.eclipse.dltk.ui.text.ScriptSourceViewerConfiguration;
import org.eclipse.dltk.ui.text.SingleTokenScriptScanner;
import org.eclipse.dltk.ui.text.completion.ContentAssistPreference;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.jface.text.DefaultIndentLineAutoEditStrategy;
import org.eclipse.jface.text.IAutoEditStrategy;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.util.PropertyChangeEvent;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.texteditor.ITextEditor;

public class LuaSourceViewerConfiguration extends
		ScriptSourceViewerConfiguration {

	private AbstractScriptScanner fCodeScanner;
	private AbstractScriptScanner fStringScanner;
	private AbstractScriptScanner fSingleQuoteStringScanner;
	private AbstractScriptScanner fCommentScanner;
	private AbstractScriptScanner fMultilineCommentScanner;

	public LuaSourceViewerConfiguration(IColorManager colorManager,
			IPreferenceStore preferenceStore, ITextEditor editor,
			String partitioning) {
		super(colorManager, preferenceStore, editor, partitioning);
	}

	public IAutoEditStrategy[] getAutoEditStrategies(
			ISourceViewer sourceViewer, String contentType) {
		return new IAutoEditStrategy[] { new DefaultIndentLineAutoEditStrategy() };
	}

	@Override
	protected ContentAssistPreference getContentAssistPreference() {
		return LuaContentAssistPreference.getDefault();
	}

	public String[] getIndentPrefixes(ISourceViewer sourceViewer,
			String contentType) {
		return new String[] { "\t", "        " };
	}

	@Override
	protected IInformationControlCreator getOutlinePresenterControlCreator(
			ISourceViewer sourceViewer, final String commandId) {
		return new IInformationControlCreator() {

			/**
			 * Returns empty object
			 */
			@Override
			public IInformationControl createInformationControl(Shell parent) {
				return new HierarchyInformationControl(parent, 0, 0) {

					@Override
					protected IPreferenceStore getPreferenceStore() {
						return null;
					}
				};
			}
		};
	}

	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new ScriptPresentationReconciler();
		reconciler.setDocumentPartitioning(this
				.getConfiguredDocumentPartitioning(sourceViewer));

		DefaultDamagerRepairer dr;
		dr = new DefaultDamagerRepairer(this.fCodeScanner);
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		dr = new DefaultDamagerRepairer(this.fStringScanner);
		reconciler.setDamager(dr, ILuaPartitions.LUA_STRING);
		reconciler.setRepairer(dr, ILuaPartitions.LUA_STRING);

		dr = new DefaultDamagerRepairer(this.fSingleQuoteStringScanner);
		reconciler.setDamager(dr, ILuaPartitions.LUA_SINGLE_QUOTE_STRING);
		reconciler.setRepairer(dr, ILuaPartitions.LUA_SINGLE_QUOTE_STRING);

		dr = new DefaultDamagerRepairer(this.fCommentScanner);
		reconciler.setDamager(dr, ILuaPartitions.LUA_COMMENT);
		reconciler.setRepairer(dr, ILuaPartitions.LUA_COMMENT);

		dr = new DefaultDamagerRepairer(this.fMultilineCommentScanner);
		reconciler.setDamager(dr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);
		reconciler.setRepairer(dr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);

		return reconciler;
	}

	/**
	 * This method is called from base class.
	 */
	protected void initializeScanners() {
		// This is our code scanner
		this.fCodeScanner = new LuaCodeScanner(this.getColorManager(),
				this.fPreferenceStore);

		// This is default scanners for partitions with same color.
		this.fStringScanner = new SingleTokenScriptScanner(this
				.getColorManager(), this.fPreferenceStore,
				ILuaColorConstants.LUA_STRING);
		this.fSingleQuoteStringScanner = new SingleTokenScriptScanner(this
				.getColorManager(), this.fPreferenceStore,
				ILuaColorConstants.LUA_STRING);
		this.fMultilineCommentScanner = new SingleTokenScriptScanner(this
				.getColorManager(), this.fPreferenceStore,
				ILuaColorConstants.LUA_MULTI_LINE_COMMENT);
		this.fCommentScanner = new SingleTokenScriptScanner(this
				.getColorManager(), this.fPreferenceStore,
				ILuaColorConstants.LUA_SINGLE_LINE_COMMENT);
	}

	public void handlePropertyChangeEvent(PropertyChangeEvent event) {
		if (this.fCodeScanner.affectsBehavior(event)) {
			this.fCodeScanner.adaptToPreferenceChange(event);
		}
		if (this.fStringScanner.affectsBehavior(event)) {
			this.fStringScanner.adaptToPreferenceChange(event);
		}
		if (this.fSingleQuoteStringScanner.affectsBehavior(event)) {
			this.fSingleQuoteStringScanner.adaptToPreferenceChange(event);
		}
	}

	public boolean affectsTextPresentation(PropertyChangeEvent event) {
		return this.fCodeScanner.affectsBehavior(event)
				|| this.fStringScanner.affectsBehavior(event)
				|| this.fSingleQuoteStringScanner.affectsBehavior(event);
	}
}
