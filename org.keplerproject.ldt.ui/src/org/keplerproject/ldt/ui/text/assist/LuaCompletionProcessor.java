package org.keplerproject.ldt.ui.text.assist;

import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;
import org.eclipse.jface.text.contentassist.IContentAssistProcessor;
import org.eclipse.jface.text.contentassist.IContextInformation;
import org.eclipse.jface.text.contentassist.IContextInformationValidator;
import org.keplerproject.ldt.ui.editors.ILuaSyntax;

public class LuaCompletionProcessor implements IContentAssistProcessor, ILuaSyntax{

	public ICompletionProposal[] computeCompletionProposals(ITextViewer viewer, int offset) {
		ICompletionProposal proposal = new CompletionProposal("TESTANDO",offset,"TESTANDO".length(),offset);
		return new ICompletionProposal[]{proposal};
	}

	public IContextInformation[] computeContextInformation(ITextViewer viewer, int offset) {
		// TODO Auto-generated method stub
		return null;
	}

	public char[] getCompletionProposalAutoActivationCharacters() {
		
		return new char[]{'.'};
	}

	public char[] getContextInformationAutoActivationCharacters() {
		// TODO Auto-generated method stub
		return new char[]{'.'};
	}

	public String getErrorMessage() {
		// TODO Auto-generated method stub
		return null;
	}

	public IContextInformationValidator getContextInformationValidator() {
		// TODO Auto-generated method stub
		return null;
	}

}
