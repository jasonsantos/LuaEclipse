package org.keplerproject.ldt.core;

import java.util.HashSet;
import java.util.Set;
import java.util.StringTokenizer;

import org.eclipse.core.resources.IFile;
import org.eclipse.core.resources.IResource;
import org.eclipse.core.runtime.IPath;
import org.eclipse.core.runtime.preferences.AbstractPreferenceInitializer;
import org.eclipse.jface.preference.IPreferenceStore;

public class LuaScriptsSpecs extends AbstractPreferenceInitializer {
	public static final String PREF_DEFAULT_SCOPE = "org.keplerproject.ldt.core";

	private static final String PREF_STORE_DELIM = ", ";

	public static final String PREF_LUASCRIPT_NAMES = PREF_DEFAULT_SCOPE + ".luascripts.pattern";

	IPreferenceStore preferenceStore;

	static LuaScriptsSpecs specs;
	
	private Set<String> luaScriptPatterns;

	public LuaScriptsSpecs() {
	}
	
	public static LuaScriptsSpecs getDefault() {
		if(specs==null)
			specs = new LuaScriptsSpecs();
		return specs;
	}
	
	public void setPreferenceStore(IPreferenceStore ip) {
		preferenceStore = ip;
		setDefaultPatterns();
	}
	
	private void savePatterns() {
		StringBuilder store = new StringBuilder();
		
		for (String string : getLuaScriptPatterns()) {
			store.append(string);
			store.append(PREF_STORE_DELIM);
		}
		preferenceStore.setValue(PREF_LUASCRIPT_NAMES, store.toString());
	}
	
	private Set<String> loadPatterns() {
		Set<String> fileNames = new HashSet<String>();
		String read = preferenceStore.getString(PREF_LUASCRIPT_NAMES);
		
		if (read != null) {
			StringTokenizer st = new StringTokenizer(read, PREF_STORE_DELIM);
			while (st.hasMoreTokens()) {
				fileNames.add(st.nextToken());
			}
		}
		return fileNames;
	}
	
	public void clearLuaScriptExtensions() {
		getLuaScriptPatterns().clear();
	}
	
	public void addLuaScriptPattern(String pattern) {
		getLuaScriptPatterns().add(pattern);
		savePatterns();
	}
	
	public boolean isValidLuaScriptFileName(IResource resource) {
		if(luaScriptPatterns!=null) {
			if(resource instanceof IFile && 
					isIncluded(resource.getProjectRelativePath(), resource, luaScriptPatterns))
				return true;
		}
		return false;
	}

	public void setDefaultPatterns() {
		clearLuaScriptExtensions();
		addLuaScriptPattern("*.lua");
		savePatterns();
	}
	
	public Set<String> getLuaScriptPatterns() {
		if(luaScriptPatterns == null)
			luaScriptPatterns = loadPatterns(); 
		return luaScriptPatterns;
	}
	
	private boolean isIncluded(IPath path, IResource resource, Set<String> includedPatterns) {
		boolean included = false;
		// NOTE: n^2 time complexity, but should not be a bottleneck
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
		setDefaultPatterns();
	}

}
