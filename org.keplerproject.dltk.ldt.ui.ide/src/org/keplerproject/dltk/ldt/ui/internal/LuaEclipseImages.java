package org.keplerproject.dltk.ldt.ui.internal;

import org.eclipse.core.runtime.Path;
import org.eclipse.dltk.ui.PluginImagesHelper;
import org.eclipse.jface.resource.ImageDescriptor;
import org.keplerproject.dltk.ldt.ui.ide.LuaEclipseIDEPlugin;

public class LuaEclipseImages {

	private static final PluginImagesHelper helper = new PluginImagesHelper(
			LuaEclipseIDEPlugin.getDefault().getBundle(), new Path("/icons")); //$NON-NLS-1$

	public static final ImageDescriptor DESC_WIZBAN_PROJECT_CREATION = helper
			.createUnManaged(PluginImagesHelper.T_WIZBAN,
					"luaeclipse.icon.big.project.png"); //$NON-NLS-1$

	public static final ImageDescriptor DESC_WIZBAN_FILE_CREATION = helper
			.createUnManaged(PluginImagesHelper.T_WIZBAN,
					"luaeclipse.icon.big.file.png"); //$NON-NLS-1$

}
