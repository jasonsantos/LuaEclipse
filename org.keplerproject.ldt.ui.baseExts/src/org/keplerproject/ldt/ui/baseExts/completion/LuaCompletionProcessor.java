/*
 * Copyright (C) 2003-2007 Kepler Project. Permission is hereby granted, free of
 * charge, to any person obtaining a copy of this software and associated
 * documentation files (the "Software"), to deal in the Software without
 * restriction, including without limitation the rights to use, copy, modify,
 * merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to
 * the following conditions: The above copyright notice and this permission
 * notice shall be included in all copies or substantial portions of the
 * Software. THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO
 * EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES
 * OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER
 * DEALINGS IN THE SOFTWARE.
 */

package org.keplerproject.ldt.ui.baseExts.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.keplerproject.ldt.ui.baseExts.BaseExtsPlugin;
import org.keplerproject.ldt.ui.baseExts.scanner.ILuaSyntax;
import org.keplerproject.ldt.ui.baseExts.scanner.LuaVariableDetector;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

/**
 * A simple Lua Completion Processor thats use the default Lua Packages to
 * assist the programmer.
 * 
 * @author guilherme
 * @version $Id: LuaCompletionProcessor.java,v 1.8 2007/05/20 23:17:09 guilherme
 *          Exp $
 */
public class LuaCompletionProcessor implements IContentAssistProcessor,
		ILuaSyntax {

	protected ArrayList	proposalList;

	protected LuaState	L	= null;

	public LuaCompletionProcessor() {
		proposalList = new ArrayList();
		try {
			L = (LuaStateFactory.newLuaState());
			L.openLibs();
		} catch (Exception e) {
			System.out.println("Could not initialize LuaState:"
					+ e.getMessage());
		} catch (Error e) {
			System.out.println("Could not initialize LuaState:"
					+ e.getMessage());
		}
	}

	public ICompletionProposal[] computeCompletionProposals(
			final ITextViewer viewer, final int documentOffset) {
		if (L == null)
			return new ICompletionProposal[] {};

		LuaVariableDetector wordPart = new LuaVariableDetector(viewer,
				documentOffset);
		try {
			if (wordPart.getVariable() != null)
				L.LdoString("return " + wordPart.getVariable());
			else
				L.getGlobal("_G");
		} catch (Throwable e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		if (L.isTable(-1)) {
			L.pushNil();

			while (L.next(-2) != 0) {
				String key = L.toString(-2);

				if (key.startsWith(wordPart.getString())) {

					String contextKey = null;
					if (wordPart.getVariable() != null)
						contextKey = wordPart.getVariable() + "." + key;
					else
						contextKey = key;

					Image image = null;
					String strLuaType = L.typeName(L.type(-1));
					// load function.gif, table.gif or string.gif
					image = BaseExtsPlugin.getDefault().getImageRegistry().get(
							strLuaType);

					// To functions, use de ().
					if (FUNCTION_TYPE_NAME.equals(strLuaType)) {
						key += "()";
					}

					IContextInformation info = new ContextInformation(
							contextKey, getContentInfoString(contextKey));

					ICompletionProposal proposal = new CompletionProposal(key,
							wordPart.getOffset(),
							wordPart.getString().length(), key.length(), image,
							key, info, getContentInfoString(key));

					proposalList.add(proposal);
				}
				L.pop(1); // removes `value'; keeps `key' for next iteration
			}

		}
		L.pop(1);
		Collections.sort(proposalList, new CompletionProposalComparator());
		ICompletionProposal result[] = new ICompletionProposal[proposalList
				.size()];
		int index = 0;
		for (Iterator i = proposalList.iterator(); i.hasNext();) {
			result[index] = (CompletionProposal) i.next();
			index++;
		}

		proposalList.clear();
		return result;
	}

	private String getContentInfoString(String keyWord) {
		String resourceKey = "ContextString." + keyWord;
		String resourceString = resourceKey;
		if (resourceString == keyWord)
			resourceString = "No Context Info String";
		return resourceString;
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer,
			int documentOffset) {
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		char result[] = new char[] { '.', ':' };
		return result;
	}

	public char[] getContextInformationAutoActivationCharacters() {
		char result[] = new char[1];
		result[0] = '(';
		return result;
	}

	public String getErrorMessage() {
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		return null;
	}

}