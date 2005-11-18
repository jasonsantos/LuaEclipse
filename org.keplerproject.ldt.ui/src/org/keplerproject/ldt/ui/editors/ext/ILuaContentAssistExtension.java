/**********************************
 * 
 ********************************/
package org.keplerproject.ldt.ui.editors.ext;

import org.eclipse.jface.text.contentassist.ContentAssistant;
import org.eclipse.ui.IEditorPart;
/**
 * This interface, provide a way to contribute to the LuaEditor
 * content assist. This Interface must be implemented by the class thats 
 * extends the extension point "SourceConfigurationExtension" at the "assist" TAG. 
 * 
 * @author guilherme
 * @version 1.0
 */
public interface ILuaContentAssistExtension {
    /**
     * This method must contribute with the content assistant
     * associating ContentAssistProcessors with DocumentPartitions.
     *  
     * @param editor
     * @param assistant
     */
	void contribute(IEditorPart editor , ContentAssistant assistant);

}
