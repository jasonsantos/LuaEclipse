package org.keplerproject.ldt.luaprofiler.core;

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

public class LuaProfiler {
	public static class LuaProfilerInfo {
		private String name;
		private File file;

		public LuaProfilerInfo(String name, String fileName) {
			this.name = name;
			this.file = new File(fileName);
		}

		public String getName() {
			return name;
		}

		public File getFile() {
			return file;
		}
	};

	protected static LuaProfiler luaProfiler;
	protected List<LuaProfilerInfo> availableProfilers;
	protected LuaProfilerInfo selectedProfiler;

	protected LuaProfiler() {
	}

	public static LuaProfiler getDefault() {
		if (luaProfiler == null)
			luaProfiler = new LuaProfiler();
		return luaProfiler;
	}

	public void setAvailableProfilers(List<LuaProfilerInfo> profilers) {
		this.availableProfilers = profilers;
		saveProfilerConfiguration();
	}

	public void setSelectedProfiler(LuaProfilerInfo selectedProfiler) {
		this.selectedProfiler = selectedProfiler;
		saveProfilerConfiguration();
	}

	public LuaProfilerInfo getSelectedProfiler() {
		if (selectedProfiler == null)
			loadProfilerConfiguration();
		return this.selectedProfiler;
	}

	public List<LuaProfilerInfo> getAvailableProfilers() {
		if (availableProfilers == null)
			loadProfilerConfiguration();
		return this.availableProfilers;
	}

	protected void saveProfilerConfiguration() {
		writeXML(getProfilerConfigurationWriter());
	}

	protected Writer getProfilerConfigurationWriter() {
		try {
			java.io.OutputStream stream = new BufferedOutputStream(new FileOutputStream(getRuntimeConfigurationFile()));
			return new OutputStreamWriter(stream);
		} catch (FileNotFoundException filenotfoundexception) {
			return null;
		}
	}

	protected void loadProfilerConfiguration() {
		availableProfilers = new ArrayList<LuaProfilerInfo>();
		try {
			XMLReader reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
			reader.setContentHandler(getRuntimeConfigurationContentHandler());
			Reader fileReader = getProfilerConfigurationReader();
			if (fileReader == null)
				return;
			reader.parse(new InputSource(fileReader));
		} catch (Exception exception) {
		}
	}

	protected Reader getProfilerConfigurationReader() {
		try {
			return new FileReader(getRuntimeConfigurationFile());
		} catch (FileNotFoundException e) {
			return null;
		}
	}

	protected void writeXML(Writer writer) {
		try {
			writer.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?><profileconfig>");
			for (Iterator<LuaProfilerInfo> profilersIterator = availableProfilers.iterator(); profilersIterator.hasNext(); writer
					.write("/>")) {
				writer.write("<profiler name=\"");
				LuaProfilerInfo entry = profilersIterator.next();
				writer.write(entry.getName());
				writer.write("\" path=\"");
				writer.write(entry.getFile().getAbsolutePath());
				writer.write("\"");
				if (entry.equals(selectedProfiler))
					writer.write(" selected=\"true\"");
			}

			writer.write("</profileconfig>");
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

			public void startPrefixMapping(String s, String s1) throws SAXException {
			}

			public void endPrefixMapping(String s) throws SAXException {
			}

			public void startElement(String namespaceURI, String localName, String qName, Attributes atts) throws SAXException {
				if ("profiler".equals(qName)) {
					String profilerName = atts.getValue("name");
					String profilerFile = atts.getValue("path");
					availableProfilers.add(new LuaProfilerInfo(profilerName, profilerFile));
					if (atts.getValue("selected") != null)
						selectedProfiler = new LuaProfilerInfo(profilerName, profilerFile);
				}
			}

			public void endElement(String s, String s1, String s2) throws SAXException {
			}

			public void characters(char ac[], int i, int j) throws SAXException {
			}

			public void ignorableWhitespace(char ac[], int i, int j) throws SAXException {
			}

			public void processingInstruction(String s, String s1) throws SAXException {
			}

			public void skippedEntity(String s) throws SAXException {
			}

		};
	}

	protected File getRuntimeConfigurationFile() {
		IPath stateLocation = LauncherPlugin.getDefault().getStateLocation();
		IPath fileLocation = stateLocation.append("profilerConfiguration.xml");
		return new File(fileLocation.toOSString());
	}
}
