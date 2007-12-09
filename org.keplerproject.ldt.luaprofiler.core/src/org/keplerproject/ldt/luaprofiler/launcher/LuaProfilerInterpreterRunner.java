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
import org.keplerproject.ldt.luaprofiler.core.analasyer.LuaProfilerAnalyser;

public class LuaProfilerInterpreterRunner extends InterpreterRunner {
	private static final Logger log = Logger.getLogger("profiler");

	@Override
	protected String renderCommandLine(InterpreterRunnerConfiguration configuration) {
		File profiled = null;
		String profilerLib = "profiler";
		try {
			profiled = File.createTempFile(configuration.getFileName(), "lua");
			profiled.deleteOnExit();
			BufferedInputStream in = new BufferedInputStream(new FileInputStream(new File(configuration.getAbsoluteFileName())));
			BufferedOutputStream out = new BufferedOutputStream(new FileOutputStream(profiled));

			File f = File.createTempFile(configuration.getFileName() + ".prof", "lua");
			LuaProfilerAnalyser.create(f.getAbsolutePath());
			byte[] buf = new byte[1024];
			int len;
			out.write(("profiler = require\"" + profilerLib + "\"\n").getBytes());
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
		return process;
	}
}
