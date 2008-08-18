/**
 * 
 */
package org.keplerproject.ldt.debug.ui.presentation;

import org.eclipse.core.resources.IFile;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.ILineBreakpoint;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.ui.IDebugModelPresentation;
import org.eclipse.debug.ui.IValueDetailListener;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.part.FileEditorInput;
import org.keplerproject.ldt.debug.core.breakpoints.LuaLineBreakpoint;
import org.keplerproject.ldt.debug.core.model.LuaDebugTarget;
import org.keplerproject.ldt.debug.core.model.LuaDebugThread;
import org.keplerproject.ldt.debug.core.model.LuaStackFrame;

/**
 * @author jasonsantos
 */
public class LuaDebugModelPresentation extends LabelProvider implements
		IDebugModelPresentation {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.jface.viewers.LabelProvider#getText(java.lang.Object)
	 */
	@Override
	public String getText(Object element) {
		if (element instanceof LuaDebugTarget)
			return getTargetText((LuaDebugTarget) element);
		if (element instanceof LuaDebugThread)
			return getThreadText((LuaDebugThread) element);
		if (element instanceof LuaStackFrame)
			return getStackFrameText((LuaStackFrame) element);
		// TODO: report Watchpoint text

		return super.getText(element);
	}

	/**
	 * @param element
	 * @return
	 */
	private String getStackFrameText(LuaStackFrame frame) {
		try {
			return frame.getName() + " (line: " + frame.getLineNumber() + ")";
		} catch (DebugException e) {
		}
		return null;

	}

	/**
	 * @param element
	 * @return
	 */
	private String getThreadText(LuaDebugThread thread) {
		String label = thread.getName();
		if (thread.isStepping()) {
			label += " (stepping)";
		} else if (thread.isSuspended()) {
			IBreakpoint[] breakpoints = thread.getBreakpoints();
			if (breakpoints.length == 0) {
				if (thread.getError() == null) {
					label += " (suspended)";
				} else {
					label += " (" + thread.getError() + ")";
				}
			} else {
				IBreakpoint breakpoint = breakpoints[0];
				if (breakpoint instanceof LuaLineBreakpoint) {
					LuaLineBreakpoint luaLineBreakpoint = (LuaLineBreakpoint) breakpoint;
					if (luaLineBreakpoint.isRunToLineBreakpoint()) {
						label += " (run to line)";
					} else {
						label += " (suspended at line breakpoint)";
					}
					// TODO: figure out what is a watchpoint
				}
			}
		} else if (thread.isTerminated()) {
			label = "<terminated> " + label;
		}
		return label;
	}

	/**
	 * @param element
	 * @return
	 */
	private String getTargetText(LuaDebugTarget element) {
		return "Lua Script";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.IDebugModelPresentation#computeDetail(org.eclipse.debug.core.model.IValue,
	 *      org.eclipse.debug.ui.IValueDetailListener)
	 */

	public void computeDetail(IValue value, IValueDetailListener listener) {
		String detail = "";
		try {

			detail = value.getValueString();

		} catch (DebugException e) {
		}

		listener.detailComputed(value, detail);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.IDebugModelPresentation#setAttribute(java.lang.String,
	 *      java.lang.Object)
	 */

	public void setAttribute(String attribute, Object value) {
		// TODO Auto-generated method stub

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ISourcePresentation#getEditorId(org.eclipse.ui.IEditorInput,
	 *      java.lang.Object)
	 */

	public String getEditorId(IEditorInput input, Object element) {
		if (element instanceof IFile || element instanceof ILineBreakpoint) {
			return "org.keplerproject.ldt.ui.editors.LuaEditor";
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.ISourcePresentation#getEditorInput(java.lang.Object)
	 */

	public IEditorInput getEditorInput(Object element) {
		if (element instanceof IFile) {
			return new FileEditorInput((IFile) element);
		}
		if (element instanceof ILineBreakpoint) {
			return new FileEditorInput((IFile) ((ILineBreakpoint) element)
					.getMarker().getResource());
		}

		return null;
	}

}
