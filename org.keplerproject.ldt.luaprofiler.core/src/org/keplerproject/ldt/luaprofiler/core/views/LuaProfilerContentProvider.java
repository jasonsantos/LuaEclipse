package org.keplerproject.ldt.luaprofiler.core.views;

import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.keplerproject.ldt.luaprofiler.core.analasyer.LuaProfilerAnalyser;

public class LuaProfilerContentProvider implements IStructuredContentProvider {
	private LuaProfilerAnalyser analyser;
	
	public LuaProfilerContentProvider() {
	}
	
	@Override
	public Object[] getElements(Object parent) {
		this.analyser = LuaProfilerAnalyser.getAnalyser();
		if (analyser != null) {
			analyser.refreshSummary();
			return analyser.getSummaryList();
		} else {
			return new Object[] {};
		}
	}

	@Override
	public void dispose() {
	}

	@Override
	public void inputChanged(Viewer view, Object oldInput, Object newInput) {
	}
}
