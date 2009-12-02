/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-16 15:58:05 +0200 (jeu., 16 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: LuaSourceElementParser.java 2086 2009-07-16 13:58:05Z kkinfoo $
 */
package com.anwrt.ldt.parser;

import org.eclipse.dltk.compiler.SourceElementRequestVisitor;
import org.eclipse.dltk.core.AbstractSourceElementParser;

import com.anwrt.ldt.core.LuaNature;

// TODO: Auto-generated Javadoc
/**
 * The Class LuaSourceElementParser.
 */
public class LuaSourceElementParser extends AbstractSourceElementParser {

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.core.AbstractSourceElementParser#createVisitor()
	 */
	public SourceElementRequestVisitor createVisitor() {
		return new SourceElementRequestVisitor(getRequestor());
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see org.eclipse.dltk.core.AbstractSourceElementParser#getNatureId()
	 */
	@Override
	protected String getNatureId() {
		return LuaNature.LUA_NATURE;
	}
}
