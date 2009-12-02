package org.keplerproject.ldt.ui.editors;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.text.TextSelection;
import org.eclipse.jface.viewers.DoubleClickEvent;
import org.eclipse.jface.viewers.IDoubleClickListener;
import org.eclipse.jface.viewers.ISelectionProvider;
import org.eclipse.jface.viewers.IStructuredSelection;
import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.jface.viewers.ViewerComparator;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.keplerproject.ldt.ui.LDTUIPlugin;
import org.keplerproject.ldt.ui.editors.outline.LuaOutlineContentProvider;
import org.keplerproject.ldt.ui.editors.outline.LuaOutlineLabelProvider;
import org.keplerproject.ldt.ui.editors.outline.LuaOutlineContentProvider.FunctionDefinition;

public class LuaOutlinePage extends ContentOutlinePage implements IContentOutlinePage {

	LuaEditor 	fTargetEditor;

	IAction 	fSortByName;

	/**
	 * Provide a viewer comparator that sorts items based on their offset
	 * locations in the document.  This allows items to be listed in their
	 * order of appearance in the file.
	 */
	class DocumentOffsetComparator extends ViewerComparator {
	    public int compare(Viewer viewer, Object arg0, Object arg1) {
			if(!(arg0 instanceof FunctionDefinition) || !(arg1 instanceof FunctionDefinition)) {
				return super.compare(viewer, arg0, arg1);
			}
			FunctionDefinition fd0 = (FunctionDefinition)arg0;
			FunctionDefinition fd1 = (FunctionDefinition)arg1;
			return fd0.getCharacterOffset() - fd1.getCharacterOffset();
		}		
	    
	    public boolean isSorterProperty(Object element, String property) {
			if(LuaOutlineContentProvider.OFFSET_PROPERTY.equals(property)) {
				return true;
			}
	        return false;
	    }
	}
	
	public LuaOutlinePage(IDocumentProvider documentProvider, LuaEditor luaEditor) {
		super();
		setInput(luaEditor);
	}

	public void setInput(LuaEditor luaEditor) {
		fTargetEditor = (LuaEditor)luaEditor;

		TreeViewer viewer = getTreeViewer();
		if(viewer != null) {
			viewer.setInput(fTargetEditor);
		}
	}
	
	public void createControl(Composite parent) {
		createActions();
		
		super.createControl(parent);
	      
		TreeViewer viewer = getTreeViewer();
	      
		viewer.setLabelProvider(new LuaOutlineLabelProvider());
		viewer.setContentProvider(new LuaOutlineContentProvider());
		viewer.setInput(fTargetEditor);

		viewer.addDoubleClickListener(new IDoubleClickListener() {
			public void doubleClick(DoubleClickEvent event) {
				setEditorPosition();
			}
		});

		addActions();
		updateSortingStyle();
	}
	
	protected void createActions() {
		String SORT_ICON_PATH = "icons/sort.gif";
		
		fSortByName = new Action("Sort") {
			public void run() {
				updateSortingStyle();
			}
		};
		fSortByName.setChecked(false);
		fSortByName.setImageDescriptor(LDTUIPlugin.getImageDescriptor(SORT_ICON_PATH));
	}
	
	protected void addActions() {
	      IToolBarManager manager = getSite().getActionBars().getToolBarManager();
	      manager.add(fSortByName);
	}

	protected void updateSortingStyle() {
		TreeViewer viewer = getTreeViewer();
		if(viewer == null) {
			return;
		}

		if(fSortByName.isChecked()) {
			viewer.setComparator(new ViewerComparator());
		} else {
			viewer.setComparator(new DocumentOffsetComparator());
		}
	}
	
	protected void setEditorPosition() {
		IStructuredSelection ss = (IStructuredSelection)getSelection();
		if(ss == null || ss.isEmpty()) {
			return;
		}
		
		Object item = ss.getFirstElement();
		if(!(item instanceof FunctionDefinition)) {
			return;
		}
		
		FunctionDefinition fd = (FunctionDefinition)item;
		TextSelection ts = new TextSelection(fd.getCharacterOffset(), 
											 fd.getCharacterEndOffset() - fd.getCharacterOffset());
		
		ISelectionProvider provider = fTargetEditor.getSelectionProvider();
		if(provider != null) {
			provider.setSelection(ts);
		}
	}
}