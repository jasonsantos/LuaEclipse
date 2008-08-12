/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.IOException;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IValue;
import org.eclipse.debug.core.model.IVariable;

/**
 * @author jasonsantos
 */
public class LuaVariable extends LuaDebugElement implements IVariable {
	private final String		fName;
	private final LuaStackFrame	fFrame;

	private final String		fType;

	/**
	 * @param target
	 */
	public LuaVariable(LuaStackFrame frame, String data) {
		super(frame.getDebugTarget());
		fFrame = frame;
		String[] values = data.split("=");
		fName = values[0];
		fType = values[1];
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	@Override
	public String getName() throws DebugException {
		return fName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */
	@Override
	public String getReferenceTypeName() throws DebugException {
		return fType;
	}

	/**
	 * Return the name used by RemDebug to identify table members This name is
	 * returned by the command EXAMINE when inspecting tables
	 * 
	 * @return
	 * @throws DebugException
	 */
	protected String getInternalName() throws DebugException {
		return getName();
	}

	LuaStackFrame getStackFrame() {
		return fFrame;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getValue()
	 */
	@Override
	public IValue getValue() throws DebugException {
		String value = "";
		try {
			value = sendRequest("EXAMINE " + fFrame.getId() + " "
					+ getInternalName());

		} catch (IOException e) {
		}

		if ("table".equals(fType))
			return new LuaTable(this, new LuaValue(this.getDebugTarget(),
					fType, value));

		return new LuaValue(this.getDebugTarget(), fType, value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */
	@Override
	public boolean hasValueChanged() throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(java.lang.String)
	 */
	@Override
	public void setValue(String expression) throws DebugException {
		// TODO: in the future, allow to change values
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(org.eclipse.debug.core.model.IValue)
	 */
	@Override
	public void setValue(IValue value) throws DebugException {
		// TODO: in the future, allow to change values
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#supportsValueModification()
	 */
	@Override
	public boolean supportsValueModification() {
		// TODO: in the future, allow to change values
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(java.lang.String)
	 */
	@Override
	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(org.eclipse.debug.core.model.IValue)
	 */
	@Override
	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}

}
