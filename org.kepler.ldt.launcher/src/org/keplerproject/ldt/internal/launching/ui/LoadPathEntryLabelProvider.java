package org.keplerproject.ldt.internal.launching.ui;

import org.eclipse.jface.viewers.ILabelProvider;
import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.swt.graphics.Image;
import org.keplerproject.ldt.core.LoadPathEntry;

public class LoadPathEntryLabelProvider
    implements ILabelProvider
{

    public LoadPathEntryLabelProvider()
    {
    }

    public Image getImage(Object element)
    {
        return null;
    }

    public String getText(Object element)
    {
        if(element != null && element.getClass() == LoadPathEntry.class)
            return ((LoadPathEntry)element).getProject().getLocation().toOSString();
        else
            return null;
    }

    public void addListener(ILabelProviderListener ilabelproviderlistener)
    {
    }

    public void dispose()
    {
    }

    public boolean isLabelProperty(Object element, String property)
    {
        return false;
    }

    public void removeListener(ILabelProviderListener ilabelproviderlistener)
    {
    }
}