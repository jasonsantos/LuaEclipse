/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: TestExpressions.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package com.anwrt.ldt.parser.internal.tests;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

import com.anwrt.ldt.parser.LuaSourceParser;
import com.anwrt.ldt.parser.internal.tests.utils.DummyReporter;

/**
 * The Class TestExpressions, tests if {@linkplain LuaSourceParser} can handle
 * every kind of {@linkplain Expression} that Lua offers.
 */
public class TestExpressions extends TestCase {

	/** The reporter. */
	private IProblemReporter reporter;

	/** The file name. */
	private char[] fileName;

	/** The module. */
	private ModuleDeclaration module;

	/*
	 * (non-Javadoc)
	 * 
	 * @see junit.framework.TestCase#setUp()
	 */
	public void setUp() {
		// No tests on about file name
		fileName = "none".toCharArray();

		// Dummy problem reporter
		this.reporter = new DummyReporter();
	}

	/**
	 * Test boolean false.
	 */
	public void testBooleanFalse() {

		char[] source = "bool = false".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("False is not recognized.", module.isEmpty());
	}

	/**
	 * Test boolean true.
	 */
	public void testBooleanTrue() {

		char[] source = "bool = true".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("True is not recognized.", module.isEmpty());
	}

	/**
	 * Test call.
	 */
	public void testCall() {

		char[] source = "method = function () end method()".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Call to function is not recognized.", module.isEmpty());

		source = "withParam = function (foo, bar) end withParam(nil, nil)"
				.toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Call to function with parameters is not recognized.",
				module.isEmpty());
	}

	/**
	 * Test dots.
	 */
	public void testDots() {

		char[] source = "method = function (...) end method()".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Dots are not recognized.", module.isEmpty());
	}

	/**
	 * Test function.
	 */
	public void testFunction() {

		char[] source = "method = function (var) return var +1 end"
				.toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Function is not recognized.", module.isEmpty());
	}

	/**
	 * Test index.
	 */
	public void testIndex() {

		char[] source = "tab = {} tab[2]= 2".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Numeric index is not handled.", module.isEmpty());

		source = "module = {} module.field= 2".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Field-like index is not handled.", module.isEmpty());
	}

	/**
	 * Test pair.
	 */
	public void testPair() {
		char[] source = "dic = {[1] = 'one', two = 2}".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Pair is not recognized.", module.isEmpty());
	}

	/**
	 * Test nil.
	 */
	public void testNil() {

		char[] source = "null = nil".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Nil is not recognized.", module.isEmpty());
	}

	/**
	 * Test number.
	 */
	public void testNumber() {

		char[] source = "number = 6".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Number is not recognized.", module.isEmpty());
	}

	/**
	 * Test parenthesis.
	 */
	public void testParenthesis() {
		char[] source = "paren = (1 + 2) * 5".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Parenthesis is not recognized.", module.isEmpty());
	}

	/**
	 * Test string.
	 */
	public void testString() {

		char[] source = ("string = 'string' " + "another =\"anotherOne\"")
				.toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("String is not recognized.", module.isEmpty());
	}

	/**
	 * Test table.
	 */
	public void testTable() {

		char[] source = "table = {1,'2'}".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Table is not recognized.", module.isEmpty());
	}

}
