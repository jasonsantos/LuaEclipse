/**
 * 
 */
package org.keplerproject.ldt.debug.ui.breakpoints;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IAdaptable;
import org.eclipse.core.runtime.NullProgressMonitor;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ISuspendResume;
import org.eclipse.debug.ui.actions.IRunToLineTarget;
import org.eclipse.debug.ui.actions.RunToLineHandler;
import org.eclipse.jface.text.ITextSelection;
import org.eclipse.jface.viewers.ISelection;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.texteditor.ITextEditor;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;
import org.keplerproject.ldt.debug.core.breakpoints.LuaRunToLineBreakpoint;

/**
 * @author jasonsantos
 */
public class LuaRunToLineTargetAdapter implements IRunToLineTarget {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IRunToLineTarget#canRunToLine(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection,
	 *      org.eclipse.debug.core.model.ISuspendResume)
	 */
	public boolean canRunToLine(IWorkbenchPart part, ISelection selection,
			ISuspendResume target) {
		return target instanceof IDebugElement
				&& ((IDebugElement) target).getModelIdentifier().equals(
						LuaDebuggerPlugin.ID_LUA_DEBUG_MODEL);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.ui.actions.IRunToLineTarget#runToLine(org.eclipse.ui.IWorkbenchPart,
	 *      org.eclipse.jface.viewers.ISelection,
	 *      org.eclipse.debug.core.model.ISuspendResume)
	 */
	public void runToLine(IWorkbenchPart part, ISelection selection,
			ISuspendResume target) throws CoreException {

		IEditorPart editorPart = (IEditorPart) part;
		ITextEditor textEditor = (ITextEditor) editorPart;
		ITextSelection textSelection = (ITextSelection) selection;
		int lineNumber = textSelection.getStartLine() + 1;
		if (lineNumber > 0) {
			if (target instanceof IAdaptable) {
				IDebugTarget debugTarget = (IDebugTarget) ((IAdaptable) target)
						.getAdapter(IDebugTarget.class);
				if (debugTarget != null) {
					IFile resource = (IFile) textEditor.getEditorInput()
							.getAdapter(IResource.class);
					IBreakpoint breakpoint = new LuaRunToLineBreakpoint(
							resource, lineNumber);
					RunToLineHandler handler = new RunToLineHandler(
							debugTarget, target, breakpoint);
					handler.run(new NullProgressMonitor());
				}
			}
		}

	}

}
