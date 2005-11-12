package org.keplerproject.ldt.ui.text.lua;

import org.eclipse.jface.text.contentassist.ICompletionProposal;


/**
 * A completion proposal with a relevance value.
 * The relevance value is used to sort the completion proposals. Proposals with higher relevance
 * should be listed before proposals with lower relevance.
 *
 * @see org.eclipse.jface.text.contentassist.ICompletionProposal
 * @since 1.0
 */
public interface ILuaCompletionProposal extends ICompletionProposal {
    /**
     * Returns the relevance of this completion proposal.
     * <p>
     * The relevance is used to determine if this proposal is more
     * relevant than another proposal.</p>
     *
     * @return the relevance of this completion proposal in the range of [0, 100]
     */
    int getRelevance();
}
