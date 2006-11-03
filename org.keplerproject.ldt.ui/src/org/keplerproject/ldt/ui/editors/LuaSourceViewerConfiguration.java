/**********************************
 * 
 *************************/
package org.keplerproject.ldt.ui.editors;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextDoubleClickStrategy;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.jface.text.contentassist.IContentAssistant;
import org.eclipse.jface.text.presentation.IPresentationReconciler;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconciler;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.jface.text.reconciler.MonoReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.SourceViewerConfiguration;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.IEditorPart;
import org.keplerproject.ldt.ui.LDTUIPlugin;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentAssistExtension;
import org.keplerproject.ldt.ui.editors.ext.ILuaContentTypeExtension;
import org.keplerproject.ldt.ui.editors.ext.ILuaReconcilierExtension;
import org.keplerproject.ldt.ui.editors.lex.Scanner;
import org.keplerproject.ldt.ui.editors.lex.Symbol;
import org.keplerproject.ldt.ui.editors.lex.sym;

/**
 * The lua source viewer configuration. This class captures the
 * SourceViewerConfiguration Extension point.
 * 
 * @author Guilherme Martins
 * @author Thiago Ponte
 * 
 */
public class LuaSourceViewerConfiguration extends SourceViewerConfiguration {
	private LuaDoubleClickStrategy doubleClickStrategy;

	private LuaColorManager colorManager;

	private IEditorPart editor;

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

	/**
	 * Source Reconcilier to the folding feature
	 */
	public IReconciler getReconciler(ISourceViewer sourceViewer) {
		if (strategy == null) {
			strategy = new LuaFoldingReconcilingStrategy();
			strategy.setEditor((LuaEditor) editor);
		}

		MonoReconciler reconciler = new MonoReconciler(strategy, false);
		/*
		 * reconciler.setReconcilingStrategy(strategy,
		 * IDocument.DEFAULT_CONTENT_TYPE);
		 */
		return reconciler;
	}

	/**
	 * A embeded Folding Reconcilier. One day a put it out of here. for now fold
	 * just functions and multiline comments.
	 * 
	 * @author guilherme
	 */
	class LuaFoldingReconcilingStrategy implements IReconcilingStrategy,
			IReconcilingStrategyExtension {
		private LuaEditor reditor;

		private IDocument doc;

		private ArrayList fPositions = new ArrayList();

		private int fOffset;

		private int fRangeEnd;

		private int cNextPos;

		public void setDocument(IDocument document) {
			this.doc = document;
		}

		public void reconcile(DirtyRegion dirtyRegion, IRegion subRegion) {
			initialReconcile();
		}

		public void reconcile(IRegion partition) {
			initialReconcile();
			// try
			// {
			// System.out.println(doc.get(partition.getOffset(),
			// partition.getLength()));
			// }
			// catch (BadLocationException e)
			// {
			// // TODO Auto-generated catch block
			// e.printStackTrace();
			// }
		}

		public void setProgressMonitor(IProgressMonitor monitor) {
			// TODO No progress.

		}

		public void initialReconcile() {
			try {
				fOffset = 0;
				fRangeEnd = doc.getLength();
				calculatePositions();

			} catch (Exception e) {
				// Do nothing..
				return;
			}
		}

		protected void calculatePositions() {
			if (reditor.isDirty())
				return;

			fPositions.clear();
			cNextPos = fOffset;
			// String contType;
			// ITypedRegion region;
			try {
				findNextFunction(cNextPos);
			} catch (BadLocationException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			Display.getDefault().asyncExec(new Runnable() {
				public void run() {
					reditor.updateFoldingStructure(fPositions);
				}

			});
		}

		private void findNextFunction(int nextPos) throws BadLocationException,
				IOException {
			cNextPos = nextPos;
			// int funcInit = -1;

			Stack stk = new Stack();
			// boolean onMultStr = false;
			int onMultStr = 0;
			boolean elseIf = false;

			while (cNextPos < fRangeEnd) {
				ITypedRegion region = doc.getPartition(cNextPos);
				String contType = region.getType();
				if (contType.equals("__lua_multiline_comment")) {
					int breakCount = 0;
					if (region.getOffset() + region.getLength() + 1 < doc
							.getLength()) {
						char lineb1 = doc.getChar(region.getOffset()
								+ region.getLength());
						char lineb2 = doc.getChar(region.getOffset()
								+ region.getLength() + 1);

						if (lineb1 == '\r' && lineb2 == '\n')
							// Windows
							breakCount = 2;
						else if (lineb1 == '\r' || lineb1 == '\n')
							breakCount = 1;
					}

					emitPosition(region.getOffset(), region.getLength()
							+ breakCount);
					cNextPos += region.getLength() + 2;
				} else {
					cNextPos += region.getLength() + 2;
				}

			}

			Scanner scanner = new Scanner(new StringReader(doc.get()));
			Symbol symbol = scanner.yylex();

			if (symbol.sym == sym.EOF) {
				cNextPos += 2;
				return;
			}

			while (symbol.sym != sym.EOF) {
				IRegion lineRegion = doc.getLineInformation(symbol.left);
				if (onMultStr == 0) {
					if (symbol.sym == sym.DO || symbol.sym == sym.FUNCTION) {
						stk.push(new Object[] { lineRegion, symbol });
					} else if (symbol.sym == sym.THEN) {
						if (elseIf)
							elseIf = false;
						else
							stk.push(new Object[] { lineRegion, symbol });
					} else if (symbol.sym == sym.ELSEIF) {
						elseIf = true;
					} else if (symbol.sym == sym.END) {
						if (stk.empty())
							return;

						Object[] stkContent = (Object[]) stk.pop();
						IRegion lReg = (IRegion) stkContent[0];

						if (((Symbol) stkContent[1]).sym != sym.FUNCTION
								|| lReg.getOffset() == lineRegion.getOffset()) {
							symbol = scanner.yylex();
							continue;
						}

						int breakCount = 0;
						if (lineRegion.getOffset() + 3 + 1 < doc.getLength()) {
							char lineb1 = doc
									.getChar(lineRegion.getOffset() + 3);
							char lineb2 = doc
									.getChar(lineRegion.getOffset() + 3 + 1);

							if (lineb1 == '\r' && lineb2 == '\n')
								// Windows
								breakCount = 2;
							else if (lineb1 == '\r' || lineb1 == '\n')
								breakCount = 1;
						}
						// Dont ask me to explain this right now..
						emitPosition(lReg.getOffset() - 1, lineRegion
								.getOffset()
								+ lineRegion.getLength()
								- lReg.getOffset()
								+ breakCount);

					} else if (symbol.sym == sym.DBLBRACK) {
						onMultStr++;
					}
				} else if (symbol.sym == sym.DBRBRACK) {
					onMultStr--;
				} else if (symbol.sym == sym.DBLBRACK)
					onMultStr++;

				symbol = scanner.yylex();
			}

		}

		protected void emitPosition(int startOffset, int length) {
			fPositions.add(new Position(startOffset, length));
		}

		public void setEditor(LuaEditor editor) {
			this.reditor = editor;
		}

	}

	public LuaFoldingReconcilingStrategy getFoldingStrategy() {
		return strategy;
	}

}