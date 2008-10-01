package org.keplerproject.dltk.ldt.core;

import java.io.IOException;
import java.io.Reader;

import org.eclipse.core.runtime.content.IContentDescription;
import org.eclipse.dltk.core.ScriptContentDescriber;

public class LuaContentTypeDescriber extends ScriptContentDescriber {
	static final String CONTENT_TYPE = "org.keplerproject.dltk.ldt.content-type";
	
	public int describe(Reader contents, IContentDescription description)
			throws IOException {
		return ScriptContentDescriber.INDETERMINATE;
	}

}
