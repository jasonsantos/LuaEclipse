package org.keplerproject.dltk.ldt.ui.internal.preferences;

import org.eclipse.dltk.ui.util.IStatusChangeListener;
import org.eclipse.dltk.ui.wizards.BuildpathsBlock;
import org.eclipse.jface.operation.IRunnableContext;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.preferences.IWorkbenchPreferenceContainer;
import org.keplerproject.dltk.ldt.ui.ide.LuaEclipseIDEPlugin;

public class LuaBuildpathsBlock extends BuildpathsBlock {

	public LuaBuildpathsBlock(IRunnableContext runnableContext,
			IStatusChangeListener context, int pageToShow, boolean useNewPage,
			IWorkbenchPreferenceContainer pageContainer) {
		super(runnableContext, context, pageToShow, useNewPage, pageContainer);
	}

	@Override
	protected IPreferenceStore getPreferenceStore() {
		return LuaEclipseIDEPlugin.getDefault().getPreferenceStore();
	}

	@Override
	protected boolean supportZips() {
		return false;
	}

}
