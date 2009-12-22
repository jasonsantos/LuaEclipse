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


/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-17 14:29:28 +0200 (ven., 17 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: LuaExpressionConstants.java 2111 2009-07-17 12:29:28Z kkinfoo $
 */
package org.keplerproject.luaeclipse.parser;

import org.eclipse.dltk.ast.expressions.ExpressionConstants;

/**
 * The Interface LuaExpressionConstants presents value that the parser use to
 * recognize Lua's syntax elements
 */
public interface LuaExpressionConstants extends ExpressionConstants {
	/*
	 * Expressions
	 */
	/** Binary operations */
	public static final int E_BIN_OP = 38001;

	/** '#' operator for length of tables */
	public static final int E_LENGTH = 38002;

	/** representation of 'nil' */
	public static final int NIL_LITTERAL = 38003;

	/**
	 * Unary minus {@code foo = -9}
	 */
	public static final int E_UN_MINUS = 38004;

	/** Representation of table */
	public static final int E_TABLE = 38005;

	/** Value for functions */
	public static final int E_FUNCTION = 38006;

	/**
	 * Representation of pairs
	 * {@code tab = [1] = 'one', two = 2}}
	 */
	public static final int E_PAIR = 38007;

	/** Boolean true */
	public static final int BOOL_TRUE = 38008;

	/** Boolean false */
	public static final int BOOL_FALSE = 38009;

	/**
	 * Optional parameters operator {@code var = ...}
	 */
	public static final int E_DOTS = 38010;

	public static final int E_INVOKE = 38011;
}
