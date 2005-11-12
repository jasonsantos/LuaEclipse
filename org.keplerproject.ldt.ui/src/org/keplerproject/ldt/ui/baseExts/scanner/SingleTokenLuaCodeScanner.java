/*******************************************************************************
 * Copyright (c) 2003 Ideais Tecnologia LTDA.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Common Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/cpl-v10.html
 *******************************************************************************/

package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.rules.IPredicateRule;
import org.keplerproject.ldt.ui.editors.LuaBaseScanner;


public class SingleTokenLuaCodeScanner extends LuaBaseScanner {
   
    public SingleTokenLuaCodeScanner(IPredicateRule rule) {
        super();
        addRules(new IPredicateRule[]{rule});
    }

}
