/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: TestVisitor.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package com.anwrt.ldt.parser.internal.tests;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

import com.anwrt.ldt.parser.LuaSourceParser;
import com.anwrt.ldt.parser.internal.tests.utils.DummyReporter;
import com.anwrt.ldt.parser.internal.tests.utils.SpyVisitor;

/**
 * The Class TestVisitor checks how AST is can be walked through.
 */
public class TestVisitor extends TestCase {
	
	/** The visitor. */
	private ASTVisitor visitor;
	
	/** The reporter. */
	private IProblemReporter reporter;

	/* (non-Javadoc)
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		this.visitor = new SpyVisitor();
		this.reporter = new DummyReporter();
	}

	/**
	 * Test visitor.
	 */
	public void testVisitor() {
		String errorMessage = new String();
		boolean success = true;
		LuaSourceParser parser = new LuaSourceParser();
		char[] fileName = "none".toCharArray();
		char[] source = "for k = 1,20 do end".toCharArray();
		try {
			ModuleDeclaration parse = parser.parse(fileName, source, reporter);
			parse.traverse(visitor);
		} catch (Exception e) {
			success = false;
			errorMessage = e.getMessage();
		}
		assertTrue("Error while walking through AST: " + errorMessage, success);
	}
}
