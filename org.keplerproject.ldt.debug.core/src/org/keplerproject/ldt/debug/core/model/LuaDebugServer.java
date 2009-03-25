/**
 * 
 */
package org.keplerproject.ldt.debug.core.model;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

import org.eclipse.core.runtime.IProgressMonitor;
import org.eclipse.core.runtime.IStatus;
import org.eclipse.core.runtime.Status;
import org.eclipse.core.runtime.jobs.Job;
import org.eclipse.debug.core.DebugException;
import org.eclipse.debug.core.model.IDebugTarget;

/**
 * @author jasonsantos
 */
public class LuaDebugServer {
	String					fHost;
	int						fControlPort;
	int						fEventPort;

	private LuaDebugElement	fElement;

	// sockets to communicate with VM
	private ServerSocket	fRemdebugServer;
	private Socket			fRemdebugSocket;
	private Socket			fRemdebugEventSocket;
	private PrintWriter		fRequestWriter;
	private BufferedReader	fRequestReader;

	// reader for event data
	private ServerSocket	fEventServer;
	private BufferedReader	fEventReader;

	private boolean			fStarted;
	private boolean			fListening;

	private final Job		fRequestJob	= new Job("Remdebug Request Dispatcher") {
		@Override
		protected IStatus run(IProgressMonitor monitor) {

			try {
				start();

				return Status.OK_STATUS;
			} catch (DebugException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}

			terminate();
			fElement.getLuaDebugTarget().terminated();
			return Status.CANCEL_STATUS;
		}
	};

	private final Job		fEventsJob	= new Job("Remdebug Event Dispatcher") {
		@Override
		protected IStatus run(IProgressMonitor monitor) {
			LuaDebugTarget target = null;

			while (!fStarted)
				Thread.yield();

			try {
				String event = receive();
				if (event == null) {
					terminate();
					return Status.CANCEL_STATUS;
				}
				
				// Thread.sleep(1000);
				target = fElement.getLuaDebugTarget();
				while ((target != null && !target.isTerminated())
						&& event != null) {

					if (event != null) {
						for (ILuaEventListener l : target.getEventListeners()) {
							l.handleEvent(event);
						}
					}

					if (!target.isTerminated())
						event = receive();
				}
			} catch (IOException e) {
				target.terminated();
			} catch (DebugException e) {
				System.out.println("Error receiving event:" + e.getMessage());
				target.terminated();
			}
			terminate();
			return Status.OK_STATUS;
		}
	};

	public LuaDebugServer(String host, int controlPort, int eventPort)
			throws DebugException, IOException {

		fControlPort = controlPort;
		fEventPort = eventPort;
		fHost = host;

		fRequestJob.setSystem(true);
		fEventsJob.setSystem(true);

		fRequestJob.schedule();
	}

	/**
	 * @throws DebugException
	 */
	private void start() throws DebugException, IOException {

		fRemdebugServer = new ServerSocket(fControlPort);
		fRemdebugServer.setSoTimeout(5000);
		fRemdebugSocket = fRemdebugServer.accept();
		fRemdebugServer.close();

		fRequestWriter = new PrintWriter(fRemdebugSocket.getOutputStream());
		fRequestReader = new BufferedReader(new InputStreamReader(
				fRemdebugSocket.getInputStream()));

		fStarted = true;

		startListen();

		fEventsJob.schedule();
	}

	private synchronized void startListen() throws IOException {
		if (!fListening) {
			fEventServer = new ServerSocket(fEventPort);
			fRemdebugEventSocket = fEventServer.accept();
			fListening = true;
		}
	}

	public void register(IDebugTarget target) throws DebugException {
		try {

			fElement = new LuaDebugElement(target);
			/*
			 * TODO create several event ports for multithreading debuggers
			 */
			while (!fStarted)
				Thread.yield();

			String result = sendRequest("SUBSCRIBE " + fEventPort);

			if (!result.startsWith("200"))
				throw new IOException("Can't connect to event port");

			while (!fListening)
				Thread.yield();

			fEventReader = new BufferedReader(new InputStreamReader(
					fRemdebugEventSocket.getInputStream()));

		} catch (UnknownHostException e) {
			fElement.requestFailed("Unable to connect to Remdebug", e);
		} catch (IOException e) {
			fElement.requestFailed("Unable to connect to Remdebug", e);
		}
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.debug.core.model.IDebugElement#getModelIdentifier()
	 */

	public String getModelIdentifier() {
		return fElement.getDebugTarget().getModelIdentifier();
	}

	public String sendRequest(String request) throws IOException {
		synchronized (fRemdebugSocket) {
			System.out.println(">>> " + request);
			fRequestWriter.println(request);
			fRequestWriter.flush();
			String response = fRequestReader.readLine();
			System.out.println("<<< " + response);
			return response;
		}
	}

	public String receive() throws IOException, DebugException {
		startListen();
		if (fEventReader != null) {
			String response = fEventReader.readLine();
			System.out.println("<-- " + response);
			return response;
		} else
			return "100 Continue";
	}

	public void terminate() {
		fRequestJob.cancel();
		fEventsJob.cancel();
		try {
			fRequestWriter.close();
			fRequestReader.close();
			fRemdebugSocket.close();
			fRemdebugServer.close();
			fEventReader.close();
			fEventServer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
