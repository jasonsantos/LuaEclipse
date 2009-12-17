/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: TestLuaBinaryOperations.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package com.anwrt.ldt.parser.internal.tests;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

import com.anwrt.ldt.parser.LuaSourceParser;
import com.anwrt.ldt.parser.internal.tests.utils.DummyReporter;

/**
 * The Class TestLuaBinaryOperations. Aims to test implementation of Lua binary
 * operations in the parser.
 */
public class TestLuaBinaryOperations extends TestCase {

	/** The reporter. */
	private IProblemReporter reporter;

	/** The file name. */
	private char[] fileName;

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
	 * Test addition.
	 */
	public void testAddition() {
		char[] source = "add = 1 + 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Addition is not supported.", module.isEmpty());
	}

	/**
	 * Test and.
	 */
	public void testAnd() {

		char[] source = "_and = True and False".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Logical and is not supported.", module.isEmpty());
	}

	/**
	 * Test concatenation.
	 */
	public void testConcatenation() {

		char[] source = "concat = 'string' .. \"another\"".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Concatenation is not supported.", module.isEmpty());
	}

	/**
	 * Test division.
	 */
	public void testDivision() {

		char[] source = "div = 1 / 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Division is not supported.", module.isEmpty());
	}

	/**
	 * Test equality.
	 */
	public void testEquality() {

		char[] source = "eq = 1 == 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Equality is not supported.", module.isEmpty());
	}

	/**
	 * Test modulo.
	 */
	public void testModulo() {

		char[] source = "mod = 1 % 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Modulo is not supported.", module.isEmpty());
	}

	/**
	 * Test multiplication.
	 */
	public void testMultiplication() {

		char[] source = "mul = 1 * 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Multiplication is not supported.", module.isEmpty());
	}

	/**
	 * Test lighter.
	 */
	public void testLighter() {

		char[] source = "lt = 1 < 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Lighter than is not supported.", module.isEmpty());
	}

	/**
	 * Test lighter or equal.
	 */
	public void testLighterOrEqual() {

		char[] source = "le = 1 <= 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Lighter than or equal is not supported.", module.isEmpty());
	}

	/**
	 * Test or.
	 */
	public void testOr() {

		char[] source = "_or = True or False".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Logical or is not supported.", module.isEmpty());
	}

	/**
	 * Test power.
	 */
	public void testPower() {

		char[] source = "pow = 1 + 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Power raise is not supported.", module.isEmpty());
	}

	/**
	 * Test subtraction.
	 */
	public void testSubtraction() {
		char[] source = "sub = 1 - 2".toCharArray();
		ModuleDeclaration module = new LuaSourceParser().parse(fileName,
				source, this.reporter);
		assertFalse("Subtraction is not supported.", module.isEmpty());
	}

}
