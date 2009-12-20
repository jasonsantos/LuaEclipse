/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-23 12:07:30 +0200 (jeu., 23 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: TestStatements.java 2161 2009-07-23 10:07:30Z kkinfoo $
 */
package com.anwrt.ldt.parser.internal.tests;

import java.util.Random;

import junit.framework.TestCase;

import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.compiler.problem.IProblemReporter;

import com.anwrt.ldt.parser.LuaSourceParser;
import com.anwrt.ldt.parser.internal.tests.utils.DummyReporter;

/**
 * The Class TestStatements aims to check full coverage of Lua's key words. In
 * order to do so it checks every kind of statements of the language in order to
 * ensure the parser handle them.
 */
public class TestStatements extends TestCase {

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
	 * Test break.
	 */
	public void testBreak() {

		char[] source = "do break end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Break statement is not recognized.", module.isEmpty());
	}

	/**
	 * Test chunk.
	 */
	public void testChunk() {

		char[] source = "".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Empty chunk is not recognized.", module.isEmpty());

		source = "do end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Explicit chunk is not recognized.", module.isEmpty());
	}

	/**
	 * Test for.
	 */
	public void testFor() {

		char[] source = "for k=1,1 do end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("For statement is not recognized.", module.isEmpty());
	}

	/**
	 * Test for each.
	 */
	public void testForEach() {

		char[] source = "for k,v in pairs({}) do end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("For statement is not recognized.", module.isEmpty());
	}

	/**
	 * Test if.
	 */
	public void testIf() {

		char[] source = "if true then end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("If statement is not recognized.", module.isEmpty());
	}

	/**
	 * Test if else.
	 */
	public void testIfElse() {

		char[] source = "if false then else end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("If statement is not recognized.", module.isEmpty());
	}

	/**
	 * Test elseif.
	 */
	public void testIfElseIf() {

		char[] source = "i = 0 if i == 0 then return i elseif i > 1 then return i-1 end"
				.toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("`elseif statement is not recognized.", module.isEmpty());
	}

	/**
	 * Test several elseif.
	 */
	public void testSeveralIfElseIf() {

		/*
		 * Generate chain of else if of variable length
		 */
		Random gen = new Random(196540427);
		int elseIfCount = gen.nextInt() % 20 + 1;
		String elseIfChain = "";
		for (int k = 0; k < elseIfCount; k++) {
			elseIfChain += "elseif i > 1 then return i-1 ";
		}
		char[] source = ("i = 0 if i == 0 then return i " + elseIfChain + "end")
				.toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse(elseIfCount + " `elseifIf in a row are not handled.",
				module.isEmpty());
	}

	/**
	 * Test local.
	 */
	public void testLocal() {

		char[] source = "local var".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Local declaration is not recognized.", module.isEmpty());

		source = "local var = 1".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Inititialisation of local declaration not handled.",
				module.isEmpty());
	}

	/**
	 * Test local recursion.
	 */
	public void testLocalRecursion() {

		char[] source = "local function f(var) return f(var+1) end"
				.toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Local recursion declaration is not handled.", module
				.isEmpty());
	}

	/**
	 * Test repeat.
	 */
	public void testRepeat() {

		char[] source = "repeat break until( true )".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Repeat statement is not recognized.", module.isEmpty());
	}

	/**
	 * Test return.
	 */
	public void testReturn() {

		char[] source = "function unicity() return 1 end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Single return is not recognized.", module.isEmpty());

		source = "function foo() return 1, 2 end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Multiple return is not recognized.", module.isEmpty());
	}

	/**
	 * Test set.
	 */
	public void testSet() {

		// Single assignment
		char[] source = "set = 'up'".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Assignment is not recognized.", module.isEmpty());

		// Multiple assignment
		source = "set, stand = 'up', 'up'".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Assignment is not recognized.", module.isEmpty());
	}

	/**
	 * Test while.
	 */
	public void testWhile() {

		char[] source = "while( true ) do break end".toCharArray();
		module = new LuaSourceParser().parse(fileName, source, this.reporter);
		assertFalse("Whiel statement is not recognized.", module.isEmpty());
	}

}
