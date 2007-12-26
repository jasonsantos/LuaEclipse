package org.keplerproject.ldt.luaprofiler.core.views;

import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IMenuListener;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.jface.viewers.TableViewer;
import org.eclipse.jface.viewers.ViewerSorter;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IActionBars;
import org.eclipse.ui.ISharedImages;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.part.ViewPart;
import org.keplerproject.ldt.luaprofiler.core.Activator;
import org.keplerproject.ldt.luaprofiler.core.analasyer.LuaProfilerAnalyserListener;
import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;

public class LuaProfilerView extends ViewPart {
	private TableViewer viewer;
	private Action updateAction;

	class ViewLabelProvider extends LabelProvider implements ITableLabelProvider {
		public String getColumnText(Object obj, int index) {
			try {
				return ((LuaObject) obj).getField(infoNames[index]).toString();
			} catch (LuaException e) {
				e.printStackTrace();
			}
			return "oops sorry";
		}

		public Image getColumnImage(Object obj, int index) {
			return null;
		}

		public Image getImage(Object obj) {
			return PlatformUI.getWorkbench().getSharedImages().getImage(ISharedImages.IMG_OBJ_ELEMENT);
		}

	}

	class NameSorter extends ViewerSorter {

	}

	private static final String[] labelsNames = new String[] { "Node name", "Calls", "Average per call", "Total time", "%Time" };
	private static final String[] infoNames = new String[] { "func", "calls", "average", "total", "time" };

	/**
	 * The constructor.
	 */
	public LuaProfilerView() {
	}

	/**
	 * This is a callback that will allow us to create the viewer and initialize
	 * it.
	 */
	public void createPartControl(Composite parent) {

		viewer = new TableViewer(parent, SWT.MULTI | SWT.H_SCROLL | SWT.V_SCROLL);

		Table tb = viewer.getTable();
		tb.setHeaderVisible(true);
		tb.setLinesVisible(true);
		for (int i = 0; i < labelsNames.length; i++) {
			TableColumn cl = new TableColumn(tb, SWT.LEFT);
			cl.setText(labelsNames[i]);
			cl.setWidth(100);

		}

		LuaProfilerContentProvider.getContentProvider().setListener(new LuaProfilerAnalyserListener() {
			@Override public void profilerDataChanged(final IProcess process) {
				Display.getDefault().asyncExec(new Runnable() {
					@Override public void run() {
						boolean finish = false;
						while (!finish) {
							try {
								process.getExitValue();
								finish = true;
								viewer.refresh();
								viewer.getControl().setFocus();
							} catch (DebugException e) {
								// Oh!! still running
								try {
									// wait .5 secs
									Thread.sleep(500);
								} catch (InterruptedException e1) {
								}
							}
						}
						return;
					}
				});
			}

		});
		viewer.setContentProvider(LuaProfilerContentProvider.getContentProvider());
		viewer.setLabelProvider(new ViewLabelProvider());
		viewer.setSorter(new NameSorter());
		viewer.setInput(getViewSite());

		makeActions();
		hookContextMenu();
		contributeToActionBars();

	}

	private void hookContextMenu() {
		MenuManager menuMgr = new MenuManager("#PopupMenu");
		menuMgr.setRemoveAllWhenShown(true);
		menuMgr.addMenuListener(new IMenuListener() {
			public void menuAboutToShow(IMenuManager manager) {
				LuaProfilerView.this.fillContextMenu(manager);
			}
		});
		Menu menu = menuMgr.createContextMenu(viewer.getControl());
		viewer.getControl().setMenu(menu);
		getSite().registerContextMenu(menuMgr, viewer);
	}

	private void contributeToActionBars() {
		IActionBars bars = getViewSite().getActionBars();
		fillLocalPullDown(bars.getMenuManager());
		fillLocalToolBar(bars.getToolBarManager());
	}

	private void fillLocalPullDown(IMenuManager manager) {
		manager.add(updateAction);
		manager.add(new Separator());
	}

	private void fillContextMenu(IMenuManager manager) {
		manager.add(updateAction);
		manager.add(new Separator(IWorkbenchActionConstants.MB_ADDITIONS));
	}

	private void fillLocalToolBar(IToolBarManager manager) {
		manager.add(updateAction);
	}

	private void makeActions() {
		updateAction = new Action() {
			public void run() {
				showMessage(" This may take a few minutes. Depending on the size of profile output :) ");
				viewer.refresh();
				showMessage("Profiler information updated");
			}
		};
		updateAction.setText("Refresh Profile Information");
		updateAction.setToolTipText("Refresh Profile Information");
		updateAction.setImageDescriptor(Activator.getImageDescriptor("icons/refresh-icon.gif"));

	}

	private void showMessage(String message) {
		MessageDialog.openInformation(viewer.getControl().getShell(), "Lua Profiler View", message);
	}

	/**
	 * Passing the focus request to the viewer's control.
	 */
	public void setFocus() {
		viewer.refresh();
		viewer.getControl().setFocus();
	}
}