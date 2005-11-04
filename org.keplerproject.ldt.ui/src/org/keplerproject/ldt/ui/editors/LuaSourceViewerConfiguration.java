package org.keplerproject.ldt.ui.editors;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;
import org.keplerproject.ldt.ui.text.assist.LuaCompletionProcessor;

public class LuaSourceViewerConfiguration extends SourceViewerConfiguration {
	private LuaDoubleClickStrategy doubleClickStrategy;
	//private XMLTagScanner tagScanner;
	//private LuaRuleBasedScanner scanner;
	private ColorManager colorManager;

	public LuaSourceViewerConfiguration(ColorManager colorManager) {
		this.colorManager = colorManager;
	}
	public String[] getConfiguredContentTypes(ISourceViewer sourceViewer) {
		return new String[] {
			IDocument.DEFAULT_CONTENT_TYPE};//,
			//ILuaPartitions.LUA_MULTI_LINE_COMMENT,
			//ILuaPartitions.LUA_SINGLE_LINE_COMMENT,
			//ILuaPartitions.LUA_STRING};
	}
	public ITextDoubleClickStrategy getDoubleClickStrategy(
		ISourceViewer sourceViewer,
		String contentType) {
		if (doubleClickStrategy == null)
			doubleClickStrategy = new LuaDoubleClickStrategy();
		return doubleClickStrategy;
	}

	public IContentAssistant getContentAssistant(ISourceViewer sourceViewer) {
		ContentAssistant assistant = new ContentAssistant();
		assistant.setContentAssistProcessor(new LuaCompletionProcessor(),IDocument.DEFAULT_CONTENT_TYPE);
		assistant.setAutoActivationDelay(400);
		assistant.setProposalPopupOrientation(ContentAssistant.CONTEXT_INFO_BELOW);
		assistant.setContextInformationPopupOrientation(ContentAssistant.CONTEXT_INFO_BELOW);
		assistant.setContextInformationPopupBackground(colorManager.getColor(new RGB(0,191,191)));
		assistant.enableAutoActivation(true);
		return assistant;
	}
	/*protected LuaRuleBasedScanner getXMLScanner() {
		if (scanner == null) {
			scanner = new LuaRuleBasedScanner(colorManager);
			scanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ILuaColorConstants.DEFAULT))));
		}
		return scanner;
	}*/
	/*protected XMLTagScanner getXMLTagScanner() {
		if (tagScanner == null) {
			tagScanner = new XMLTagScanner(colorManager);
			tagScanner.setDefaultReturnToken(
				new Token(
					new TextAttribute(
						colorManager.getColor(ILuaColorConstants.TAG))));
		}
		return tagScanner;
	}*/

	/*public IPresentationReconciler getPresentationReconciler(ISourceViewer sourceViewer) {
		PresentationReconciler reconciler = new PresentationReconciler();

		DefaultDamagerRepairer dr =
			new DefaultDamagerRepairer(getXMLTagScanner());
		reconciler.setDamager(dr, LuaBaseScanner.XML_TAG);
		reconciler.setRepairer(dr, LuaBaseScanner.XML_TAG);

		dr = new DefaultDamagerRepairer(getXMLScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ILuaColorConstants.XML_COMMENT)));
		reconciler.setDamager(ndr, LuaBaseScanner.XML_COMMENT);
		reconciler.setRepairer(ndr, LuaBaseScanner.XML_COMMENT);

		return reconciler;
	}*/

}