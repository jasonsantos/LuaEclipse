/*********************************
 * 
 *******************************/
package org.keplerproject.ldt.ui.editors.ext;

/**
 * This class provide a interface to the SourceConfigurationExtension to contribute
 * with available document content type. Content Types are defined by a Document Patition.
 * See the IScannerRuleExtension to know how to define content typ partitionig.
 *  
 * @author guilherme
 * @version 1.0
 */
public interface ILuaContentTypeExtension 
{
	/**
	 * Return the available contet types names.
	 * @return
	 */
	String[] getContentTypes();
}
