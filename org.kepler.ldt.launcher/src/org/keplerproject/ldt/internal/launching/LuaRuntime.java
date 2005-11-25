package org.keplerproject.ldt.internal.launching;

import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.runtime.IPath;
import org.kepler.ldt.laucher.LauncherPlugin;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

public class LuaRuntime {

	protected static LuaRuntime runtime;

	protected List installedInterpreters;

	protected LuaInterpreter selectedInterpreter;

	protected LuaRuntime() {
	}

	public static LuaRuntime getDefault() {
		if (runtime == null)
			runtime = new LuaRuntime();
		return runtime;
	}

	public LuaInterpreter getSelectedInterpreter() {
		if (selectedInterpreter == null)
			loadRuntimeConfiguration();
		return selectedInterpreter;
	}

	public LuaInterpreter getInterpreter(String name) {
		for (Iterator interpreters = getInstalledInterpreters().iterator(); interpreters
				.hasNext();) {
			LuaInterpreter each = (LuaInterpreter) interpreters.next();
			if (each.getName().equals(name))
				return each;
		}

		return getSelectedInterpreter();
	}

	public void setSelectedInterpreter(LuaInterpreter anInterpreter) {
		selectedInterpreter = anInterpreter;
		saveRuntimeConfiguration();
	}

	public void addInstalledInterpreter(LuaInterpreter anInterpreter) {
		getInstalledInterpreters().add(anInterpreter);
		if (getInstalledInterpreters().size() == 1)
			setSelectedInterpreter((LuaInterpreter) getInstalledInterpreters()
					.get(0));
		saveRuntimeConfiguration();
	}

	public List getInstalledInterpreters() {
		if (installedInterpreters == null)
			loadRuntimeConfiguration();
		return installedInterpreters;
	}

	public void setInstalledInterpreters(List newInstalledInterpreters) {
		installedInterpreters = newInstalledInterpreters;
		if (installedInterpreters.size() > 0)
			setSelectedInterpreter((LuaInterpreter) installedInterpreters
					.get(0));
		else
			setSelectedInterpreter(null);
	}

	protected void saveRuntimeConfiguration() {
		writeXML(getRuntimeConfigurationWriter());
	}

	protected Writer getRuntimeConfigurationWriter() {
		try {
			java.io.OutputStream stream = new BufferedOutputStream(
					new FileOutputStream(getRuntimeConfigurationFile()));
			return new OutputStreamWriter(stream);
		} catch (FileNotFoundException filenotfoundexception) {
			return null;
		}
	}

	protected void loadRuntimeConfiguration() {
		installedInterpreters = new ArrayList();
		try {
			XMLReader reader = SAXParserFactory.newInstance().newSAXParser()
					.getXMLReader();
			reader.setContentHandler(getRuntimeConfigurationContentHandler());
			Reader fileReader = getRuntimeConfigurationReader();
			if (fileReader == null)
				return;
			reader.parse(new InputSource(fileReader));
		} catch (Exception exception) {
		}
	}

	protected Reader getRuntimeConfigurationReader() {
		try {
			return new FileReader(getRuntimeConfigurationFile());
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	protected void writeXML(Writer writer) {
		try {
			writer
					.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><runtimeconfig>");
			for (Iterator interpretersIterator = installedInterpreters
					.iterator(); interpretersIterator.hasNext(); writer
					.write("/>")) {
				writer.write("<interpreter name=\"");
				LuaInterpreter entry = (LuaInterpreter) interpretersIterator
						.next();
				writer.write(entry.getName());
				writer.write("\" path=\"");
				writer.write(entry.getFileName().toString());
				writer.write("\"");
				if (entry.equals(selectedInterpreter))
					writer.write(" selected=\"true\"");
			}

			writer.write("</runtimeconfig>");
			writer.flush();
		} catch (IOException ioexception) {
		}
	}

	protected ContentHandler getRuntimeConfigurationContentHandler() {
		return new ContentHandler() {

			public void setDocumentLocator(Locator locator1) {
			}

			public void startDocument() throws SAXException {
			}

			public void endDocument() throws SAXException {
			}

			public void startPrefixMapping(String s, String s1)
					throws SAXException {
			}

			public void endPrefixMapping(String s) throws SAXException {
			}

			public void startElement(String namespaceURI, String localName,
					String qName, Attributes atts) throws SAXException {
				if ("interpreter".equals(qName)) {
					String interpreterName = atts.getValue("name");
					String interpreterFile = atts.getValue("path");
					LuaInterpreter interpreter = new LuaInterpreter(
							interpreterName, interpreterFile);
					installedInterpreters.add(interpreter);
					if (atts.getValue("selected") != null)
						selectedInterpreter = interpreter;
				}
			}

			public void endElement(String s, String s1, String s2)
					throws SAXException {
			}

			public void characters(char ac[], int i, int j) throws SAXException {
			}

			public void ignorableWhitespace(char ac[], int i, int j)
					throws SAXException {
			}

			public void processingInstruction(String s, String s1)
					throws SAXException {
			}

			public void skippedEntity(String s) throws SAXException {
			}

		};
	}

	protected File getRuntimeConfigurationFile() {
		IPath stateLocation = LauncherPlugin.getDefault().getStateLocation();
		IPath fileLocation = stateLocation.append("runtimeConfiguration.xml");
		return new File(fileLocation.toOSString());
	}

}