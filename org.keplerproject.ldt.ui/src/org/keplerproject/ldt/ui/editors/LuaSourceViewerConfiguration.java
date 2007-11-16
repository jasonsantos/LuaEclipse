/*
* Copyright (C) 2003-2007 Kepler Project.
*
* Permission is hereby granted, free of charge, to any person obtaining
* a copy of this software and associated documentation files (the
* "Software"), to deal in the Software without restriction, including
* without limitation the rights to use, copy, modify, merge, publish,
* distribute, sublicense, and/or sell copies of the Software, and to
* permit persons to whom the Software is furnished to do so, subject to
* the following conditions:
*
* The above copyright notice and this permission notice shall be
* included in all copies or substantial portions of the Software.
*
* THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
* EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
* MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
* IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
* CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
* TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
* SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
*/

package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.ui.IEditorPart;
import org.keplerproject.ldt.ui.LDTUIPlugin;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentAssistExtension;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentTypeExtension;
import org.keplerproject.ldt.ui.editors.ext.ILuaReconcilierExtension;

/**
 * The lua source viewer configuration. This class captures the
 * SourceViewerConfiguration Extension point.
 * 
 * @author Guilherme Martins
 * @author Thiago Ponte
 * @version $Id$
 * 
 */
public class LuaSourceViewerConfiguration extends SourceViewerConfiguration {
	private LuaDoubleClickStrategy doubleClickStrategy;

	private LuaColorManager colorManager;

	private LuaEditor editor;

	private LuaFoldingReconcilingStrategy strategy;

	public LuaSourceViewerConfiguration(LuaColorManager colorManager) {
		this.colorManager = colorManager;
	}

	/**
	 * get the configuredContent Types from the Extensions
	 * 
	 */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		List stringContentTypes = new ArrayList();
		String editorId = this.editor.getInstanceId();
		
		List extensions = LDTUIPlugin.getDefault().getContentTypeExtension(editorId);
		Iterator extIte = extensions.iterator();
		while (extIte.hasNext()) {
			ILuaContentTypeExtension ext = (ILuaContentTypeExtension) extIte
					.next();
			stringContentTypes.addAll(Arrays.asList(ext.getContentTypes()));
		}
		String[] resultContents = new String[stringContentTypes.size()];
		stringContentTypes.toArray(resultContents);
		return resultContents;
	}

	public void setColorManager(LuaColorManager colorManager) {
		this.colorManager = colorManager;
	}

	public void setEditor(IEditorPart editor) {
		if (editor instanceof LuaEditor) {
			this.editor = (LuaEditor) editor;
			
		}else
			throw new IllegalArgumentException("The editor part must be a LuaEditor");
		
	}

	/**
	 * This method return a simple DoubleClickStrategy. Just word selection.
	 */
	public ITextDoubleClickStrategy getDoubleClickStrategy(
			ISourceViewer sourceViewer, String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new LuaDoubleClickStrategy();
		return doubleClickStrategy;
	}

	/**
	 * get the Content Assist to the sourceviewer contributed by the extensions
	 */
	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		String editorId = this.editor.getInstanceId();
		List extensions = LDTUIPlugin.getDefault().getAssistExtension(editorId);
		Iterator extIte = extensions.iterator();
		while (extIte.hasNext()) {
			/*
			 * assistant.setContentAssistProcessor(new
			 * LuaCompletionProcessor(),IDocument.DEFAULT_CONTENT_TYPE);
			 */
			ILuaContentAssistExtension ext = (ILuaContentAssistExtension) extIte
					.next();
			ext.contribute(editor, assistant);
		}

		assistant.setAutoActivationDelay(400);
		assistant
				.setProposalPopupOrientation(ContentAssistant.CONTEXT_INFO_BELOW);
		assistant
				.setContextInformationPopupOrientation(ContentAssistant.CONTEXT_INFO_BELOW);
		assistant.setContextInformationPopupBackground(colorManager
				.getColor(new RGB(255, 255, 255)));
		assistant.setProposalSelectorBackground(colorManager.getColor(new RGB(
				255, 255, 255)));
		assistant.enableAutoActivation(true);

		return assistant;
	}

	/**
	 * get the presentation reconcilier from the extensions
	 */
	public IPresentationReconciler getPresentationReconciler(
			ISourceViewer sourceViewer) {
		String editorId = this.editor.getInstanceId();
		PresentationReconciler reconciler = new PresentationReconciler();
		List extensions = LDTUIPlugin.getDefault().getReconcilierExtension(editorId);
		Iterator extIte = extensions.iterator();
		reconciler.install(sourceViewer);
		while (extIte.hasNext()) {
			/*
			 * DefaultDamagerRepairer dr = new DefaultDamagerRepairer(scanner);
			 * reconciler.setDamager(dr, "__lua_multiline_comment");
			 * reconciler.setRepairer(dr, "__lua_multiline_comment");
			 */
			ILuaReconcilierExtension ext = (ILuaReconcilierExtension) extIte
					.next();
			ext.contribute(colorManager, reconciler, sourceViewer);
		}
		return reconciler;
	}

	/**
	 * Source Reconcilier to the folding feature
	 */
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		if (strategy == null) {
			strategy = new LuaFoldingReconcilingStrategy();
			strategy.setEditor(editor);
		}

		MonoReconciler reconciler = new MonoReconciler(strategy, false);
		//XXX we can this in brazil as BACALHAU.. 'saltfish'
		//reconciler.setDelay(5000);
		//reconciler.setIsIncrementalReconciler(true);
		/*
		 * reconciler.setReconcilingStrategy(strategy,
		 * IDocument.DEFAULT_CONTENT_TYPE);
		 */
		return reconciler;
	}


	public ITextHover getTextHover(ISourceViewer sourceViewer, String contentType) {
		return new LuaTextHover();
	}
}