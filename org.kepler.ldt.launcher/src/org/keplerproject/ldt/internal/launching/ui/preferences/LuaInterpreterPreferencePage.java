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
package org.keplerproject.ldt.internal.launching.ui.preferences;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.viewers.CheckStateChangedEvent;
import org.eclipse.jface.viewers.CheckboxTableViewer;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.ICheckStateListener;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionChangedListener;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.SelectionChangedEvent;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableColumn;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.kepler.ldt.laucher.LauncherPlugin;
import org.keplerproject.ldt.internal.launching.LuaInterpreter;
import org.keplerproject.ldt.internal.launching.LuaRuntime;
/**
 * 
 * @author guilherme
 * @version $Id$
 */
public class LuaInterpreterPreferencePage extends PreferencePage implements
		IWorkbenchPreferencePage {

	protected CheckboxTableViewer tableViewer;

	protected Button addButton;

	protected Button editButton;

	protected Button removeButton;

	public LuaInterpreterPreferencePage() {
		setPreferenceStore(LauncherPlugin.getDefault().getPreferenceStore());
	}

	public void init(IWorkbench iworkbench) {
	}

	protected Control createContents(Composite parent) {
		noDefaultAndApplyButton();
		Composite composite = createPageRoot(parent);
		Table table = createInstalledInterpretersTable(composite);
		createInstalledInterpretersTableViewer(table);
		createButtonGroup(composite);
		tableViewer
				.setInput(LuaRuntime.getDefault().getInstalledInterpreters());
		LuaInterpreter selectedInterpreter = LuaRuntime.getDefault()
				.getSelectedInterpreter();
		if (selectedInterpreter != null)
			tableViewer.setChecked(selectedInterpreter, true);
		enableButtons();
		return composite;
	}

	protected void createButtonGroup(Composite composite) {
		Composite buttons = new Composite(composite, 0);
		buttons.setLayoutData(new GridData(2));
		GridLayout layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);
		addButton = new Button(buttons, 8);
		addButton.setLayoutData(new GridData(768));
		addButton.setText("Add");
		addButton.addListener(13, new Listener() {

			public void handleEvent(Event evt) {
				addInterpreter();
			}

		});
		editButton = new Button(buttons, 8);
		editButton.setLayoutData(new GridData(768));
		editButton.setText("Edit");
		editButton.addListener(13, new Listener() {

			public void handleEvent(Event evt) {
				editInterpreter();
			}

		});
		removeButton = new Button(buttons, 8);
		removeButton.setLayoutData(new GridData(768));
		removeButton.setText("Remove");
		removeButton.addListener(13, new Listener() {

			public void handleEvent(Event evt) {
				removeInterpreter();
			}

		});
	}

	protected void createInstalledInterpretersTableViewer(Table table) {
		tableViewer = new CheckboxTableViewer(table);
		tableViewer.setLabelProvider(new LuaInterpreterLabelProvider());
		tableViewer.setContentProvider(new LuaInterpreterContentProvider());
		tableViewer
				.addSelectionChangedListener(new ISelectionChangedListener() {

					public void selectionChanged(SelectionChangedEvent evt) {
						enableButtons();
					}

				});
		tableViewer.addCheckStateListener(new ICheckStateListener() {

			public void checkStateChanged(CheckStateChangedEvent event) {
				updateSelectedInterpreter(event.getElement());
			}

		});
		tableViewer.addDoubleClickListener(new IDoubleClickListener() {

			public void doubleClick(DoubleClickEvent e) {
				editInterpreter();
			}

		});
	}

	protected Table createInstalledInterpretersTable(Composite composite) {
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
		return table;
	}

	protected Composite createPageRoot(Composite parent) {
		Composite composite = new Composite(parent, 0);
		GridLayout layout = new GridLayout();
		layout.numColumns = 2;
		composite.setLayout(layout);
		return composite;
	}

	protected void addInterpreter() {
		LuaInterpreter newInterpreter = new LuaInterpreter(null, null);
		EditInterpreterDialog editor = new EditInterpreterDialog(getShell(),
				"Add interpreter");
		editor.create();
		editor.setInterpreterToEdit(newInterpreter);
		if (editor.open() == 0)
			tableViewer.add(newInterpreter);
	}

	protected void removeInterpreter() {
		tableViewer.remove(getSelectedInterpreter());
	}

	protected void enableButtons() {
		if (getSelectedInterpreter() != null) {
			editButton.setEnabled(true);
			removeButton.setEnabled(true);
		} else {
			editButton.setEnabled(false);
			removeButton.setEnabled(false);
		}
	}

	protected void updateSelectedInterpreter(Object interpreter) {
		Object checkedElements[] = tableViewer.getCheckedElements();
		for (int i = 0; i < checkedElements.length; i++)
			tableViewer.setChecked(checkedElements[i], false);

		tableViewer.setChecked(interpreter, true);
	}

	protected void editInterpreter() {
		EditInterpreterDialog editor = new EditInterpreterDialog(getShell(),
				"Edit interpreter");
		editor.create();
		LuaInterpreter anInterpreter = getSelectedInterpreter();
		editor.setInterpreterToEdit(anInterpreter);
		if (editor.open() == 0)
			tableViewer.update(anInterpreter, null);
	}

	protected LuaInterpreter getSelectedInterpreter() {
		IStructuredSelection selection = (IStructuredSelection) tableViewer
				.getSelection();
		return (LuaInterpreter) selection.getFirstElement();
	}

	public boolean performOk() {
		org.eclipse.swt.widgets.TableItem tableItems[] = tableViewer.getTable()
				.getItems();
		List installedInterpreters = new ArrayList(tableItems.length);
		for (int i = 0; i < tableItems.length; i++)
			installedInterpreters.add(tableItems[i].getData());

		LuaRuntime.getDefault().setInstalledInterpreters(installedInterpreters);
		Object checkedElements[] = tableViewer.getCheckedElements();
		if (checkedElements.length > 0)
			LuaRuntime.getDefault().setSelectedInterpreter(
					(LuaInterpreter) checkedElements[0]);
		return super.performOk();
	}

}