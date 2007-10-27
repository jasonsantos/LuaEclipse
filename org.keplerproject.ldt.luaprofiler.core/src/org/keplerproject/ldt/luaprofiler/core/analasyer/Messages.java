package org.keplerproject.ldt.luaprofiler.core.analasyer;

import org.eclipse.osgi.util.NLS;

public class Messages extends NLS {
	private static final String BUNDLE_NAME = "org.keplerproject.ldt.luaprofiler.core.analasyer.messages"; //$NON-NLS-1$

	public static String LuaProfilerAnalyser_ReadFileFunction;
	public static String LuaProfilerAnalyser_CreateSummaryFunction;
	public static String LuaProfilerAnalyser_StartAnalysisFunction;
	public static String LuaProfilerAnalyser_GetWordsListFunction;

	
	static {
		// initialize resource bundle
		NLS.initializeMessages(BUNDLE_NAME, Messages.class);
	}

	private Messages() {
	}
}
