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


package org.keplerproject.luaeclipse.parser.internal.tests.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.ASTVisitor;

/**
 * Just compute nodes types and count.
 * 
 * @author kkinfoo
 * 
 */
public class SpyVisitor extends ASTVisitor {

    private String _error;
    private int _nodesCount = 0;
    private Map<String, Integer> _types;

    public void clear() {
	_nodesCount = 0;
	_types = new HashMap<String, Integer>();
    }

    private void countType(String typeName) {
	int count = _types.containsKey(typeName) ? _types.get(typeName) : 0;
	_types.put(typeName, new Integer(count+1));
    }

    /**
     * Provide error message in error case.
     * 
     * @return String Error message when available, empty string else way.
     */
    public String getErrorMessage() {
	return _error == null ? new String() : _error;
    }

    /**
     * Reports if visitor has meet the requested object type.
     * 
     * @param String
     *            nodeType Type of node sought.
     * @return boolean True if encountered, false else way.
     */
    public boolean hasVisitedType(String nodeType) {
	return _types.containsKey(nodeType);
    }

    /**
     * Count of visited nodes
     * 
     * @return int
     */
    public int nodesCount() {
	return _nodesCount;
    }

    public boolean visitGeneral(ASTNode node) throws Exception {
	try {
	    // Keep node's details in mind
	    _nodesCount++;
	    countType(node.getClass().getName());
	} catch (Exception e) {
	    _error = e.getMessage();
	}
	return true;
    }

    public Set<String> types() {
	return _types.keySet();
    }

    public int typeCount(String type) {
	return _types.get(type).intValue();
    }
}
