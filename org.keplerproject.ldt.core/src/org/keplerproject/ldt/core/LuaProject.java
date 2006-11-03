package org.keplerproject.ldt.core;

import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import javax.xml.parsers.SAXParserFactory;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IProject;
import org.eclipse.core.resources.IProjectNature;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.resources.ResourcesPlugin;
import org.eclipse.core.runtime.CoreException;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.Path;
import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.InputSource;
import org.xml.sax.Locator;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
/**
 * Lua project Model
 * @author guilherme
 *
 */
public class LuaProject
    implements IProjectNature, LuaElement
{
	   protected IProject project;
	    protected List loadPathEntries;
	    protected boolean scratched;

    public LuaProject()
    {
    }

    public void configure()
        throws CoreException
    {
    }

    public void deconfigure()
        throws CoreException
    {
    }

    public IProject getProject()
    {
        return project;
    }

    protected IProject getProject(String name)
    {
        return ResourcesPlugin.getWorkspace().getRoot().getProject(name);
    }

    public void setProject(IProject aProject)
    {
        project = aProject;
    }

    public void addLoadPathEntry(IProject anotherLuaProject)
    {
        scratched = true;
        LoadPathEntry newEntry = new LoadPathEntry(anotherLuaProject);
        getLoadPathEntries().add(newEntry);
    }

    public void removeLoadPathEntry(IProject anotherLuaProject)
    {
        for(Iterator entries = getLoadPathEntries().iterator(); entries.hasNext();)
        {
            LoadPathEntry entry = (LoadPathEntry)entries.next();
            if(entry.getType() == "project" && entry.getProject().getName().equals(anotherLuaProject.getName()))
            {
                getLoadPathEntries().remove(entry);
                scratched = true;
                break;
            }
        }

    }

    public List getLoadPathEntries()
    {
        if(loadPathEntries == null)
            loadLoadPathEntries();
        return loadPathEntries;
    }

    public List getReferencedProjects()
    {
        List referencedProjects = new ArrayList();
        for(Iterator iterator = getLoadPathEntries().iterator(); iterator.hasNext();)
        {
            LoadPathEntry pathEntry = (LoadPathEntry)iterator.next();
            if(pathEntry.getType() == "project")
                referencedProjects.add(pathEntry.getProject());
        }

        return referencedProjects;
    }

    protected void loadLoadPathEntries()
    {
        loadPathEntries = new ArrayList();
        IFile loadPathsFile = getLoadPathEntriesFile();
        XMLReader reader = null;
        try
        {
            reader = SAXParserFactory.newInstance().newSAXParser().getXMLReader();
            reader.setContentHandler(getLoadPathEntriesContentHandler());
            reader.parse(new InputSource(loadPathsFile.getContents()));
        }
        catch(Exception exception) { }
    }

    protected ContentHandler getLoadPathEntriesContentHandler()
    {
        return new ContentHandler() {

            public void characters(char ac[], int i, int j)
                throws SAXException
            {
            }

            public void endDocument()
                throws SAXException
            {
            }

            public void endElement(String s, String s1, String s2)
                throws SAXException
            {
            }

            public void endPrefixMapping(String s)
                throws SAXException
            {
            }

            public void ignorableWhitespace(char ac[], int i, int j)
                throws SAXException
            {
            }

            public void processingInstruction(String s, String s1)
                throws SAXException
            {
            }

            public void setDocumentLocator(Locator locator)
            {
            }

            public void skippedEntity(String s)
                throws SAXException
            {
            }

            public void startDocument()
                throws SAXException
            {
            }

            public void startElement(String namespaceURI, String localName, String qName, Attributes atts)
                throws SAXException
            {
                if("pathentry".equals(qName) && "project".equals(atts.getValue("type")))
                {
                    IPath referencedProjectPath = new Path(atts.getValue("path"));
                    IProject referencedProject = getProject(referencedProjectPath.lastSegment());
                    loadPathEntries.add(new LoadPathEntry(referencedProject));
                }
            }

            public void startPrefixMapping(String s, String s1)
                throws SAXException
            {
            }

        };
    }

    protected IFile getLoadPathEntriesFile()
    {
        return project.getFile(".loadpath");
    }

    public void save()
        throws CoreException
    {
        if(scratched)
        {
            java.io.InputStream xmlPath = new ByteArrayInputStream(getLoadPathXML().getBytes());
            IFile loadPathsFile = getLoadPathEntriesFile();
            if(!loadPathsFile.exists())
                loadPathsFile.create(xmlPath, true, null);
            else
                loadPathsFile.setContents(xmlPath, true, false, null);
            scratched = false;
        }
    }

    protected String getLoadPathXML()
    {
        StringBuffer buffer = new StringBuffer();
        buffer.append("<?xml version=\"1.0\" encoding=\"UTF-8\"?><loadpath>");
        LoadPathEntry entry;
        for(Iterator pathEntriesIterator = loadPathEntries.iterator(); pathEntriesIterator.hasNext(); buffer.append(entry.toXML()))
            entry = (LoadPathEntry)pathEntriesIterator.next();

        buffer.append("</loadpath>");
        return buffer.toString();
    }

    public IResource getUnderlyingResource()
    {
        return project;
    }

 
}