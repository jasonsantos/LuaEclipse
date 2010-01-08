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
package org.keplerproject.luaeclipse.internal.parser;

/**
 * 
 * @author Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 * 
 */
public class LuaParseErrorAnalyzer {

	private String _codeToParse;
	private Integer _errorLine;
	private Integer _errorOffset;
	private Integer _errorCol;
	private String _errorString;

	public LuaParseErrorAnalyzer(String code, String errorMessage) {
		_codeToParse = code;
		_errorCol = null;
		_errorLine = null;
		_errorOffset = null;
		_errorString = errorMessage;
	}

	private Integer extractIntFromErrorString(final String startTag,
			final char endTag) {
		return extractIntFromErrorString(startTag, endTag, 0);

	}

	private Integer extractIntFromErrorString(final String startTag,
			final char endTag, final int shift) {
		String errorMessage = shift > 0 ? _errorString.substring(shift)
				: _errorString;

		int offsetStart = errorMessage.indexOf(startTag) + startTag.length();
		int offsetEnd = errorMessage.indexOf(endTag);
		String offset = errorMessage.substring(offsetStart, offsetEnd);
		return Integer.parseInt(offset);

	}

	public String getErrorString() {
		return _errorString;
	}

	public String getOriginalCode() {
		return _codeToParse;
	}

	public int syntaxErrorOffset() {
		if (_errorOffset == null) {
			_errorOffset = extractIntFromErrorString(" char ", ':');
		}
		return _errorOffset;
	}

	public int syntaxErrorLine() {
		if (_errorLine == null) {
			_errorLine = extractIntFromErrorString(" line ", ',');
		}
		return _errorLine;
	}

	public int syntaxErrorColumn() {
		if (_errorCol == null) {
			 String tag = " column ";
			_errorCol = extractIntFromErrorString(tag, ',', _errorString.indexOf(tag));
		}
		return _errorCol;
	}
}
