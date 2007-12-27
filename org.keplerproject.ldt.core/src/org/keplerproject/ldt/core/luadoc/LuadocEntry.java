/*
 * Copyright (C) 2003-2007 Kepler Project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package org.keplerproject.ldt.core.luadoc;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.keplerproject.ldt.core.ILuaEntry;

/**
 * A Lua syntax element for using with Luadoc.
 * 
 * @author jasonsantos
 * @since 1.2
 * @version $Id$
 */

public class LuadocEntry implements ILuaEntry, Serializable {

	String module;
	String name;
	String entryType;
	String summary;
	String description;
	List<String> codeLines;
	List<String> commentLines;
	Map<String, String> params;

	public List<String> getCodeLines() {
		return codeLines;
	}

	public List<String> getCommentLines() {
		return commentLines;
	}

	public String getDescription() {
		return description;
	}

	public String getEntryType() {
		return entryType;
	}

	public String getName() {
		return name;
	}

	public Map<String, String> getParams() {
		return params;
	}

	public String getSummary() {
		return summary;
	}

	public void setCodeLines(List<String> codeLines) {
		this.codeLines = codeLines;
	}

	public void setCommentLines(List<String> commentLines) {
		this.commentLines = commentLines;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public void setEntryType(String entryType) {
		this.entryType = entryType;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setParams(Map<String, String> params) {
		this.params = params;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getModule() {
		return module;
	}

	public void setModule(String module) {
		this.module = module;
	}

	public void setComment(String comment) {
		this.commentLines = Arrays.asList(comment.split("\n"));
	}

	public void setCode(String string) {
		this.codeLines = Arrays.asList(string.split("\n"));
	}

	public String getComment() {
		List<String> l = commentLines;
		return getAsString(l, "\n");
	}

	public String getCode() {
		List<String> l = this.codeLines;
		return getAsString(l, "\n");
	}

	/**
	 * Return a string concatenating list of strings with a delimiter
	 * 
	 * @param l
	 *            the list of strings
	 * @param delimiter
	 *            the string to be inserted after each line
	 * @return a string containing all entries concatenated with delimiter
	 */
	private String getAsString(List<String> l, String delimiter) {
		StringBuilder result = new StringBuilder();

		if (l != null)
			for (String s : l)
				result.append(s + delimiter);

		return result.toString();
	}

}
