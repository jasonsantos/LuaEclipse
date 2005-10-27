/*******************************************************************************
 * Copyright (c) 2003 Ideais Tecnologia LTDA.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package org.keplerproject.ldt.ui.editors;

public interface ILuaSyntax {
    public static final String[] reservedwords = {
        "for", "while", "do", "end", "repeat", "until", "if", "elseif", "then",
        "else", "return", "break", "function", "local", "in"
    };
    public static final String[] predicates = {
        "+", "-", "*", "/", "^", "..", "<", "<=", ">", ">=", "==", "~=", "and",
        "or", "not"
    };
    public static final String[] constants = { "false", "true", "nil" };
    public static final String[] functions = {
        "assert", "collectgarbage", "dofile", "error", "getfenv", "getmetatable",
        "gcinfo", "ipairs", "loadfile", "loadstring", "next", "pairs", "pcall",
        "print", "rawequal", "rawget", "rawset", "require", "setfenv",
        "setmetatable", "tonumber", "tostring", "type", "unpack", "xpcall"
    };
    Object[] allWords = { reservedwords, predicates, constants, functions };
}
