package org.keplerproject.ldt.luaprofiler.core.analasyer;

import java.util.ArrayList;
import java.util.List;

import org.keplerproject.luajava.LuaException;
import org.keplerproject.luajava.LuaObject;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;

public class LuaProfilerAnalyser {
	private static LuaProfilerAnalyser analyser = null;
	private String filename;

	private LuaState L;

	private LuaProfilerAnalyser(String filename) {
		if (filename == null)
			return;
		L = LuaStateFactory.newLuaState();
		L.openLibs();
		// loads the file buffer function (FROM LUA PROFILER summary.lua)
		L.LdoString(Messages.LuaProfilerAnalyser_ReadFileFunction);
		L.LdoString(Messages.LuaProfilerAnalyser_CreateSummaryFunction);
		L.LdoString(Messages.LuaProfilerAnalyser_StartAnalysisFunction);
		this.filename = filename;
		refreshSummary();
	}

	public static LuaProfilerAnalyser create(String filename) {
		analyser = new LuaProfilerAnalyser(filename);
		return analyser;
	}

	public static LuaProfilerAnalyser getAnalyser() {
		return analyser;
	}

	public void refreshSummary() {
		if (filename != null)
			L.LdoString("return startSummary('" + filename + "')");
	}

	public Object[] getSummaryList() {
		if (filename == null)
			return new Object[] {};
		refreshSummary();
		List<LuaObject> summaryList = new ArrayList<LuaObject>();

		LuaObject sortedInfo = L.getLuaObject("sorted");

		int idx = 1;
		try {

			LuaObject currWord = L.getLuaObject(sortedInfo, new Integer(idx));
			while (currWord != null && !currWord.isNil()) {
				if (currWord.isTable()) {
					LuaObject info = currWord.getField("info");

					// Average
					info.push();
					L.pushString("average");
					double total = info.getField("total").getNumber();
					double calls = info.getField("calls").getNumber();
					L.pushNumber(total / calls);
					L.setTable(-3);

					L.pushString("time");
					double globalT = L.getLuaObject("global_t").getNumber();
					L.pushNumber(total / globalT * 100);
					L.setTable(-3);
					L.pop(1);

					summaryList.add(info);
				}
				currWord = L.getLuaObject(sortedInfo, new Integer(++idx));
			}
		} catch (LuaException e) {

			e.printStackTrace();
		}

		return summaryList.toArray();
	}

}
