/**
 * 
 */
package org.keplerproject.ldt.debug.core.breakpoints;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;

/**
 * @author jasonsantos
 */
public class LuaRunToLineBreakpoint extends LuaLineBreakpoint {
	public LuaRunToLineBreakpoint(final IFile resource, final int lineNumber)
			throws CoreException {
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = ResourcesPlugin
						.getWorkspace()
						.getRoot()
						.createMarker(
								"org.eclipse.ldt.debug.core.markerType.lineBreakpoint");
				setMarker(marker);
				marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
				fSourceFile = resource;
			}
		};
		run(getMarkerRule(resource), runnable);
	}

	/**
	 * Returns whether this breakpoint is a run-to-line breakpoint
	 * 
	 * @return whether this breakpoint is a run-to-line breakpoint
	 */
	@Override
	public boolean isRunToLineBreakpoint() {
		return true;
	}

}
