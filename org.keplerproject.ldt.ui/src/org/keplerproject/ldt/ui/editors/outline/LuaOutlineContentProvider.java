package org.keplerproject.ldt.ui.editors.outline;

import org.eclipse.jface.viewers.IContentProvider;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class LuaOutlineContentProvider implements ITreeContentProvider {

	private Object newInput = null;
	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		// TODO Auto-generated method stub
		this.newInput = newInput;
		System.out.println((oldInput != null) ? oldInput.toString() : "null" + '>'+ newInput.toString());
	}

	public Object[] getChildren(Object parentElement) {
		// TODO Auto-generated method stub
		return null;
	}

	public Object getParent(Object element) {
		// TODO Auto-generated method stub
		return null;
	}

	public boolean hasChildren(Object element) {
		// TODO Auto-generated method stub
		return false;
	}

	public Object[] getElements(Object inputElement) {
		// TODO Auto-generated method stub
		return new Object[0];
	}

}
