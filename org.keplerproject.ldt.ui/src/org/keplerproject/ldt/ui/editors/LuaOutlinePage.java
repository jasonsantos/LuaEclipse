package org.keplerproject.ldt.ui.editors;

import org.eclipse.jface.viewers.TreeViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.ui.IEditorInput;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.views.contentoutline.ContentOutlinePage;
import org.eclipse.ui.views.contentoutline.IContentOutlinePage;
import org.keplerproject.ldt.ui.editors.outline.LuaOutlineContentProvider;
import org.keplerproject.ldt.ui.editors.outline.LuaOutlineLabelProvider;

public class LuaOutlinePage extends ContentOutlinePage implements
		IContentOutlinePage {

	IEditorInput in;
	
	public LuaOutlinePage(IDocumentProvider documentProvider,
			LuaEditor luaEditor) {
		super();
	}

	public void setInput(IEditorInput editorInput) {
		in = editorInput;
		System.out.println("setInput");
	}
	
	private void sysout(String name) {
		// TODO Auto-generated method stub
		
	}

	public void createControl(Composite parent) {
	      super.createControl(parent);
	      TreeViewer viewer= getTreeViewer();
	      viewer.setContentProvider(new LuaOutlineContentProvider());
	      viewer.setLabelProvider(new LuaOutlineLabelProvider());
	      viewer.addSelectionChangedListener(this);
	      viewer.setInput(in);
	   }

	
}
