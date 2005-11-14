package org.keplerproject.ldt.ui.editors.ext;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.IEditorPart;

public interface ILuaContentAssistExtension {

	void contribute(IEditorPart editor , ContentAssistant assistant);

}
