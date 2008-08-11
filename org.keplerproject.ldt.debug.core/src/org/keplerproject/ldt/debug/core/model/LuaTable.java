/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

/**
 * @author jasonsantos
 */
public class LuaTable extends LuaValue {
	private final String	fCompositeValue;

	/**
	 * @param debugTarget
	 * @param type
	 * @param value
	 * @throws DebugException
	 */
	public LuaTable(LuaValue value) {
		super(value.getDebugTarget(), value.getReferenceTypeName(),
				parseCompositeValue(value.getValueString()));
		fCompositeValue = value.getValueString();
	}

	/**
	 * @param valueString
	 * @return
	 */
	private static String parseCompositeValue(String valueString) {
		if (valueString != null) {
			String[] words = valueString.split("#");// TODO: add escape
													// characters support
			return words[0];
		}
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.keplerproject.ldt.debug.core.model.LuaValue#hasVariables()
	 */
	@Override
	public boolean hasVariables() throws DebugException {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.keplerproject.ldt.debug.core.model.LuaValue#getVariables()
	 */
	@Override
	public IVariable[] getVariables() throws DebugException {

		String[] words = fCompositeValue.split("#");// TODO: add escape
		// characters support
		if (words.length > 1) {
			String[] items = words[1].split("\\|");// TODO: add escape
			// characters support
			IVariable[] variables = new IVariable[items.length];
			for (int i = 0; i < items.length; i++) {
				String[] pair = items[i].split("="); // TODO: add escape
				// characters support
				variables[i] = new LuaTableEntry(getLuaDebugTarget(), pair[0],
						new LuaValue(getLuaDebugTarget(),
								getReferenceTypeName(), pair[1]));
			}
			return variables;
		}

		return super.getVariables();
	}

}
