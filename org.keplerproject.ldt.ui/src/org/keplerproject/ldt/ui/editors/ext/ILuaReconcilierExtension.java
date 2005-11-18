/*********************************
 * 
 *************************************/
package org.keplerproject.ldt.ui.editors.ext;

import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.source.ISourceViewer;
import org.keplerproject.ldt.ui.editors.LuaColorManager;
/**
 * This Class provide a inteface to extend the SourceConfigurationExtension
 * with a recocilier contributor.
 * The Reconcilier provide a way to define text attributes to the 
 * source code using damager and reparier. The contributor class must 
 * associate with some document partition, a Damager and A reparier.
 * 
 * @author guilherme
 * @version 1.0
 */
public interface ILuaReconcilierExtension {

	/**
	 * Contribute with the PresantationReconcilier with 
	 * Demagers and Repariers
	 * @param manager
	 * @param reconciler
	 * @param sourceViewer
	 */
	void contribute(LuaColorManager manager,PresentationReconciler reconciler, ISourceViewer sourceViewer);
	
	
}
