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
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IWorkspaceRoot;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.Status;
import org.eclipse.debug.core.DebugEvent;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.DebugPlugin;
import org.eclipse.debug.core.ILaunch;
import org.eclipse.debug.core.ILaunchConfiguration;
import org.eclipse.debug.core.ILaunchManager;
import org.eclipse.debug.core.model.IDebugTarget;
import org.eclipse.debug.core.model.ILaunchConfigurationDelegate;
import org.eclipse.debug.core.model.IProcess;
import org.eclipse.debug.core.model.IStreamsProxy;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;
import org.keplerproject.ldt.debug.core.model.LuaDebugServer;
import org.keplerproject.ldt.debug.core.model.LuaDebugTarget;
import org.keplerproject.ldt.internal.launching.LuaInterpreter;
import org.keplerproject.ldt.internal.launching.LuaRuntime;

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
	
	public void launch(ILaunchConfiguration configuration, String mode,
			ILaunch launch, IProgressMonitor monitor) throws CoreException {
		
		String projectName = configuration.getAttribute(
				LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE, (String) null);

		IWorkspaceRoot myWorkspaceRoot = ResourcesPlugin.getWorkspace()
				.getRoot();
		IProject project = myWorkspaceRoot.getProject(projectName);
		
		boolean remoteDbgEnabled = configuration.getAttribute(LuaDebuggerPlugin.LUA_REMOTE_DBG_ENABLED_ATTRIBUTE, false);
		int remoteDbgPort = configuration.getAttribute(LuaDebuggerPlugin.LUA_REMOTE_DBG_PORT_ATTRIBUTE, 8171);
		
		// TODO obtain host and ports from configuration
		String controlHost = "localhost";
		int controlPort = -1;
		int eventPort = -1;
		IProcess proc;
		
		if (remoteDbgEnabled) {
			controlPort = remoteDbgPort;
			eventPort = findFreePort();
			if (controlPort == -1 || eventPort == -1)
				error("Could not allocate debug port", null);
			proc = new RemoteProcess(launch, "Remote Lua Debugger");
			
		} else {
			String script = configuration.getAttribute(
					LuaDebuggerPlugin.LUA_SCRIPT_ATTRIBUTE, (String) null);
	
			String args = configuration.getAttribute(
					LuaDebuggerPlugin.LUA_ARGS_ATTRIBUTE, (String) null);
	
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
	
			commandList.add("--prefix=" + getLuaModulesPath());
	
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
			
			String[] argList = DebugPlugin.parseArguments(args);
			for (String arg : argList)
				commandList.add(arg);
	
			String[] commandLine = commandList.toArray(new String[commandList
					.size()]);
			
			File scriptPathFile = new File(scriptPath).getParentFile();
			Process process = DebugPlugin.exec(commandLine, scriptPathFile);
			proc = DebugPlugin.newProcess(launch, process,
					"Standard Lua Remdebug Engine");
		}
		
		LuaDebugServer server = null;
		try {
			server = new LuaDebugServer(controlHost, controlPort, eventPort);

		} catch (IOException e) {
			throw new DebugException(new Status(IStatus.ERROR, DebugPlugin
					.getUniqueIdentifier(), "Could not connect to RemDebug", e));
		}

		// if in debug mode, create a debug target
		if (mode.equals(ILaunchManager.DEBUG_MODE)) {
			IDebugTarget target = new LuaDebugTarget(launch, proc, server);
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

	private String getLuaModulesPath() {
		File f = LuaDebuggerPlugin.getFileInPlugin(new Path("lua/5.1/"));
		if (f != null)
			return f.getAbsolutePath();
		return null;

	}

	/**
	 * Returns the absolute path of the remdebug client
	 * 
	 * @return absolute path of the remdebug client
	 */
	private String getLuaDebugClient() throws DebugException {
		File f = LuaDebuggerPlugin.getFileInPlugin(new Path(
				"lua/5.1/debugger/client.lua"));
		if (f == null)
			throw new DebugException(new Status(IStatus.ERROR, DebugPlugin
					.getUniqueIdentifier(),
					"Could not locate remdebug eclipse client",
					new FileNotFoundException()));

		return f.getAbsolutePath();
	}

	/**
	 * Returns the name of the Lua Interpreter for the project
	 * 
	 * @return
	 * @throws CoreException 
	 */
	private String getLuaInterpreter(IProject prj) throws CoreException {
		String interpreter;
		
		LuaInterpreter li = LuaRuntime.getDefault().getSelectedInterpreter();
		interpreter = li.getFileName();
		
		return interpreter;
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

class RemoteProcess implements IProcess {

	private ILaunch launch;
	private HashMap<String,String> attrs = new HashMap<String,String>();
	private String label;
	private boolean termintated;

	public RemoteProcess(ILaunch launch, String label) {
		this.launch = launch;
		this.label = label;
		launch.addProcess(this);
	}
	
	public String getAttribute(String key) {
		return attrs.get(key);
	}

	public int getExitValue() throws DebugException {
		if (!termintated)
			throw new DebugException(new Status(IStatus.ERROR, DebugPlugin
					.getUniqueIdentifier(),
					"Remote debugger not terminated"));
		return 0;
	}

	public String getLabel() {
		return label;
	}

	public ILaunch getLaunch() {
		return launch;
	}

	public IStreamsProxy getStreamsProxy() {
		return null;
	}

	public void setAttribute(String key, String value) {
		attrs.put(key, value);
	}

	public Object getAdapter(Class adapter) {
		return null;
	}

	public boolean canTerminate() {
		return !isTerminated();
	}

	public boolean isTerminated() {
		return termintated;
	}

	public void terminate() throws DebugException {
		if (!termintated) {
			termintated = true;
			launch.terminate();
		}
	}
}
