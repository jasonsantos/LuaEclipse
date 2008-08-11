/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.IOException;
import java.util.Vector;

import org.eclipse.core.resources.IMarker;
import org.eclipse.core.resources.IMarkerDelta;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.IBreakpointManagerListener;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IBreakpoint;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.IMemoryBlock;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IThread;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;
import org.keplerproject.ldt.debug.core.breakpoints.LuaLineBreakpoint;
import org.keplerproject.ldt.debug.core.breakpoints.LuaRunToLineBreakpoint;

/**
 * @author jasonsantos
 */
public class LuaDebugTarget extends LuaDebugElement implements IDebugTarget,
		IBreakpointManagerListener, ILuaEventListener {

	// process
	private final IProcess					fProcess;

	// containing launch object
	private final ILaunch					fLaunch;

	private final LuaDebugServer			fServer;

	// terminated state
	private boolean							fTerminated		= false;

	// threads
	private IThread[]						fThreads;
	private LuaDebugThread					fLuaDebugThread;

	private boolean							fStarted;

	// event listeners
	private final Vector<ILuaEventListener>	fEventListeners	= new Vector<ILuaEventListener>();

	/**
	 * @param launch
	 * @param proc
	 * @param controlPort
	 * @throws IOException
	 * @throws InterruptedException
	 */
	public LuaDebugTarget(ILaunch launch, IProcess proc, String scriptPath,
			LuaDebugServer server) throws CoreException {
		super(null);
		fLaunch = launch;
		fProcess = proc;
		fServer = server;

		addEventListener(this);

		fServer.register(this);

		fLuaDebugThread = new LuaDebugThread(this);
		// TODO abrir uma thread para cada resposta da aplicação
		fThreads = new IThread[] { fLuaDebugThread };

		IBreakpointManager breakpointManager = getBreakpointManager();
		breakpointManager.addBreakpointListener(this);
		breakpointManager.addBreakpointManagerListener(this);

		try {
			System.out.println(sendRequest("SETB " + scriptPath + " 1"));
			System.out.println(sendRequest("RUN"));
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	@Override
	public String sendRequest(String request) throws IOException,
			DebugException {
		synchronized (fServer) {
			return fServer.sendRequest(request);
		}

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDebugTarget#getName()
	 */
	@Override
	public String getName() throws DebugException {
		return "Lua";
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDebugTarget#getProcess()
	 */
	@Override
	public IProcess getProcess() {
		return fProcess;
	}

	public IThread getMainThread() {
		return fLuaDebugThread;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDebugTarget#getThreads()
	 */
	@Override
	public IThread[] getThreads() throws DebugException {
		return fThreads;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.DebugElement#getLaunch()
	 */
	@Override
	public ILaunch getLaunch() {
		return fLaunch;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDebugTarget#hasThreads()
	 */
	@Override
	public boolean hasThreads() throws DebugException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDebugTarget#supportsBreakpoint(org.eclipse.debug.core.model.IBreakpoint)
	 */
	@Override
	public boolean supportsBreakpoint(IBreakpoint breakpoint) {
		if (!isTerminated()
				&& breakpoint.getModelIdentifier().equals(getModelIdentifier())) {
			try {
				String program = getLaunch().getLaunchConfiguration()
						.getAttribute(LuaDebuggerPlugin.LUA_SCRIPT_ATTRIBUTE,
								(String) null);
				if (program != null) {
					IResource resource = null;
					if (breakpoint instanceof LuaRunToLineBreakpoint) {
						LuaRunToLineBreakpoint rtlb = (LuaRunToLineBreakpoint) breakpoint;
						resource = rtlb.getSourceFile();
					} else {
						IMarker marker = breakpoint.getMarker();
						if (marker != null)
							resource = marker.getResource();

					}

					if (resource != null) {
						return resource.getFullPath().equals(program);
					}
				}
			} catch (CoreException e) {
				e.printStackTrace();
			}
			return true;
		}
		return false;
	}

	/**
	 * Install breakpoints that are already registered with the breakpoint
	 * manager.
	 */
	private void installDeferredBreakpoints() {
		IBreakpoint[] breakpoints = getBreakpointManager().getBreakpoints(
				getModelIdentifier());
		for (int i = 0; i < breakpoints.length; i++) {
			breakpointAdded(breakpoints[i]);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.IBreakpointListener#breakpointAdded(org.eclipse.debug.core.model.IBreakpoint)
	 */
	public void breakpointAdded(IBreakpoint breakpoint) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				if ((breakpoint.isEnabled() && getBreakpointManager()
						.isEnabled())
						|| !breakpoint.isRegistered()) {
					LuaLineBreakpoint luaBreakpoint = (LuaLineBreakpoint) breakpoint;
					luaBreakpoint.install(this);
				}
			} catch (CoreException e) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.IBreakpointListener#breakpointRemoved(org.eclipse.debug.core.model.IBreakpoint,
	 *      org.eclipse.core.resources.IMarkerDelta)
	 */
	public void breakpointRemoved(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				LuaLineBreakpoint pdaBreakpoint = (LuaLineBreakpoint) breakpoint;
				pdaBreakpoint.remove(this);
			} catch (CoreException e) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.IBreakpointListener#breakpointChanged(org.eclipse.debug.core.model.IBreakpoint,
	 *      org.eclipse.core.resources.IMarkerDelta)
	 */
	public void breakpointChanged(IBreakpoint breakpoint, IMarkerDelta delta) {
		if (supportsBreakpoint(breakpoint)) {
			try {
				if (breakpoint.isEnabled()
						&& getBreakpointManager().isEnabled()) {
					breakpointAdded(breakpoint);
				} else {
					breakpointRemoved(breakpoint, null);
				}
			} catch (CoreException e) {
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		return getProcess().canTerminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated() {
		return fTerminated || getProcess().isTerminated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public void terminate() throws DebugException {
		for (IThread thread : fThreads)
			if (thread != null)
				thread.terminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canResume()
	 */
	public boolean canResume() {
		return !isTerminated() && isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canSuspend()
	 */
	public boolean canSuspend() {
		return !isTerminated() && !isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#isSuspended()
	 */
	public boolean isSuspended() {
		return !isTerminated() && getMainThread().isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#resume()
	 */
	public void resume() throws DebugException {
		getMainThread().resume();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#suspend()
	 */
	public void suspend() throws DebugException {
		getMainThread().suspend();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDisconnect#canDisconnect()
	 */
	public boolean canDisconnect() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDisconnect#disconnect()
	 */
	public void disconnect() throws DebugException {
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDisconnect#isDisconnected()
	 */
	public boolean isDisconnected() {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IMemoryBlockRetrieval#getMemoryBlock(long,
	 *      long)
	 */
	@Override
	public IMemoryBlock getMemoryBlock(long startAddress, long length)
			throws DebugException {
		// TODO Auto-generated method stub
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IMemoryBlockRetrieval#supportsStorageRetrieval()
	 */
	@Override
	public boolean supportsStorageRetrieval() {
		// TODO Auto-generated method stub
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.IBreakpointManagerListener#breakpointManagerEnablementChanged(boolean)
	 */
	public void breakpointManagerEnablementChanged(boolean enabled) {
		// TODO Auto-generated method stub

	}

	/**
	 * Registers the given event listener. The listener will be notified of
	 * events in the program being interpretted. Has no effect if the listener
	 * is already registered.
	 * 
	 * @param listener
	 *            event listener
	 */
	public void addEventListener(ILuaEventListener listener) {
		if (!fEventListeners.contains(listener)) {
			fEventListeners.add(listener);
		}
	}

	/**
	 * Deregisters the given event listener. Has no effect if the listener is
	 * not currently registered.
	 * 
	 * @param listener
	 *            event listener
	 */
	public void removeEventListener(ILuaEventListener listener) {
		fEventListeners.remove(listener);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.keplerproject.ldt.debug.core.model.ILuaEventListener#handleEvent(java.lang.String)
	 */
	@Override
	public void handleEvent(String event) {
		if (event.startsWith("200") && !fStarted)
			fireCreationEvent();

	}

	/**
	 * Called when this debug target terminates.
	 */
	synchronized void terminated() {
		fTerminated = true;
		fLuaDebugThread = null;
		fThreads = new IThread[0];
		IBreakpointManager breakpointManager = getBreakpointManager();
		breakpointManager.removeBreakpointListener(this);
		breakpointManager.removeBreakpointManagerListener(this);
		fireTerminateEvent();
		removeEventListener(this);
	}

	/**
	 * @return
	 */
	ILuaEventListener[] getEventListeners() {
		return fEventListeners.toArray(new ILuaEventListener[] {});
	}

}
