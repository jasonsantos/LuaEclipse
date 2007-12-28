package org.keplerproject.ldt.luaprofiler.core.views;

import org.eclipse.debug.core.model.IProcess;
import org.eclipse.jface.viewers.IStructuredContentProvider;
import org.eclipse.jface.viewers.Viewer;
import org.keplerproject.ldt.luaprofiler.core.analasyer.LuaProfilerAnalyser;
import org.keplerproject.ldt.luaprofiler.core.analasyer.LuaProfilerAnalyserListener;

public class LuaProfilerContentProvider implements IStructuredContentProvider {
	private LuaProfilerAnalyser analyser;
	private LuaProfilerAnalyserListener listener;
	private static LuaProfilerContentProvider provider = new LuaProfilerContentProvider();

	protected LuaProfilerContentProvider() {
	}

	public static LuaProfilerContentProvider getContentProvider() {
		if (provider == null)
			provider = new LuaProfilerContentProvider();
		return provider;
	}

	public Object[] getElements(Object parent) {
		this.analyser = LuaProfilerAnalyser.getAnalyser();
		if (analyser != null) {
			return analyser.getSummaryList();
		} else {
			return new Object[] {};
		}
	}
	public void dispose() {
	}

	public void inputChanged(Viewer view, Object oldInput, Object newInput) {
	}

	public void setListener(LuaProfilerAnalyserListener listener) {
		this.listener = listener;
	}

	public void fireChange(IProcess process) {
		if (listener != null)
			listener.profilerDataChanged(process);
	}
}
