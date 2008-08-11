/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.debug.core.model.ILogicalStructureTypeDelegate;
import org.eclipse.debug.core.model.IValue;

/**
 * @author jasonsantos
 */
public class LuaTableStructureType implements ILogicalStructureTypeDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILogicalStructureTypeDelegate#getLogicalStructure(org.eclipse.debug.core.model.IValue)
	 */
	public IValue getLogicalStructure(IValue value) throws CoreException {
		return new LuaTable((LuaValue) value);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILogicalStructureTypeDelegate#providesLogicalStructure(org.eclipse.debug.core.model.IValue)
	 */
	public boolean providesLogicalStructure(IValue value) {
		try {
			if ("table".equals(value.getReferenceTypeName())) {
				String string = value.getValueString();
				String[] words = string.split("#");
				return (words.length > 1);
			}
		} catch (Exception e) {
		}
		return false;
	}

}
