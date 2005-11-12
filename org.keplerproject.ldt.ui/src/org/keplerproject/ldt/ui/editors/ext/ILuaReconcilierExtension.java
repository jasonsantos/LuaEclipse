package org.keplerproject.ldt.ui.editors.ext;

import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.keplerproject.ldt.ui.editors.LuaColorManager;

public interface ILuaReconcilierExtension {

	void contribute(LuaColorManager manager,PresentationReconciler reconciler);

}
