// Decompiled by DJ v3.7.7.81 Copyright 2004 Atanas Neshkov  Date: 27/9/2005 10:30:06
// Home Page : http://members.fortunecity.com/neshkov/dj.html  - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   CompletionProposalComparator.java

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