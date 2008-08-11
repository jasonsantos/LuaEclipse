/**
 * 
 */
package org.keplerproject.ldt.debug.core.breakpoints;

import java.io.IOException;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IWorkspaceRunnable;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.LineBreakpoint;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;
import org.keplerproject.ldt.debug.core.model.ILuaEventListener;
import org.keplerproject.ldt.debug.core.model.LuaDebugTarget;
import org.keplerproject.ldt.debug.core.model.LuaDebugThread;

/**
 * @author jasonsantos
 */
public class LuaLineBreakpoint extends LineBreakpoint implements
		ILuaEventListener {

	protected IFile	fSourceFile;
	LuaDebugTarget	fTarget;

	/**
	 * Default constructor is required for the breakpoint manager to re-create
	 * persisted breakpoints. After instantiating a breakpoint, the
	 * <code>setMarker(...)</code> method is called to restore this
	 * breakpoint's attributes.
	 */
	public LuaLineBreakpoint() {
	}

	public LuaLineBreakpoint(final IFile resource, final int lineNumber)
			throws CoreException {
		IWorkspaceRunnable runnable = new IWorkspaceRunnable() {
			public void run(IProgressMonitor monitor) throws CoreException {
				IMarker marker = resource.createMarker("lua.lineBreakpoint");
				setMarker(marker);
				marker.setAttribute(IBreakpoint.ENABLED, Boolean.TRUE);
				marker.setAttribute(IMarker.LINE_NUMBER, lineNumber);
				marker.setAttribute(IBreakpoint.ID, getModelIdentifier());
				marker.setAttribute(IMarker.MESSAGE, "Line Breakpoint: "
						+ resource.getName() + " [line: " + lineNumber + "]");
				fSourceFile = resource;
			}
		};
		run(getMarkerRule(resource), runnable);
	}

	/**
	 * Returns the source file this breakpoint is contained in.
	 * 
	 * @return the source file this breakpoint is contained in
	 */
	public IFile getSourceFile() {
		return fSourceFile;
	}

	protected void notifyThread() {
		if (fTarget != null) {
			try {
				IThread[] threads = fTarget.getThreads();
				if (threads.length == 1) {
					LuaDebugThread thread = (LuaDebugThread) threads[0];
					thread.suspendedBy(this);
				}
			} catch (DebugException e) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.keplerproject.ldt.debug.core.model.ILuaEventListener#handleEvent(java.lang.String)
	 */
	@Override
	public void handleEvent(String event) {
		// TODO verify if this breakpoint was hit
		System.out.println(event);
		// notifyThread()
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IBreakpoint#getModelIdentifier()
	 */
	@Override
	public String getModelIdentifier() {
		return LuaDebuggerPlugin.ID_LUA_DEBUG_MODEL;
	}

	/**
	 * Returns whether this breakpoint is a run-to-line breakpoint
	 * 
	 * @return whether this breakpoint is a run-to-line breakpoint
	 */
	public boolean isRunToLineBreakpoint() {
		return false;
	}

	public void install(LuaDebugTarget target) throws CoreException {
		fTarget = target;
		target.addEventListener(this);
		createRequest(target);
	}

	protected void createRequest(LuaDebugTarget target) throws CoreException {
		try {
			target.sendRequest("SETB "
					+ fSourceFile.getLocation().toPortableString() + " "
					+ (getLineNumber() - 1));
		} catch (IOException e) {
			// TODO: throw something
		}
	}

	public void remove(LuaDebugTarget target) throws CoreException {
		target.removeEventListener(this);
		clearRequest(target);
		fTarget = null;

	}

	protected void clearRequest(LuaDebugTarget target) throws CoreException {

		try {
			target.sendRequest("DELB "
					+ fSourceFile.getLocation().toPortableString() + " "
					+ (getLineNumber() - 1));
		} catch (IOException e) {
			// TODO: throw something
		}
	}
}
