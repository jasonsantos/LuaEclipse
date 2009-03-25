/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.util.Arrays;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

/**
 * @author jasonsantos
 */
public class LuaTable extends LuaValue {
	private final String		fCompositeValue;
	private final LuaVariable	fVariable;

	/**
	 * @param debugTarget
	 * @param type
	 * @param value
	 * @throws DebugException
	 */
	public LuaTable(LuaVariable variable, LuaValue value) {
		super(value.getDebugTarget(), value.getReferenceTypeName(),
				parseCompositeValue(value.getValueString()));
		fCompositeValue = value.getValueString();
		fVariable = variable;
	}

	public LuaVariable getVariable() {
		return fVariable;
	}

	/**
	 * @param valueString
	 * @return
	 */
	private static String parseCompositeValue(String valueString) {
		if (valueString != null) {
			String[] words = valueString.split("#");// TODO: add escape
			// characters support
			// TODO: check if there's a type
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
				// characters support
				variables[i] = new LuaTableEntry(this, items[i]);
			}
			Arrays.sort(variables, LuaVariable.getComparator());
			return variables;
		}

		return super.getVariables();
	}
}
