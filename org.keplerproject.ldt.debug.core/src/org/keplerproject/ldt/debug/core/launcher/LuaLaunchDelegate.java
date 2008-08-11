/**
 * 
 */
package org.keplerproject.ldt.debug.core.launcher;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.text.MessageFormat;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Platform;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;
import org.keplerproject.ldt.debug.core.model.LuaDebugServer;
import org.keplerproject.ldt.debug.core.model.LuaDebugTarget;

/**
 * @author jasonsantos
 */
public class LuaLaunchDelegate implements ILaunchConfigurationDelegate {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ILaunchConfigurationDelegate#launch(org.eclipse.debug.core.ILaunchConfiguration,
	 *      java.lang.String, org.eclipse.debug.core.ILaunch,
	 *      org.eclipse.core.runtime.IProgressMonitor)
	 */
	@Override
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {

		String projectName = configuration.getAttribute(
				LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE, (String) null);

		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace()
				.getRoot();
		IProject project = myWorkspaceRoot.getProject(projectName);

		String script = configuration.getAttribute(
				LuaDebuggerPlugin.LUA_SCRIPT_ATTRIBUTE, (String) null);

		IFile p = myWorkspaceRoot.getFile(new Path(script));

		if (!p.exists()) {
			String msg = MessageFormat.format("Lua script {0} does not exist.",
					new String[] { p.getFullPath().toString() }, null);
			error(msg, new FileNotFoundException());

		}

		String scriptPath = p.getLocation().toOSString();

		List<String> commandList = new ArrayList<String>();

		commandList.add(getLuaInterpreter(project));
		commandList.add(getLuaDebugClient());

		int controlPort = -1;
		int eventPort = -1;
		// TODO obtain host from configuration
		String controlHost = "localhost";

		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			commandList.add("--debug");

			controlPort = findFreePort();
			eventPort = findFreePort();

			if (controlPort == -1 || eventPort == -1)
				error("Could not allocate debug port", null);

			commandList.add("--host=" + controlHost);
			commandList.add("--port=" + controlPort);
		}

		commandList.add(scriptPath);

		String[] commandLine = commandList.toArray(new String[commandList
				.size()]);

		LuaDebugServer server = null;
		try {
			server = new LuaDebugServer(controlHost, controlPort, eventPort);

		} catch (IOException e) {
			throw new DebugException(new Status(IStatus.ERROR, DebugPlugin
					.getUniqueIdentifier(), "Could not connect to RemDebug", e));
		}

		Process process = DebugPlugin.exec(commandLine, null);
		IProcess proc = DebugPlugin.newProcess(launch, process,
				"Standard Lua Remdebug Engine");

		try {
			Thread.sleep(1000);
		} catch (InterruptedException e) {
		}
		// if in debug mode, create a debug target
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			IDebugTarget target = new LuaDebugTarget(launch, proc, scriptPath,
					server);
			launch.addDebugTarget(target);
		}

	}

	/**
	 * Throws an error with a message
	 * 
	 * @param msg
	 * @throws CoreException
	 */
	private void error(String msg, Exception e) throws CoreException {
		throw new CoreException(new Status(IStatus.ERROR,
				LuaDebuggerPlugin.PLUGIN_ID, 0, msg, e));
	}

	/**
	 * Returns the absolute path of the remdebug client
	 * 
	 * @return absolute path of the remdebug client
	 */
	private String getLuaDebugClient() {
		File f = LuaDebuggerPlugin.getFileInPlugin(new Path(
				"lua/5.1/debugger/client.lua"));
		return f.getAbsolutePath();
	}

	/**
	 * Returns the name of the Lua Interpreter for the project
	 * 
	 * @return
	 */
	private String getLuaInterpreter(IProject prj) {
		// TODO obtain the LuaInterpreter command-line for the given project
		if (Platform.getOS() == Platform.OS_WIN32)
			return "lua.exe";
		else
			return "lua";
	}

	public static int findFreePort() {
		ServerSocket socket = null;
		try {
			socket = new ServerSocket(0);
			return socket.getLocalPort();
		} catch (IOException e) {
		} finally {
			if (socket != null) {
				try {
					socket.close();
				} catch (IOException e) {
				}
			}
		}
		return -1;
	}

}
