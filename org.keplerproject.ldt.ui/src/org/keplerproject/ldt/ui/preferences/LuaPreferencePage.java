package org.keplerproject.ldt.ui.preferences;

import org.eclipse.jface.preference.FieldEditorPreferencePage;
import org.eclipse.ui.IWorkbench;
import org.eclipse.ui.IWorkbenchPreferencePage;
import org.keplerproject.ldt.ui.LDTUIPlugin;
/**
 * Lua Preference page Extension. The Blank root page.
 * @author guilherme
 *
 */
public class LuaPreferencePage extends FieldEditorPreferencePage
    implements IWorkbenchPreferencePage
{

    public LuaPreferencePage()
    {
        super(1);
        setPreferenceStore(LDTUIPlugin.getDefault().getPreferenceStore());
        setDescription(" Lua General Properties");
        initializeDefaults();
    }

    private void initializeDefaults()
    {
       // org.eclipse.jface.preference.IPreferenceStore store = getPreferenceStore();
    }

    public void init(IWorkbench iworkbench)
    {
    }

    protected void createFieldEditors()
    {
    }
}