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

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.Stack;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.jface.text.BadLocationException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITypedRegion;
import org.eclipse.jface.text.Position;
import org.eclipse.jface.text.reconciler.DirtyRegion;
import org.eclipse.jface.text.reconciler.IReconcilingStrategy;
import org.eclipse.jface.text.reconciler.IReconcilingStrategyExtension;
import org.eclipse.swt.widgets.Display;
import org.keplerproject.ldt.ui.editors.lex.Scanner;
import org.keplerproject.ldt.ui.editors.lex.Symbol;
import org.keplerproject.ldt.ui.editors.lex.sym;

/**
 * A embeded Folding Reconcilier. One day a put it out of here. for now fold
 * just functions and multiline comments.
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaFoldingReconcilingStrategy implements IReconcilingStrategy,
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
			//symbol.left -1 (because jflex return line starting at 1 instead of 0)
			IRegion lineRegion = doc.getLineInformation(symbol.left -1);
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
					emitPosition(lReg.getOffset(), lineRegion
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