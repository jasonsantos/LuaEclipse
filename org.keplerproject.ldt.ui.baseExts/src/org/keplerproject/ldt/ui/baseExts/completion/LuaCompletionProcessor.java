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
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.resource.ImageRegistry;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ContextInformation;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.eclipse.swt.graphics.Image;
import org.keplerproject.ldt.core.LuaScriptsSpecs;
import org.keplerproject.ldt.ui.baseExts.BaseExtsPlugin;
import org.keplerproject.ldt.ui.baseExts.scanner.ILuaSyntax;
import org.keplerproject.ldt.ui.baseExts.scanner.LuaVariableDetector;
import org.keplerproject.luajava.LuaState;

/**
 * A simple Lua Completion Processor thats use the default Lua Packages to
 * assist the programmer.
 * 
 * @author guilherme
 * @version $Id: LuaCompletionProcessor.java,v 1.8 2007/05/20 23:17:09 guilherme
 *          Exp $
 */
public class LuaCompletionProcessor implements IContentAssistProcessor, ILuaSyntax {

	protected ArrayList<ICompletionProposal>	proposalList;

	public LuaCompletionProcessor() {
		proposalList = new ArrayList<ICompletionProposal>();
	}

	@SuppressWarnings("unchecked")
	public ICompletionProposal[] computeCompletionProposals(
			final ITextViewer viewer, final int documentOffset) {

		LuaVariableDetector wordPart = new LuaVariableDetector(viewer, documentOffset);

		List<ICompletionProposal> internalProposalList = getInternalProposals(wordPart);
		proposalList.addAll(internalProposalList);

		List<ICompletionProposal> functionProposalList = getFunctionProposals(viewer.getDocument().get(), wordPart);
		proposalList.addAll(functionProposalList);

		Collections.sort(proposalList, new CompletionProposalComparator());

		ICompletionProposal result[] = new ICompletionProposal[proposalList.size()];
		result = proposalList.toArray(result);

		proposalList.clear();
		
		return result;
	}
	
	private List<ICompletionProposal> getInternalProposals(LuaVariableDetector wordPart) {
		LuaState L = LuaScriptsSpecs.getDefault().getLuaState();
		
		if (L == null) {
			return Collections.emptyList();
		}
		
		try {
			if (wordPart.getVariable() != null) {
				L.LdoString("return " + wordPart.getVariable());
			} else {
				L.getGlobal("_G");
			}
		} catch (Throwable e) {
			e.printStackTrace();
			return Collections.emptyList();
		}
		
		if (!L.isTable(-1)) {
			L.pop(1);
			return Collections.emptyList();
		}

		String 	stem = wordPart.getString();
		int 	wordOffset = wordPart.getOffset();

		ImageRegistry imageRegistry = BaseExtsPlugin.getDefault().getImageRegistry(); 

		ArrayList<ICompletionProposal> internalList = new ArrayList<ICompletionProposal>();

		L.pushNil();
		while (L.next(-2) != 0) {
			String key = L.toString(-2);

			if (!key.startsWith(wordPart.getString())) {
				
				String contextKey = null;
				if (wordPart.getVariable() != null) {
					contextKey = wordPart.getVariable() + "." + key;
				} else {
					contextKey = key;
				}
				
				String strLuaType = L.typeName(L.type(-1));
				// load function.gif, table.gif or string.gif
				Image image = imageRegistry.get(strLuaType);

				int cursorLocation = key.length();
					
				// To functions, use de ().
				if (FUNCTION_TYPE_NAME.equals(strLuaType)) {
					key += "()";
					cursorLocation += 1; 		//Put cursor inside "()"
				}

				IContextInformation info;
				info = new ContextInformation(contextKey, getContentInfoString(contextKey));

				ICompletionProposal proposal = new CompletionProposal(
						key,											//Replacement string
						wordOffset, stem.length(),						//Replacement offset & length
						cursorLocation, 								//Cursor location relative to offset
						image,											//Display image
						key, 											//Display string
						info, 											//Context information
						getContentInfoString(key));						//Extra string information

				internalList.add(proposal);
			}
			L.pop(1); // removes `value'; keeps `key' for next iteration
		}
		L.pop(1);	
		
		return internalList;
	}
	
	static final Pattern fLuaFunctionPattern = Pattern.compile("^\\w*function\\s+(\\w+)\\s*\\(.*$", Pattern.MULTILINE);
	protected ArrayList<ICompletionProposal> getFunctionProposals(String fileContents, LuaVariableDetector wordPart) {
		ICompletionProposal proposal;

		String 	stem = wordPart.getString();
		int 	wordOffset = wordPart.getOffset();
		
		ArrayList<ICompletionProposal> functionList = new ArrayList<ICompletionProposal>();
		
		Matcher matcher = fLuaFunctionPattern.matcher(fileContents);
		int offset = 0;
//		int startOffset = 0;
		while(matcher.find(offset)) {
			//startOffset = matcher.start();
			offset = matcher.end();
			String functionName = matcher.group(1);
			
			if(!functionName.startsWith(stem)) {
				continue;
			}
			//We can't do this for all completions
			functionName += "()";
		
			//Put the cursor inside the brackets 
			int cursorLocation = functionName.length() - 1;
			
			ImageRegistry imageRegistry = BaseExtsPlugin.getDefault().getImageRegistry(); 
			Image displayImage = imageRegistry.get("function");

			String displayName = functionName;
			
			proposal = new CompletionProposal(functionName,		//Replacement string
					wordOffset, stem.length(),					//Replacement offset & length
					cursorLocation,								//Cursor location relative to offset
					displayImage,								//Display Image
					displayName,								//Display string
					null, 										//Context information
					null);									    //Extra string information
			
			functionList.add(proposal);
		}
	
		return functionList;
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
		char result[] = new char[] { '.', ':', ' '};
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