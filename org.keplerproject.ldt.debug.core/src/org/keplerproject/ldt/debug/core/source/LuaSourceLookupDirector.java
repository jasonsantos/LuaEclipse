/**
 * 
 */
package org.keplerproject.ldt.debug.core.source;

import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupDirector;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;

/**
 * @author jasonsantos
 */
public class LuaSourceLookupDirector extends AbstractSourceLookupDirector {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupDirector#initializeParticipants()
	 */
	public void initializeParticipants() {

		addParticipants(new ISourceLookupParticipant[] { new LuaSourceLookupParticipant() });

	}

}
