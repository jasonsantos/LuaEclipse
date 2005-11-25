
package org.keplerproject.ldt.internal.launching.ui.preferences;

import org.eclipse.jface.viewers.ILabelProviderListener;
import org.eclipse.jface.viewers.ITableLabelProvider;
import org.eclipse.swt.graphics.Image;
import org.keplerproject.ldt.internal.launching.LuaInterpreter;

public class LuaInterpreterLabelProvider
    implements ITableLabelProvider
{

    public LuaInterpreterLabelProvider()
    {
    }

    public Image getColumnImage(Object element, int columnIndex)
    {
        return null;
    }

    public String getColumnText(Object element, int columnIndex)
    {
        LuaInterpreter interpreter = (LuaInterpreter)element;
        switch(columnIndex)
        {
        case 0: // '\0'
            return interpreter.getName();

        case 1: // '\001'
            String installLocation = interpreter.getFileName();
            return installLocation == null ? "In user path" : installLocation;
        }
        return "Unknown Column Index";
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