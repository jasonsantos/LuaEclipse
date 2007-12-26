package org.keplerproject.ldt.luaprofiler.core.analasyer;

import org.eclipse.debug.core.model.IProcess;

public interface LuaProfilerAnalyserListener {
	public void profilerDataChanged(IProcess process);
}
