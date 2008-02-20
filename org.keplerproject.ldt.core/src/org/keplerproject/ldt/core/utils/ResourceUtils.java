package org.keplerproject.ldt.core.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;

import org.eclipse.core.runtime.Platform;
import org.osgi.framework.Bundle;

public class ResourceUtils {

	public static URL findfile(String name, String pluginName) {
		Bundle b = Platform.getBundle(pluginName);
		
		return  b.getResource(name);
	}
	
	public static URL findmodule(String name, String pluginName) {
		Bundle b = Platform.getBundle(pluginName);
		name = name.replace('.', '/');
		String[] moduleLocations = { name ,
				"lua/5.1/" + name + ".lua",
				"lua/5.1/" + name + "/init.lua",
				"lua/5.1/" + name + ".lp"};
	
		URL u = null;
	
		for (String moduleLocation : moduleLocations) {
			if ((u = b.getResource(moduleLocation)) != null)
				break;
		}
		
		return u;
	}

	/**
	 * Loads the contents of an URL
	 * 
	 * @param u
	 *            URL to the file
	 * @return String with the file contetn
	 * @throws IOException
	 */
	public static String getFileContents(URL u) throws IOException {
		BufferedReader is = new BufferedReader(new InputStreamReader(u
				.openStream()));
		String s = null;
	
		StringBuilder moduleBody = new StringBuilder();
		while ((s = is.readLine()) != null) {
			moduleBody.append(s + "\n");
		}
	
		return moduleBody.toString();
	}

}
