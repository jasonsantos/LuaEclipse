/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.Arrays;

import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IRegisterGroup;
import org.eclipse.debug.core.model.IStackFrame;
import org.eclipse.debug.core.model.IThread;
import org.eclipse.debug.core.model.IVariable;
import org.keplerproject.ldt.debug.core.LuaDebuggerPlugin;

/**
 * @author jasonsantos
 */
public class LuaStackFrame extends LuaDebugElement implements IStackFrame,
		ILuaEventListener {

	private final LuaDebugThread	fThread;

	private int						fId;
	private String					fFileName;
	private String					fShortFileName;
	private String					fName;
	private String					fType;
	private int						fLine;

	private IProject				fProject;

	/**
	 * @param thread
	 * @param data
	 * @param i
	 */
	public LuaStackFrame(LuaDebugThread thread, String data, int id) {
		super(thread.getLuaDebugTarget());

		try {
			String projectName = thread.getLuaDebugTarget().getLaunch()
					.getLaunchConfiguration().getAttribute(
							LuaDebuggerPlugin.LUA_PROJECT_ATTRIBUTE,
							(String) null);
			fProject = ResourcesPlugin.getWorkspace().getRoot().getProject(
					projectName);
		} catch (CoreException e) {
		}

		fThread = thread;

		init(data);
	}

	private void init(String data) {
		String[] datum = data.split("\\|");
		for (int i = 0; i < datum.length; i++) {
			try {
				datum[i] = URLDecoder.decode(datum[i], "UTF-8");
			} catch (UnsupportedEncodingException e) { }
		}
		fId = Integer.parseInt(datum[0]);
		fName = datum[1];
		fType = datum[2];
		fFileName = datum[3];
		fFileName = fFileName.replace(fProject.getLocation().toFile().toString()+File.separatorChar, "");
		fShortFileName = datum[4];
		fLine = Integer.parseInt(datum[5]);
		System.out.println(data);
		if (fFileName != null && fFileName.length() > 0) {
			IPath filePath = getProjectPath();
			System.out.println(filePath.toPortableString());
			fShortFileName = filePath.toPortableString();
		}
		int numVars = datum.length - 6;
		IVariable[] variables = new IVariable[numVars];
		for (int i = 0; i < numVars; i++) {
			variables[i] = new LuaVariable(this, datum[i + 6]);
		}
		Arrays.sort(variables, LuaVariable.getComparator());
		fThread.setVariables(this, variables);
	}

	/**
	 * @return
	 */
	public IPath getProjectPath() {
		IPath filePath = Path.fromOSString(fFileName);

		IPath projectPath = fProject.getLocation();
		if (filePath.matchingFirstSegments(projectPath) == projectPath
				.segmentCount())
			filePath = filePath.removeFirstSegments(projectPath.segmentCount());
		
		return filePath;
	}

	/**
	 * @return the fFileName
	 */
	public IPath getFilePath() {
		IPath projectPath = fProject.getLocation();
		IPath filePath = Path.fromOSString(fFileName);
		return projectPath.append(fFileName);
//		return fFileName.toString();
	}

	/**
	 * 
	 */
	public IProject getProject() {
		return fProject;

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#getCharEnd()
	 */

	public int getCharEnd() throws DebugException {

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#getCharStart()
	 */

	public int getCharStart() throws DebugException {

		return -1;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#getLineNumber()
	 */

	public int getLineNumber() throws DebugException {
		return fLine;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#getName()
	 */

	public String getName() throws DebugException {

		return fShortFileName + ':' + fName;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#getRegisterGroups()
	 */

	public IRegisterGroup[] getRegisterGroups() throws DebugException {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#getThread()
	 */

	public IThread getThread() {
		return fThread;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#getVariables()
	 */

	public IVariable[] getVariables() throws DebugException {
		// TODO: return variables from this function
		return fThread.getVariables(this);
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#hasRegisterGroups()
	 */

	public boolean hasRegisterGroups() throws DebugException {
		return false;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStackFrame#hasVariables()
	 */

	public boolean hasVariables() throws DebugException {
		return getVariables().length > 0;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepInto()
	 */
	public boolean canStepInto() {
		return getThread().canStepInto();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepOver()
	 */
	public boolean canStepOver() {
		return getThread().canStepOver();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#canStepReturn()
	 */
	public boolean canStepReturn() {
		return getThread().canStepReturn();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#isStepping()
	 */
	public boolean isStepping() {

		return getThread().isStepping();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepInto()
	 */
	public void stepInto() throws DebugException {
		getThread().stepInto();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepOver()
	 */
	public void stepOver() throws DebugException {
		getThread().stepOver();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IStep#stepReturn()
	 */
	public void stepReturn() throws DebugException {
		getThread().stepReturn();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canResume()
	 */
	public boolean canResume() {
		return getThread().canResume();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#canSuspend()
	 */
	public boolean canSuspend() {
		return getThread().canSuspend();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#isSuspended()
	 */
	public boolean isSuspended() {
		return getThread().isSuspended();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#resume()
	 */
	public void resume() throws DebugException {
		getThread().resume();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ISuspendResume#suspend()
	 */
	public void suspend() throws DebugException {
		getThread().suspend();

	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#canTerminate()
	 */
	public boolean canTerminate() {
		return getThread().canTerminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#isTerminated()
	 */
	public boolean isTerminated() {
		return getThread().isTerminated();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.ITerminate#terminate()
	 */
	public void terminate() throws DebugException {
		getThread().terminate();
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.keplerproject.ldt.debug.core.model.ILuaEventListener#handleEvent(java.lang.String)
	 */

	public void handleEvent(String event) {
		if (event.startsWith("101")) {
			String framesData = event.substring("101 Stack ".length());
			String[] frames = framesData.split("#");
			for (int i = 0; i < frames.length; i++) {
				String[] datum = frames[i].split("\\|");
				try {
					int id = Integer.parseInt(URLDecoder.decode(datum[0], "UTF-8"));
					if (fId == id) {
						init(frames[i]);
					}
				} catch (UnsupportedEncodingException e) { }
			}
		}
	}

	public IProject getFProject() {
		return fProject;
	}

	/**
	 * @return
	 */
	public int getId() {
		return fId;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (!(obj instanceof LuaStackFrame))
			return false;
		LuaStackFrame lsf = (LuaStackFrame) obj;
		return this == lsf || fFileName.equals(lsf.fFileName) && fId == lsf.fId
				&& fLine == lsf.fLine && fName.equals(lsf.fName)
				&& fType.equals(lsf.fType);
	}
}
