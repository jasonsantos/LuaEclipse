/**********************************
 * 
 *************************/
package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
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
 * 
 */
public class LuaSourceViewerConfiguration extends SourceViewerConfiguration {
	private LuaDoubleClickStrategy doubleClickStrategy;

	private LuaColorManager colorManager;

	private IEditorPart editor;

	public LuaSourceViewerConfiguration(LuaColorManager colorManager) {
		this.colorManager = colorManager;
	}

	/**
	 * get the configuredContent Types from the Extensions
	 * 
	 */
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		List stringContentTypes = new ArrayList();

		List extensions = LDTUIPlugin.getDefault().getContentTypeExtension();
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
		this.editor = editor;
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

		List extensions = LDTUIPlugin.getDefault().getAssistExtension();
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

		PresentationReconciler reconciler = new PresentationReconciler();
		List extensions = LDTUIPlugin.getDefault().getReconcilierExtension();
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
 
}