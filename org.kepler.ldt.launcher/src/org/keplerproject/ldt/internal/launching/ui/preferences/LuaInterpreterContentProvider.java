package org.keplerproject.ldt.internal.launching.ui.preferences;

import java.util.List;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;

public class LuaInterpreterContentProvider implements
		IStructuredContentProvider {

	protected List interpreters;

	public LuaInterpreterContentProvider() {
	}

	public Object[] getElements(Object inputElement) {
		return interpreters.toArray();
	}

	public void dispose() {
	}

	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		interpreters = (List) newInput;
	}

}