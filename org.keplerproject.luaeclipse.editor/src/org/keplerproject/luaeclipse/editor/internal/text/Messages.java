package org.keplerproject.luaeclipse.editor.internal.text;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "com.anwrt.ldt.internal.editor.text.messages"; //$NON-NLS-1$
	public static String LuaEditor_matchingBracketIsOutsideSelectedElement;
	public static String LuaEditor_nobracketSelected;
	public static String LuaEditor_noMatchingBracketFound;
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}
}
