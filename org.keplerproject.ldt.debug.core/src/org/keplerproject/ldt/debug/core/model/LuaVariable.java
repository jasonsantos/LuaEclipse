/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Comparator;

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
	private IValue fValue;

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

	public String getName() throws DebugException {
		return fName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getReferenceTypeName()
	 */

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

	public synchronized IValue getValue() throws DebugException {
		if (fValue == null) {
			// Default the value to the type in case of a nil value
			String value = fType;
			
			if (!fType.equals("nil")) {
				try {
					value = sendRequest("EXAMINE " + fFrame.getId() + " "
							+ getInternalName());
				} catch (IOException e) {
				}
				
				try {
					value = URLDecoder.decode(value, "UTF-8");
				} catch (UnsupportedEncodingException e) { }
				
			}
			
			if ("table".equals(fType))
				fValue = new LuaTable(this, new LuaValue(this.getDebugTarget(), fType, value));
			else
				fValue = new LuaValue(this.getDebugTarget(), fType, value);
		}
		
		return fValue;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#hasValueChanged()
	 */

	public boolean hasValueChanged() throws DebugException {
		if (fName.equals("FATAL_ERROR") && fType.equals("table") && fValue.getValueString().startsWith("A Fatal"))
			return true;
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(java.lang.String)
	 */

	public void setValue(String expression) throws DebugException {
		// TODO: in the future, allow to change values
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#setValue(org.eclipse.debug.core.model.IValue)
	 */

	public void setValue(IValue value) throws DebugException {
		// TODO: in the future, allow to change values
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#supportsValueModification()
	 */

	public boolean supportsValueModification() {
		// TODO: in the future, allow to change values
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(java.lang.String)
	 */

	public boolean verifyValue(String expression) throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IValueModification#verifyValue(org.eclipse.debug.core.model.IValue)
	 */

	public boolean verifyValue(IValue value) throws DebugException {
		return false;
	}

	public static Comparator<IVariable> getComparator() {
		return new Comparator<IVariable>() {
			public int compare(IVariable o1, IVariable o2) {
				try {
					if (o1.getName().equals("FATAL_ERROR")) return -1;
					if (o2.getName().equals("FATAL_ERROR")) return 1;
					return o1.getName().compareTo(o2.getName());
				} catch (DebugException e) {
					throw new RuntimeException(e);
				}
			}
		};
	}
}
