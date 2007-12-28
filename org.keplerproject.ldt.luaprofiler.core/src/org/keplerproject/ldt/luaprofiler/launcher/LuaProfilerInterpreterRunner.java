package org.keplerproject.ldt.luaprofiler.launcher;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.model.IProcess;
import org.keplerproject.ldt.internal.launching.InterpreterRunner;
import org.keplerproject.ldt.internal.launching.InterpreterRunnerConfiguration;
import org.keplerproject.ldt.luaprofiler.core.LuaProfiler;
import org.keplerproject.ldt.luaprofiler.core.LuaProfiler.LuaProfilerInfo;
import org.keplerproject.ldt.luaprofiler.core.analasyer.LuaProfilerAnalyser;
import org.keplerproject.ldt.luaprofiler.core.views.LuaProfilerContentProvider;

public class LuaProfilerInterpreterRunner extends InterpreterRunner {
	private static final Logger log = Logger.getLogger("profiler");

	@Override
	protected String renderLabel(InterpreterRunnerConfiguration configuration) {
		// TODO Auto-generated method stub
		return super.renderLabel(configuration);
	}
	
	@Override
	protected String renderCommandLine(InterpreterRunnerConfiguration configuration) {
		File profiled = null;
		LuaProfilerInfo profilerLib = LuaProfiler.getDefault().getSelectedProfiler();
		if (profilerLib == null) {
			return configuration.getAbsoluteFileName();
		}
		try {
			profiled = File.createTempFile(configuration.getFileName(), "lua");
			profiled.deleteOnExit();
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(configuration.getAbsoluteFileName())));
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(profiled));
			
		
			File f = File.createTempFile(configuration.getFileName() + ".prof", "lua");
			f.deleteOnExit();
			LuaProfilerAnalyser.create(f.getAbsolutePath());

			int i = profilerLib.getFile().getName().lastIndexOf('.');
			int j = profilerLib.getFile().getPath().lastIndexOf(File.separatorChar);
			String profilerName = profilerLib.getFile().getName().substring(0, i);
			String profilerExtension = profilerLib.getFile().getName().substring(i, profilerLib.getName().length());
			String profilerPath = profilerLib.getFile().getPath().substring(0, j) + File.separatorChar + "?" + profilerExtension;
			
			byte[] buf = new byte[1024];
			int len;
			out.write(("_ = LUA_PATH\n").getBytes());
			out.write(("LUA_PATH = \"" + profilerPath + "\"\n").getBytes());
			out.write(("profiler = require\"" + profilerName + "\"\n").getBytes());
			out.write("LUA_PATH = _\n".getBytes());			
			out.write(("profiler.start(\"" + f.getAbsolutePath() + "\")\n").getBytes());
			while ((len = in.read(buf)) > 0) {
				out.write(buf, 0, len);
			}
			out.write("\nprofiler.stop()".getBytes());
			in.close();
			out.close();
			
		} catch (FileNotFoundException e) {
			log.log(Level.WARNING, String.format("File %s not found", configuration.getAbsoluteFileName()), e);
		} catch (IOException e) {
			log.log(Level.WARNING, "I/O error", e);
		}
		

		return profiled.getAbsolutePath();
	}

	@Override
	public IProcess run(InterpreterRunnerConfiguration configuration, ILaunch launch) {
		IProcess process = super.run(configuration, launch);
		LuaProfilerContentProvider.getContentProvider().fireChange(process);
		return process;
	}
}
