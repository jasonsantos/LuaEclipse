package org.keplerproject.ldt.luaprofiler.launcher.ui;

import org.eclipse.ui.IFolderLayout;
import org.eclipse.ui.IPageLayout;
import org.eclipse.ui.IPerspectiveFactory;
import org.eclipse.ui.console.IConsoleConstants;
import org.eclipse.ui.progress.IProgressConstants;

public class LuaProfilerPerspective implements IPerspectiveFactory {

	public void createInitialLayout(IPageLayout layout) {
		// Views
		IFolderLayout leftFolder = layout.createFolder("left",
				IPageLayout.LEFT, (float) 0.2, layout.getEditorArea());
		leftFolder.addView("org.eclipse.ui.navigator.ProjectExplorer");

		IFolderLayout rightFolder = layout.createFolder("right",
				IPageLayout.RIGHT, (float) 0.8, layout.getEditorArea());
		rightFolder.addView(IPageLayout.ID_OUTLINE);

		IFolderLayout profilerFolder = layout.createFolder("profiler",
				IPageLayout.BOTTOM, (float) 0.75, layout.getEditorArea());
		profilerFolder
				.addView("org.keplerproject.ldt.luaprofiler.core.views.LuaProfilerView");

		IFolderLayout bottomFolder = layout.createFolder("bottom",
				IPageLayout.BOTTOM, (float) 2. / 3, layout.getEditorArea());
		bottomFolder.addView(IConsoleConstants.ID_CONSOLE_VIEW);
		bottomFolder.addPlaceholder(IPageLayout.ID_BOOKMARKS);
		bottomFolder.addPlaceholder(IProgressConstants.PROGRESS_VIEW_ID);

		// Actions
		layout.addActionSet(IPageLayout.ID_NAVIGATE_ACTION_SET);
		// layout.addActionSet(IDebugUIConstants.DEBUG_ACTION_SET);
		layout.addActionSet("org.keplerproject.ldt.ui.luaProjectAction");

		// Shortcuts
		layout.addPerspectiveShortcut("org.eclipse.debug.ui.DebugPerspective");
	}
}
