package org.keplerproject.ldt.luaprofiler.launcher.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.runtime.Platform;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.SWT;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.FileDialog;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.kepler.ldt.laucher.LauncherPlugin;
import org.keplerproject.ldt.luaprofiler.core.LuaProfiler;
import org.keplerproject.ldt.luaprofiler.core.LuaProfiler.LuaProfilerInfo;

/**
 * Lua Profiler page Extension.
 * @author edgard
 * @version $Id$
 * @since 1.2
 */
public class LuaProfilerPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {
	protected CheckboxTableViewer tableViewer;

	protected Button addButton;

	// protected Button editButton;

	protected Button removeButton;

	public LuaProfilerPreferencePage() {
		setPreferenceStore(LauncherPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench workbench) {
	}

	@Override
	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		Composite composite = createPageRoot(parent);

		Table table = new Table(composite, 0x10820);
		GridData data = new GridData(1808);
		table.setLayoutData(data);
		table.setHeaderVisible(true);
		table.setLinesVisible(false);
		TableColumn column = new TableColumn(table, 0);
		column.setText("Name");
		column.setWidth(100);
		column = new TableColumn(table, 0);
		column.setText("Path");
		column.setWidth(250);

		tableViewer = new CheckboxTableViewer(table);

		tableViewer.setContentProvider(new IStructuredContentProvider() {
			@SuppressWarnings("unchecked")
			public Object[] getElements(Object input) {
				List<LuaProfilerInfo> l = (List<LuaProfilerInfo>) input;
				return l.toArray();
			}

			public void dispose() {
			}

			public void inputChanged(Viewer viewer, Object oldVal, Object newVal) {
			}
		});
		tableViewer.setLabelProvider(new ITableLabelProvider() {

			public Image getColumnImage(Object arg0, int arg1) {
				return null;
			}

			public String getColumnText(Object arg0, int column) {
				switch (column) {
				case 0:
					return ((LuaProfilerInfo) arg0).getName();
				case 1:
					return ((LuaProfilerInfo) arg0).getFile().getAbsolutePath();
				default:
					return "";
				}
			}

			public void addListener(ILabelProviderListener arg0) {
			}

			public void dispose() {
			}

			public boolean isLabelProperty(Object arg0, String arg1) {
				return false;
			}

			public void removeListener(ILabelProviderListener arg0) {
			}
		});
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent event) {
						enableButtons();
					}

				});

		tableViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				LuaProfiler.getDefault().setSelectedProfiler(
						(LuaProfilerInfo) event.getElement());
				tableViewer.setChecked(event.getElement(), true);
			}
		});
		tableViewer.setInput(LuaProfiler.getDefault().getAvailableProfilers());
		if (LuaProfiler.getDefault().getSelectedProfiler() != null) {
			tableViewer.setChecked(LuaProfiler.getDefault()
					.getSelectedProfiler(), true);
		}

		createButtonGroup(composite);
		enableButtons();
		return composite;
	}

	protected Composite createPageRoot(Composite parent) {
		Composite composite = new Composite(parent, 0);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		return composite;
	}

	protected void createButtonGroup(Composite composite) {
		Composite buttons = new Composite(composite, 0);
		buttons.setLayoutData(new GridData(1));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);
		addButton = new Button(buttons, 8);
		addButton.setLayoutData(new GridData(768));
		addButton.setText("Add");
		addButton.addListener(13, new Listener() {

			public void handleEvent(Event evt) {
				addProfiler();
			}

		});
		/*
		 * editButton = new Button(buttons, 8); editButton.setLayoutData(new
		 * GridData(768)); editButton.setText("Edit");
		 * editButton.addListener(13, new Listener() {
		 * 
		 * public void handleEvent(Event evt) { editProfiler(); }
		 * 
		 * });
		 */
		removeButton = new Button(buttons, 8);
		removeButton.setLayoutData(new GridData(768));
		removeButton.setText("Remove");
		removeButton.addListener(13, new Listener() {

			public void handleEvent(Event evt) {
				removeProfiler();
			}

		});
	}

	protected void addProfiler() {
		InputDialog name = new InputDialog(getShell(), "Name", "Profiler Name",
				"Profiler", null);
		name.open();
		FileDialog file = new FileDialog(getShell(), SWT.OPEN);
		
		if(Platform.getOS().equals(Platform.OS_WIN32)) 
			file.setFilterExtensions(new String[] { "*.dll", "*.a", "*.lib" });
		else
			file.setFilterExtensions(new String[] { "*.so", "*.a"});
		
		LuaProfilerInfo info = new LuaProfilerInfo(name.getValue(), file.open());
		tableViewer.add(info);
	}

	protected void removeProfiler() {
		tableViewer.remove(getSelection());
	}

	protected void enableButtons() {
		if (getSelection() != null) {
			// editButton.setEnabled(true);
			removeButton.setEnabled(true);
		} else {
			// editButton.setEnabled(false);
			removeButton.setEnabled(false);
		}
	}

	protected Object getSelection() {
		return ((IStructuredSelection) tableViewer.getSelection())
				.getFirstElement();
	}

	@Override
	public boolean performOk() {
		TableItem[] itens = tableViewer.getTable().getItems();
		List<LuaProfilerInfo> profilers = new ArrayList<LuaProfilerInfo>(
				itens.length);
		for (TableItem item : itens) {
			profilers.add((LuaProfilerInfo) item.getData());
		}

		LuaProfiler.getDefault().setAvailableProfilers(profilers);
		LuaProfilerInfo selected = (LuaProfilerInfo) ((IStructuredSelection) tableViewer
				.getSelection()).getFirstElement();
		LuaProfiler.getDefault().setSelectedProfiler(selected);

		return true;
	}
}
