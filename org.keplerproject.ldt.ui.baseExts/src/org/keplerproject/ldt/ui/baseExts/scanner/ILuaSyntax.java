/*******************************************************************************
*
*
*
********************************************************************************/

package org.keplerproject.ldt.ui.baseExts.scanner;

/**
 * Lua syntax tokens
 * @author guilherme
 *
 */
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
