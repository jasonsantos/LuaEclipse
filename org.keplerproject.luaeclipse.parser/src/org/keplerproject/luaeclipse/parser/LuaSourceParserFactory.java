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


package org.keplerproject.luaeclipse.parser;

import org.eclipse.dltk.ast.parser.ISourceParser;
import org.eclipse.dltk.ast.parser.ISourceParserFactory;

/**
 * Provides source parser
 * 
 * @author Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 */
public class LuaSourceParserFactory implements ISourceParserFactory {

    public LuaSourceParserFactory() {
    }

    @Override
    public ISourceParser createSourceParser() {
	return new LuaSourceParser();
    }

}
