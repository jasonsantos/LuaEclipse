/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.IOException;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.IBreakpointManager;
import org.eclipse.debug.core.model.DebugElement;
import org.eclipse.debug.core.model.IDebugTarget;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;

/**
 * Common infrastructure for all Lua Debug Elements
 * 
 * @author jasonsantos
 */
public class LuaDebugElement extends DebugElement {

	public LuaDebugElement(IDebugTarget target) {
		super(target);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */

	public String getModelIdentifier() {
		return LuaDebuggerPlugin.ID_LUA_DEBUG_MODEL;
	}

	/**
	 * Returns the actual debug target
	 * 
	 * @return the debug target
	 */
	protected LuaDebugTarget getLuaDebugTarget() {
		return (LuaDebugTarget) super.getDebugTarget();
	}

	/**
	 * Returns the breakpoint manager
	 * 
	 * @return the breakpoint manager
	 */
	protected IBreakpointManager getBreakpointManager() {
		return DebugPlugin.getDefault().getBreakpointManager();
	}

	public String sendRequest(String request) throws IOException,
			DebugException {
		return getLuaDebugTarget().sendRequest(request);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.DebugElement#requestFailed(java.lang.String,
	 *      java.lang.Throwable)
	 */
	@Override
	public void requestFailed(String message, Throwable e)
			throws DebugException {
		super.requestFailed(message, e);
	}

}
