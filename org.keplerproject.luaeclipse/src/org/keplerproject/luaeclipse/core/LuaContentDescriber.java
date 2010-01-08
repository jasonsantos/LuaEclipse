/******************************************************************************
 * Copyright (c) 2009 KeplerProject, Sierra Wireless.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 *          - initial API and implementation and initial documentation
 *****************************************************************************/

package org.keplerproject.luaeclipse.core;

import java.util.regex.Pattern;

import org.eclipse.dltk.core.ScriptContentDescriber;

/**
 * The Class LuaContentDescriber gives patterns for Lua files headers. Most of
 * the time those headers allow to choose an interpreter for runnable scripts
 * 
 * @author Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 */
public class LuaContentDescriber extends ScriptContentDescriber {

	/** Accepted patterns for headers. */
	protected static Pattern[] header_patterns = { Pattern.compile(
			"^#!.*lua.*", Pattern.MULTILINE) };

	/**
	 * Instantiates a new Lua content describer.
	 */
	public LuaContentDescriber() {
		// Do nothing
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.core.ScriptContentDescriber#getHeaderPatterns()
	 */
	@Override
	protected Pattern[] getHeaderPatterns() {
		return header_patterns;
	}
}
