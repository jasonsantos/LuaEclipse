/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;

/**
 * @author jasonsantos Heavily based in source code from IBM Corporation and
 *         Bjorn Freeman-Benson made available under the EPL software license
 */
public class LuaDebugThread extends LuaDebugElement implements IThread,
		ILuaEventListener {

	/**
	 * Breakpoint this thread is suspended at or <code>null</code> if none.
	 */
	private IBreakpoint							fBreakpoint;

	/**
	 * Whether this thread is stepping
	 */
	private boolean								fStepping	= false;

	/**
	 * Whether this thread is suspended
	 */
	private boolean								fSuspended	= false;

	/**
	 * Most recent error event or <code>null</code>
	 */
	private String								fErrorEvent;

	/**
	 * Stack data obtained during a stack request event
	 */
	private String								fStackData;

	private IStackFrame[]						fStackFrames;

	/**
	 * Table mapping stack frames to current variables
	 */
	private final Map<IStackFrame, IVariable[]>	fVariables	= new HashMap<IStackFrame, IVariable[]>();

	private String								fFramesData;

	public LuaDebugThread(LuaDebugTarget target) {
		super(target);
		getLuaDebugTarget().addEventListener(this);
	}

	/**
	 * Returns the current variables for the given stack frame, or
	 * <code>null</code> if none.
	 * 
	 * @param frame
	 *            stack frame
	 * @return variables or <code>null</code>
	 */
	protected IVariable[] getVariables(IStackFrame frame) {
		synchronized (fVariables) {
			IVariable[] variables = fVariables.get(frame);
			if (variables == null) {
				return new IVariable[0];
			}
			return variables;
		}
	}

	/**
	 * Sets the current variables for the given stack frame. Called by the Lua
	 * stack frame when it is created.
	 * 
	 * @param frame
	 * @param variables
	 */
	protected void setVariables(IStackFrame frame, IVariable[] variables) {
		synchronized (fVariables) {
			fVariables.put(frame, variables);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getBreakpoints()
	 */
	@Override
	public IBreakpoint[] getBreakpoints() {
		if (fBreakpoint == null) {
			return new IBreakpoint[0];
		}
		return new IBreakpoint[] { fBreakpoint };
	}

	/**
	 * Notifies this thread it has been suspended by the given breakpoint.
	 * 
	 * @param breakpoint
	 *            breakpoint
	 */
	public void suspendedBy(IBreakpoint breakpoint) {
		fBreakpoint = breakpoint;
		suspended(DebugEvent.BREAKPOINT);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getName()
	 */
	@Override
	public String getName() {
		return "Lua Main Thread";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getPriority()
	 */
	@Override
	public int getPriority() {
		return 0; // irrelevant
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getStackFrames()
	 */
	@Override
	public IStackFrame[] getStackFrames() throws DebugException {
		if (isSuspended()) {
			System.out.println("------------------------->" + fFramesData);
			if (fFramesData != null && fFramesData.startsWith("101")) {
				fStackFrames = parseFramesData(fFramesData);
				return fStackFrames;
			}

		}
		return new IStackFrame[0];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#getTopStackFrame()
	 */
	@Override
	public IStackFrame getTopStackFrame() throws DebugException {
		IStackFrame[] frames = getStackFrames();
		if (frames.length > 0) {
			return frames[0];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IThread#hasStackFrames()
	 */
	@Override
	public boolean hasStackFrames() throws DebugException {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.core.runtime.IAdaptable#getAdapter(java.lang.Class)
	 */
	@Override
	public Object getAdapter(Class adapter) {
		if (adapter == IThread.class) {
			return this;
		}

		return super.getAdapter(adapter);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canResume()
	 */
	public boolean canResume() {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canSuspend()
	 */
	public boolean canSuspend() {
		return !isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#isSuspended()
	 */
	public boolean isSuspended() {
		return fSuspended && !isTerminated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#resume()
	 */
	public void resume() throws DebugException {
		try {
			System.out.println(sendRequest("RUN"));
		} catch (Exception e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#suspend()
	 */
	public void suspend() throws DebugException {
		try {
			System.out.println(sendRequest("STEP"));
		} catch (IOException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepInto()
	 */
	public boolean canStepInto() {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepOver()
	 */
	public boolean canStepOver() {
		return isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepReturn()
	 */
	public boolean canStepReturn() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#isStepping()
	 */
	public boolean isStepping() {
		return fStepping;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepInto()
	 */
	public void stepInto() throws DebugException {
		try {
			System.out.println(sendRequest("STEP"));
		} catch (IOException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepOver()
	 */
	public void stepOver() throws DebugException {
		try {
			System.out.println(sendRequest("OVER"));
		} catch (IOException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepReturn()
	 */
	public void stepReturn() throws DebugException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		return !isTerminated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated() {
		return getDebugTarget().isTerminated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public void terminate() throws DebugException {
		try {
			System.out.println(sendRequest("EXIT"));
		} catch (IOException e) {
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.keplerproject.ldt.debug.core.model.ILuaEventListener#handleEvent(java.lang.String)
	 */
	@Override
	public void handleEvent(String event) {
		// clear previous state
		fBreakpoint = null;
		setStepping(false);

		// handle events
		if (event.startsWith("200")) {
			setSuspended(false);
			resumed(DebugEvent.STEP_OVER);

		} else if (event.startsWith("202")) {
			setSuspended(true);
			suspended(DebugEvent.STEP_END);
			try {
				fFramesData = sendRequest("STACK");
			} catch (Exception e) {
			}
		} else if (event.startsWith("101")) {
			if (!event.equals(fStackData)) {
				fStackFrames = parseFramesData(event);
			}
			fStackData = event;

		} else if (event.equals("started")) {
			fireCreationEvent();
		} else {
			setError(event);
		}
	}

	/**
	 * @param event
	 * @return
	 */
	public IStackFrame[] parseFramesData(String event) {
		if (event != null && event.startsWith("101")) {
			String framesData = event.substring("101 Stack ".length());
			String[] frames = framesData.split("#");
			IStackFrame[] theFrames = new IStackFrame[frames.length];
			for (int i = 0; i < frames.length; i++) {
				String data = frames[i];
				theFrames[frames.length - i - 1] = new LuaStackFrame(this,
						data, i);
			}
			return theFrames;
		} else
			return new IStackFrame[0];

	}

	/**
	 * Sets whether this thread is stepping
	 * 
	 * @param stepping
	 *            whether stepping
	 */
	private void setStepping(boolean stepping) {
		fStepping = stepping;
	}

	/**
	 * Sets whether this thread is suspended
	 * 
	 * @param suspended
	 *            whether suspended
	 */
	private void setSuspended(boolean suspended) {
		fSuspended = suspended;
	}

	/**
	 * Sets the most recent error event encountered, or <code>null</code> to
	 * clear the most recent error
	 * 
	 * @param event
	 *            one of 'unimpinstr' or 'nosuchlabel' or <code>null</code>
	 */
	private void setError(String event) {
		fErrorEvent = event;
	}

	/**
	 * Returns the most revent error event encountered since the last suspend,
	 * or <code>null</code> if none.
	 * 
	 * @return the most revent error event encountered since the last suspend,
	 *         or <code>null</code> if none
	 */
	public Object getError() {
		return fErrorEvent;
	}

	/**
	 * Notification the target has resumed for the given reason. Clears any
	 * error condition that was last encountered and fires a resume event, and
	 * clears all cached variables for stack frames.
	 * 
	 * @param detail
	 *            reason for the resume
	 */
	private void resumed(int detail) {
		setError(null);
		synchronized (fVariables) {
			fVariables.clear();
		}
		fireResumeEvent(detail);
	}

	/**
	 * Notification the target has suspended for the given reason
	 * 
	 * @param detail
	 *            reason for the suspend
	 */
	private void suspended(int detail) {
		fireSuspendEvent(detail);
	}

	/**
	 * Notification an error was encountered. Fires a breakpoint suspend event.
	 */
	private void exceptionHit() {
		suspended(DebugEvent.BREAKPOINT);
	}

}
