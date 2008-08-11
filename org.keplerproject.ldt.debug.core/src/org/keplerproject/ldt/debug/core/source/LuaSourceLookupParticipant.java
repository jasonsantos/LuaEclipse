/**
 * 
 */
package org.keplerproject.ldt.debug.core.source;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.debug.core.sourcelookup.AbstractSourceLookupParticipant;
import org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant;
import org.keplerproject.ldt.debug.core.model.LuaStackFrame;

/**
 * @author jasonsantos
 */
public class LuaSourceLookupParticipant extends AbstractSourceLookupParticipant
		implements ISourceLookupParticipant {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.sourcelookup.ISourceLookupParticipant#getSourceName(java.lang.Object)
	 */
	public String getSourceName(Object object) throws CoreException {

		if (object instanceof LuaStackFrame) {
			LuaStackFrame frame = (LuaStackFrame) object;
			IPath path = frame.getProjectPath();

			return path.toPortableString();
		}

		return null;
	}

	/**
	 * @param path
	 * @param project
	 * @return
	 */
	private boolean isPathWithinProject(IPath path, IPath container) {
		return container.matchingFirstSegments(path) == container
				.segmentCount();
	}
}
