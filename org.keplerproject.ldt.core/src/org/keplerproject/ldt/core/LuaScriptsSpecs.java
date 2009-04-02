package org.keplerproject.ldt.core;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.logging.ErrorManager;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.dialogs.ErrorDialog;
import org.eclipse.jface.preference.IPreferenceStore;
import org.eclipse.ui.PlatformUI;
import org.keplerproject.ldt.core.compiler.LuaAlert;
import org.keplerproject.luajava.LuaState;
import org.keplerproject.luajava.LuaStateFactory;


public class LuaScriptsSpecs extends AbstractPreferenceInitializer {
	public static final String PREF_DEFAULT_SCOPE = "org.keplerproject.ldt.core";

	private static final String PREF_STORE_DELIM = ", ";

	public static final String PREF_LUASCRIPT_NAMES = PREF_DEFAULT_SCOPE + ".luascripts.pattern";
	public static final String PREF_LUADOC_AUTOGENERATION = PREF_DEFAULT_SCOPE + ".luadoc.autogeneration";
	public static final String PREF_LUASTATEINITSCRIPT = PREF_DEFAULT_SCOPE + ".lua.initscript";

	private LuaState L = null;
	private boolean luaStateIsDirty = true;
	
	IPreferenceStore preferenceStore;

	static LuaScriptsSpecs specs;
	
	private Set<String> luaScriptPatterns;
	private boolean luaDocAutoGeneration = true;

	private String initialScript;

	public LuaScriptsSpecs() {
//		setPreferenceStore(PlatformUI.getPreferenceStore());
	}
	
	public static LuaScriptsSpecs getDefault() {
		if(specs==null)
			specs = new LuaScriptsSpecs();
		return specs;
	}
	
	public void setPreferenceStore(IPreferenceStore ip) {
		if(preferenceStore == null)
			preferenceStore = ip;
		
		luaScriptPatterns = loadPatterns();
		if (luaScriptPatterns.size() == 0) 
			setDefaultSpecs();
	}
	
	public void savePatterns() {
		StringBuilder store = new StringBuilder();
		
		for (String string : getLuaScriptPatterns()) {
			store.append(string);
			
			store.append(PREF_STORE_DELIM);
		}
		preferenceStore.setValue(PREF_LUASCRIPT_NAMES, store.toString());
		
		preferenceStore.setValue(PREF_LUADOC_AUTOGENERATION, new Boolean(luaDocAutoGeneration).toString());

		preferenceStore.setValue(PREF_LUASTATEINITSCRIPT, getInitialScript());
	}
	
	//TODO: check if this signature is really right
	private Set<String> loadPatterns() {
		Set<String> fileNames = new HashSet<String>();
		
		if(preferenceStore != null) {
		
			String read = preferenceStore.getString(PREF_LUASCRIPT_NAMES);
			
			if (read != null) {
				StringTokenizer st = new StringTokenizer(read, PREF_STORE_DELIM);
				while (st.hasMoreTokens()) {
					fileNames.add(st.nextToken());
				}
			}
			
			read = preferenceStore.getString(PREF_LUADOC_AUTOGENERATION);
			
			if (read != null)
				luaDocAutoGeneration = Boolean.parseBoolean(read);

			read = preferenceStore.getString(PREF_LUASTATEINITSCRIPT);
			
			if (read != null)
				initialScript = read;
		
		}
		
		return fileNames;
	}
	
	public void clearLuaScriptExtensions() {
		getLuaScriptPatterns().clear();
	}
	
	public void addLuaScriptPattern(String pattern) {
		getLuaScriptPatterns().add(pattern);

	}

	public boolean isLuaDocAutoGenerationActive() {
		return luaDocAutoGeneration;
	}
	
	public void setLuaDocAutoGeneration(boolean active) {
		luaDocAutoGeneration = active;

	}
	
	public boolean isValidLuaScriptFileName(IResource resource) {
		if(resource instanceof IFile && 
				isIncluded(resource.getProjectRelativePath(), resource, getLuaScriptPatterns()))
			return true;
		return false;
	}

	public void setDefaultSpecs() {
		clearLuaScriptExtensions();
		addLuaScriptPattern("*.lua");
		
		setLuaDocAutoGeneration(true);
		
		setInitialScript("");
	}
	
	public Set<String> getLuaScriptPatterns() {
		if(luaScriptPatterns == null)
			luaScriptPatterns = loadPatterns(); 
		return luaScriptPatterns;
	}
	
	private boolean isIncluded(IPath path, IResource resource, Set<String> includedPatterns) {
		boolean included = false;
		// NOTE: n^2 time complexity, but should not be a bottleneck
		// TODO: figure out a way to increase speed in this match - I believe reducing the scope will help 
		for (String pattern : includedPatterns) {
			if (resource != null && pattern.startsWith("file:/")) {
				included |= isUriIncluded(resource.getLocationURI().toString(), pattern);
			} else {
				for (String segment : path.segments()) {
					included |= segment.matches(pattern.replaceAll("\\.", "\\\\.").replaceAll("\\*", ".*"));
				}
			}

			if (included) {
				break;
			}
		}
		return included;
	}

	private boolean isUriIncluded(String uri, String pattern) {
		if (uri != null && uri.startsWith(pattern)) {
			return true;
		} else {
			return false;
		}
	}

	@Override
	public void initializeDefaultPreferences() {
		setDefaultSpecs();
	}

	public String getInitialScript() {

		return initialScript;
	}

	public void setInitialScript(String initialScript) {
		luaStateIsDirty = true;
		this.initialScript = initialScript;
	}

	public LuaState getLuaState() {
		if (L == null || luaStateIsDirty) { 
			try {
				L = LuaStateFactory.newLuaState();
				L.openLibs();
				
				int result = L.LdoString(getInitialScript());
				if (result != 0) {
					String s = L.toString(-1);
					System.out.println(s);
				}	
				luaStateIsDirty = false;
			}  catch (Exception e) {
				System.out.println("Could not initialize LuaState:"
						+ e.getMessage());
			} catch (Error e) {
				System.out.println("Could not initialize LuaState:"
						+ e.getMessage());
			}
		}
		
		return L;
	}

}
