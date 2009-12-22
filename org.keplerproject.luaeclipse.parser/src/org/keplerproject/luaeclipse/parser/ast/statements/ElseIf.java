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


package org.keplerproject.luaeclipse.parser.ast.statements;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.Expression;

public class ElseIf extends If {

	private List<Expression> expressions = new ArrayList<Expression>();
	private List<Chunk> chunks = new ArrayList<Chunk>();

	public ElseIf(int start, int end, Expression condition, Chunk nominal,
			Chunk alternative) {
		super(start, end, condition, nominal, alternative);
	}

	public ElseIf(int start, int end, Expression condition, Chunk nominal) {
		super(start, end, condition, nominal);
	}

	public void addExpressionAndRelatedChunk(Expression expression, Chunk chunk) {
		expressions.add(expression);
		chunks.add(chunk);
	}
	public void traverse(ASTVisitor visitor) throws Exception {
		if (visitor.visit(this)) {
			super.traverse(visitor);
			for( int pairs = 0, count = expressions.size(); pairs <count; pairs++){
				(expressions.get(pairs)).traverse(visitor);
				(chunks.get(pairs)).traverse(visitor);
			}
			visitor.endvisit(this);
		}
	}
}
