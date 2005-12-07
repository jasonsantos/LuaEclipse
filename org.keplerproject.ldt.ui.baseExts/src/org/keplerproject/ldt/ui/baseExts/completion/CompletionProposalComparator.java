package org.keplerproject.ldt.ui.baseExts.completion;

import java.util.Comparator;
import org.eclipse.jface.text.contentassist.CompletionProposal;
import org.eclipse.jface.text.contentassist.ICompletionProposal;

public class CompletionProposalComparator
    implements Comparator
{

    public CompletionProposalComparator()
    {
    }

    public int compare(Object o1, Object o2)
    {
        ICompletionProposal prop1 = (CompletionProposal)o1;
        ICompletionProposal prop2 = (CompletionProposal)o2;
        return prop1.getDisplayString().compareTo(prop2.getDisplayString());
    }
}