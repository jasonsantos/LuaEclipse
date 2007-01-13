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

package org.keplerproject.ldt.ui.editors.ext;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;

/**
 * This interface define the extension point to LuaEditor Scanners.
 * Even if the scanner is used to <i>document partition</i> or <i>syntax highlighting</i>,
 * this interface can be used to extend the rules to Scan the source document.
 * 
 * <i>Note that all the methods returns a array. The position of each element in differrent arrays
 * is relevant. For example, the method getTokens will be the base array, and the Token at index
 * 0 will have the ScannerRule(with textAtribute) ,of the getRules method ,at the position 0. The same
 * thing happens with the getCompletionProcessors.</i>
 *  
 * @author guilherme
 * @version $Id$
 */
public interface IScannerRuleExtension {

	/**
	 * Return all IRules objects to extend the document partitioner or the code scanner
	 * 
	 * @return IRule [] containing the rules to extend the scanner 
	 */
   IPredicateRule[] getRules();
   
   /**
    * Return the Tokens Scanned by these Rules.
    * 
    * Some uses of this method will expect diferent types of IToken.getData() returns
    * for example, to a document partitioner, will expect a String.
    * 
    * @return
    */
   IToken[] getTokens();
   
  }
