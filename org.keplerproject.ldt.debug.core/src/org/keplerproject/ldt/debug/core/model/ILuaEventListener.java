/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

/**
 * Interface to be used in all debug elements to communicate with RemDebug
 * 
 * @author jasonsantos
 */
public interface ILuaEventListener {
	/**
	 * Event occurred in the target program being interpreted.
	 * 
	 * @param event
	 *            the event
	 */
	public void handleEvent(String event);
}
