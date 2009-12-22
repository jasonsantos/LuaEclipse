/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:30:48 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: LuaContentDescriber.java 1840 2009-06-15 15:30:48Z kkinfoo $
 */
package org.keplerproject.luaeclipse.core;

import java.util.regex.Pattern;

import org.eclipse.dltk.core.ScriptContentDescriber;

// TODO: Auto-generated Javadoc
/**
 * The Class LuaContentDescriber.
 */
public class LuaContentDescriber extends ScriptContentDescriber {

	/** Accepted patterns for headers. */
	protected static Pattern[] header_patterns = {
		Pattern.compile("^#!.*lua.*", Pattern.MULTILINE)
	};

	/**
	 * Instantiates a new lua content describer.
	 */
	public LuaContentDescriber() {
		// Do nothing
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.core.ScriptContentDescriber#getHeaderPatterns()
	 */
	@Override
	protected Pattern[] getHeaderPatterns() {
		return header_patterns;
	}
}
