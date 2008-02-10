/*
 * Copyright (C) 2003-2007 Kepler Project.
 *
 * Permission is hereby granted, free of charge, to any person obtaining
 * a copy of this software and associated documentation files (the
 * "Software"), to deal in the Software without restriction, including
 * without limitation the rights to use, copy, modify, merge, publish,
 * distribute, sublicense, and/or sell copies of the Software, and to
 * permit persons to whom the Software is furnished to do so, subject to
 * the following conditions:
 *
 * The above copyright notice and this permission notice shall be
 * included in all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */
package org.keplerproject.ldt.core;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InvalidClassException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ISynchronizer;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.eclipse.core.runtime.QualifiedName;
import org.keplerproject.ldt.core.luadoc.LuadocGenerator;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;

/**
 * Lua project Model
 * 
 * @author guilherme
 * @author jasonsantos
 * @version $Id$
 */
public class LuaProject implements IProjectNature, LuaElement {
	private static QualifiedName qualifiedName;
	private static final ISynchronizer synchronizer = ResourcesPlugin
			.getWorkspace().getSynchronizer();
	protected IProject project;
	protected List<LoadPathEntry> loadPathEntries;
	protected boolean scratched;

	protected Map<String, Map<String, ILuaEntry>> luaEntries;

	public LuaProject() {

	}

	public void configure() throws CoreException {
	}

	public void deconfigure() throws CoreException {
	}

	public IProject getProject() {
		return project;
	}

	protected IProject getProject(String name) {
		return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
	}

	public void setProject(IProject aProject) {
		project = aProject;
		qualifiedName = new QualifiedName("org.keplerproject.ldt.core", project
				.getName()
				+ ".luadoc.projectdata");
		try {
			synchronizer.add(qualifiedName);
			{
				byte[] syncInfo = synchronizer.getSyncInfo(qualifiedName,
						project);
				if (syncInfo != null) {
					
					ObjectInputStream ois = new ObjectInputStream(
							new ByteArrayInputStream(syncInfo));
					try {
						HashMap<String, Map<String, ILuaEntry>> savedObject = (HashMap<String, Map<String, ILuaEntry>>) ois
								.readObject();
						luaEntries = savedObject;
					} catch(InvalidClassException e) {
						
					}
				}
			}

			if (luaEntries == null)
				luaEntries = new HashMap<String, Map<String, ILuaEntry>>();

			if (luaEntries.size() > 0) {
				LuadocGenerator lg = LuadocGenerator.getInstance();

				for (Map<String, ILuaEntry> e : luaEntries.values()) {
					lg.generateIndexes(e);
				}
			}

		} catch (IOException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (CoreException e) {
			System.out.println(e.getMessage());
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
		}

	}

	public void saveLuaDocEntries() {
		try {
			ByteArrayOutputStream baos = new ByteArrayOutputStream();
			ObjectOutputStream oos = new ObjectOutputStream(baos);

			oos.writeObject(luaEntries);

			synchronizer
					.setSyncInfo(qualifiedName, project, baos.toByteArray());

		} catch (CoreException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void addLoadPathEntry(IProject anotherLuaProject) {
		scratched = true;
		getLoadPathEntries().add(new LoadPathEntry(anotherLuaProject));
	}

	public void removeLoadPathEntry(IProject anotherLuaProject) {
		for (Iterator<LoadPathEntry> entries = getLoadPathEntries().iterator(); entries
				.hasNext();) {
			LoadPathEntry entry = entries.next();
			if (entry.getType() == "project"
					&& entry.getProject().getName().equals(
							anotherLuaProject.getName())) {
				getLoadPathEntries().remove(entry);
				scratched = true;
				break;
			}
		}

	}

	public List<LoadPathEntry> getLoadPathEntries() {
		if (loadPathEntries == null)
			loadLoadPathEntries();
		return loadPathEntries;
	}

	public List<IProject> getReferencedProjects() {
		List<IProject> referencedProjects = new ArrayList<IProject>();
		for (Iterator<LoadPathEntry> iterator = getLoadPathEntries().iterator(); iterator
				.hasNext();) {
			LoadPathEntry pathEntry = iterator.next();

			if (pathEntry.getType() == "project") {
				referencedProjects.add(pathEntry.getProject());
			}
		}

		return referencedProjects;
	}

	protected void loadLoadPathEntries() {
		loadPathEntries = new ArrayList<LoadPathEntry>();
		IFile loadPathsFile = getLoadPathEntriesFile();
		XMLReader reader = null;
		try {
			reader = SAXParserFactory.newInstance().newSAXParser()
					.getXMLReader();
			reader.setContentHandler(getLoadPathEntriesContentHandler());
			reader.parse(new InputSource(loadPathsFile.getContents()));
		} catch (Exception exception) {
		}
	}

	protected ContentHandler getLoadPathEntriesContentHandler() {
		return new ContentHandler() {

			public void characters(char ac[], int i, int j) throws SAXException {
			}

			public void endDocument() throws SAXException {
			}

			public void endElement(String s, String s1, String s2)
					throws SAXException {
			}

			public void endPrefixMapping(String s) throws SAXException {
			}

			public void ignorableWhitespace(char ac[], int i, int j)
					throws SAXException {
			}

			public void processingInstruction(String s, String s1)
					throws SAXException {
			}

			public void setDocumentLocator(Locator locator) {
			}

			public void skippedEntity(String s) throws SAXException {
			}

			public void startDocument() throws SAXException {
			}

			public void startElement(String namespaceURI, String localName,
					String qName, Attributes atts) throws SAXException {
				if ("pathentry".equals(qName)
						&& "project".equals(atts.getValue("type"))) {
					IPath referencedProjectPath = new Path(atts
							.getValue("path"));
					IProject referencedProject = getProject(referencedProjectPath
							.lastSegment());
					loadPathEntries.add(new LoadPathEntry(referencedProject));
				}
			}

			public void startPrefixMapping(String s, String s1)
					throws SAXException {
			}

		};
	}

	protected IFile getLoadPathEntriesFile() {
		return project.getFile(".loadpath");
	}

	public void save() throws CoreException {
		if (scratched) {
			java.io.InputStream xmlPath = new ByteArrayInputStream(
					getLoadPathXML().getBytes());
			IFile loadPathsFile = getLoadPathEntriesFile();
			if (!loadPathsFile.exists())
				loadPathsFile.create(xmlPath, true, null);
			else
				loadPathsFile.setContents(xmlPath, true, false, null);
			scratched = false;
		}
	}

	protected String getLoadPathXML() {
		StringBuffer buffer = new StringBuffer();
		buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><loadpath>");
		LoadPathEntry entry;
		for (Iterator<LoadPathEntry> pathEntriesIterator = loadPathEntries
				.iterator(); pathEntriesIterator.hasNext(); buffer.append(entry
				.toXML()))
			entry = pathEntriesIterator.next();

		buffer.append("</loadpath>");
		return buffer.toString();
	}

	public IResource getUnderlyingResource() {
		return project;
	}

	/**
	 * Returns all entries collected from a lua resource.
	 * 
	 * @param luaFileFullPath
	 *            the full pathname of the resource
	 * @return a collection of all entries found within that resource
	 */
	public Map<String, ILuaEntry> getLuaEntries(String luaFileFullPath) {
		Map<String, ILuaEntry> m = luaEntries.get(luaFileFullPath);
		if (m == null) {
			m = new HashMap<String, ILuaEntry>();
			luaEntries.put(luaFileFullPath, m);
		}
		return m;
	}

}