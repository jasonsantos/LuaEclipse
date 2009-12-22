/******************************************************************************
 * Copyright (c) 2009 KeplerProject, Sierra Wireless.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *      Kevin KIN-FOO <kkin-foo@sierrawireless.com>
 *          - initial API and implementation and initial documentation
 *****************************************************************************/


/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-06-15 17:30:48 +0200 (lun., 15 juin 2009) $
 * $Author: kkinfoo $
 * $Id: LuaLanguageToolkit.java 1840 2009-06-15 15:30:48Z kkinfoo $
 */
package org.keplerproject.luaeclipse.core;

import org.eclipse.dltk.core.AbstractLanguageToolkit;
import org.eclipse.dltk.core.IDLTKLanguageToolkit;

// TODO: Auto-generated Javadoc
/**
 * The Class LuaLanguageToolkit.
 */
public class LuaLanguageToolkit extends AbstractLanguageToolkit {
	
	/** The toolkit. */
	private static IDLTKLanguageToolkit toolkit = null;
	
	/**
	 * Gets the default.
	 * 
	 * @return the default
	 */
	public static IDLTKLanguageToolkit getDefault() {
		if ( toolkit == null ){
			toolkit = new LuaLanguageToolkit();
		}
		return toolkit;
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.core.IDLTKLanguageToolkit#getLanguageContentType()
	 */
	@Override
	public String getLanguageContentType() {
		return "com.anwrt.ldt.content-type";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.core.IDLTKLanguageToolkit#getLanguageName()
	 */
	@Override
	public String getLanguageName() {
		return "Lua";
	}

	/* (non-Javadoc)
	 * @see org.eclipse.dltk.core.IDLTKLanguageToolkit#getNatureId()
	 */
	@Override
	public String getNatureId() {
		return LuaNature.LUA_NATURE;
	}

}
