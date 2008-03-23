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
package org.keplerproject.ldt.ui.preferences;

import java.util.Set;

import org.eclipse.jface.dialogs.Dialog;
import org.eclipse.jface.dialogs.InputDialog;
import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.jface.preference.PreferencePage;
import org.eclipse.jface.preference.StringFieldEditor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.keplerproject.ldt.core.LuaScriptsSpecs;
import org.keplerproject.ldt.ui.LDTUIPlugin;
/**
 * Lua Preference page Extension. The Blank root page.
 * @author guilherme
 * @author jasonsantos
 * @version $Id$
 */
public class LuaPreferencePage extends  PreferencePage implements IWorkbenchPreferencePage
{

	private Table scriptsTable;

	private Button addButton;

	private Button removeButton;
	
	private Button luadocAutoGen;

	public LuaPreferencePage() {
		
	}
	
	public void init(IWorkbench workbench) {
		LuaScriptsSpecs.getDefault().setPreferenceStore(LDTUIPlugin.getDefault().getPreferenceStore());
	}

	@Override
	protected Control createContents(Composite parent) {
		createFileNamesTable(parent);

		return parent;
	}

	private void createFileNamesTable(Composite parent) {
		Group group = new Group(parent, SWT.SHADOW_ETCHED_IN);
		group.setText("Lua Scripts Filename Patterns");
		GridLayout layout = new GridLayout(1, false);
		group.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		group.setLayout(layout);

		Group lowerGroup = new Group(parent, SWT.SHADOW_ETCHED_IN);
		lowerGroup.setText("LuaDoc Integration Config");
		lowerGroup.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));
		lowerGroup.setLayout(layout);
		
		Composite composite = new Composite(group, SWT.NULL);
		layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		composite.setLayout(layout);
		composite.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		Label l1 = new Label(composite, SWT.NULL);
		l1.setText("Matching file names will be treated as lua scripts");
		GridData data = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		data.horizontalSpan = 2;
		l1.setLayoutData(data);

		scriptsTable = new Table(composite, SWT.BORDER);
		data = new GridData(GridData.FILL_BOTH);
		data.heightHint = 60;
		scriptsTable.setLayoutData(data);
		scriptsTable.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				handleSelection();
			}
		});

		Composite buttons = new Composite(composite, SWT.NULL);
		buttons.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_BEGINNING));
		layout = new GridLayout();
		layout.marginHeight = 0;
		layout.marginWidth = 0;
		buttons.setLayout(layout);

		
		Composite luadocControls = new Composite(lowerGroup, SWT.NULL);
		layout = new GridLayout();
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.numColumns = 2;
		luadocControls.setLayout(layout);
		luadocControls.setLayoutData(new GridData(GridData.FILL_HORIZONTAL));

		luadocAutoGen = new Button(luadocControls, SWT.CHECK);
		luadocAutoGen.setText("Allow LuaDoc to index your sources at every save");
		
		
		addButton = new Button(buttons, SWT.PUSH);
		addButton.setText("Add...");
		addButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				addIgnore();
			}
		});

		removeButton = new Button(buttons, SWT.PUSH);
		removeButton.setText("Remove");
		removeButton.setEnabled(false);
		removeButton.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				removeIgnore();
			}
		});
		fillTable(LuaScriptsSpecs.getDefault().getLuaScriptPatterns());
		luadocAutoGen.setSelection(LuaScriptsSpecs.getDefault().isLuaDocAutoGenerationActive());

		Dialog.applyDialogFont(group);
		setButtonLayoutData(addButton);
		setButtonLayoutData(removeButton);
	}

	/**
	 * Do anything necessary because the OK button has been pressed.
	 * 
	 * @return whether it is okay to close the preference page
	 */
	@Override
	public boolean performOk() {
		TableItem[] items = scriptsTable.getItems();
		for (int i = 0; i < items.length; i++) {
			LuaScriptsSpecs.getDefault().addLuaScriptPattern(items[i].getText());		
		}
		
		LuaScriptsSpecs.getDefault().setLuaDocAutoGeneration(luadocAutoGen.getSelection());
		
		LuaScriptsSpecs.getDefault().savePatterns();
		
		return true;
	}

	@Override
	protected void performDefaults() {
		super.performDefaults();
		scriptsTable.removeAll();
		LuaScriptsSpecs.getDefault().setDefaultSpecs();
		
		fillTable(LuaScriptsSpecs.getDefault().getLuaScriptPatterns());
		luadocAutoGen.setSelection(LuaScriptsSpecs.getDefault().isLuaDocAutoGenerationActive());
	}

	/**
	 * @param ignore
	 */
	private void fillTable(Set<String> patterns) {
		for (String pattern : patterns) {
			TableItem item = new TableItem(scriptsTable, SWT.NONE);
			item.setText(pattern);
		}
	}

	private void addIgnore() {
		InputDialog dialog = new InputDialog(getShell(), "Add Extension of Lua Scripts", "Enter pattern (* = any string)",
				null, null); // 
		dialog.open();
		if (dialog.getReturnCode() != Window.OK) {
			return;
		}
		String pattern = dialog.getValue();
		if (pattern.equals("")) {
			return; //$NON-NLS-1$
		}
		TableItem item = new TableItem(scriptsTable, SWT.NONE);
		item.setText(pattern);
		item.setChecked(true);
	}

	private void removeIgnore() {
		int[] selection = scriptsTable.getSelectionIndices();
		scriptsTable.remove(selection);
	}

	private void handleSelection() {
		if (scriptsTable.getSelectionCount() > 0) {
			removeButton.setEnabled(true);
		} else {
			removeButton.setEnabled(false);
		}
	}
	
}