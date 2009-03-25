/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IVariable;

/**
 * @author jasonsantos
 */
public class LuaTableEntry extends LuaVariable implements IVariable {

	private final LuaTable	fTable;

	/**
	 */
	public LuaTableEntry(LuaTable table, String data) {
		super(table.getVariable().getStackFrame(), data);
		fTable = table;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.keplerproject.ldt.debug.core.model.LuaVariable#getInternalName()
	 */
	@Override
	protected String getInternalName() throws DebugException {
		String name = super.getName();
		try {
			name = URLEncoder.encode(name, "UTF-8");
		} catch (UnsupportedEncodingException e) { }
		return fTable.getVariable().getInternalName() + ":" + name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IVariable#getName()
	 */
	@Override
	public String getName() throws DebugException {
		Integer i = null;
		try {
			i = Integer.parseInt(super.getName());
		} catch (Exception e) {
		}

		return i == null ? super.getName() : ("[" + i + "]");
	}

}
