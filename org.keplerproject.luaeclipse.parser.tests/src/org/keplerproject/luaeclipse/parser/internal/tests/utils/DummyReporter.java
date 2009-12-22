/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: DummyReporter.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser.internal.tests.utils;

import org.eclipse.dltk.compiler.problem.IProblem;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

/**
 * The Class DummyReporter.
 * Is an empty reporter used for testing.
 */
public class DummyReporter implements IProblemReporter {

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.compiler.problem.IProblemReporter#reportProblem(org.eclipse.dltk.compiler.problem.IProblem)
	 */
	@Override
	public void reportProblem(IProblem problem) {
		// Do nothing
	}
	
	/* (non-Javadoc)
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	@SuppressWarnings("unchecked")
	public Object getAdapter(Class adapter) {
		// Empty Object
		return new Object();
	}

}
