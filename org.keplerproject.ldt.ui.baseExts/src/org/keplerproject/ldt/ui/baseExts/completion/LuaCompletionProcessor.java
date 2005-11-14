package org.keplerproject.ldt.ui.baseExts.completion;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

import luajava.LuaState;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.keplerproject.ldt.core.LuaCorePlugin;
import org.keplerproject.ldt.ui.baseExts.BaseExtsPlugin;
import org.keplerproject.ldt.ui.baseExts.scanner.ILuaSyntax;
import org.keplerproject.ldt.ui.baseExts.scanner.LuaVariableDetector;

// Referenced classes of package luaeclipse.internal.ui.text.lua:
//            CompletionProposalComparator

public class LuaCompletionProcessor implements IContentAssistProcessor,
		ILuaSyntax {

	protected ArrayList proposalList;

	public LuaCompletionProcessor() {
		proposalList = new ArrayList();
	}

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer,
			int documentOffset) {
		LuaVariableDetector wordPart = new LuaVariableDetector(viewer,
				documentOffset);
		LuaState L = BaseExtsPlugin.getDefault().getLuaState();

		try {
			if (wordPart.getVariable() != null)
				L.doString("return " + wordPart.getVariable());
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

					// load function.gif, table.gif or string.gif
					image = LuaCorePlugin.getDefault().getImageRegistry().get(
							L.typeName(L.type(-1)));

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
			/*
			 * for(; L.next(-2) != 0; L.pop(1)) { String key = L.toString(-2);
			 * if(key.startsWith(wordPart.getString())) {
			 * org.eclipse.swt.graphics.Image image = null; image =
			 * LDTUIPlugin.getDefault().getImageRegistry().get(L.typeName(L.type(-1)));
			 * IContextInformation info = new ContextInformation(key,
			 * getContentInfoString(key)); ICompletionProposal proposal = new
			 * CompletionProposal(key, wordPart.getOffset(),
			 * wordPart.getString().length(), key.length(), image, key, info,
			 * getContentInfoString(key)); proposalList.add(proposal); } }
			 */

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
		char result[] = new char[1];
		result[0] = '.';
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