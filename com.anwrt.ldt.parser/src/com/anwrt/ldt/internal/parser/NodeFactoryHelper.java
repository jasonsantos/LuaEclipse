/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: NodeFactoryHelper.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package com.anwrt.ldt.internal.parser;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.eclipse.core.runtime.FileLocator;
import org.eclipse.core.runtime.Platform;
import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.keplerproject.luajava.LuaState;

import com.anwrt.ldt.parser.Activator;
import com.anwrt.ldt.parser.LuaExpressionConstants;
import com.anwrt.ldt.parser.ast.statements.LuaStatementConstants;
import com.anwrt.metalua.Metalua;

/**
 * All Lua tools for parsing Lua code are available from here.
 */
public class NodeFactoryHelper implements LuaExpressionConstants,
		LuaStatementConstants {

	/** The state. */
	private LuaState state;

	/** The comparator. */
	private Comparator<Long> comparator;

	/** The source. */
	private String source;

	private boolean _syntaxErrors;

	/**
	 * Instantiates a new node factory helper.
	 */
	private NodeFactoryHelper() {
		try {
			/*
			 *  Define path to source file
			 */
			
			// Make sure that file is available on disk
			URL url = Platform.getBundle(Activator.PLUGIN_ID).getEntry(
					"/scripts/ast_to_table.mlua");
			
			// Retrieve absolute URI of file
			String path = new File(FileLocator.toFileURL(url).getFile()).getPath();
			
			// Run file
			state = Metalua.get();
			state.LdoFile(path);
		} catch (IOException e) {
			e.printStackTrace();
		}
		// Implement comparator in order to be able to sort child node IDs
		this.comparator = new Comparator<Long>() {
			public int compare(Long l1, Long l2) {
				return l1.compareTo(l2);
			}

			@Override
			@SuppressWarnings("unchecked")
			public boolean equals(Object obj) {
				if (obj instanceof Comparator<?>) {
					Comparator<Long> comparator = (Comparator<Long>) obj;
					return this.compare(Long.valueOf(2), Long.valueOf(3)) == comparator
							.compare(Long.valueOf(2), Long.valueOf(3));
				}
				return false;
			}
		};
	}

	/**
	 * Instantiates a new node factory helper.
	 * 
	 * @param source
	 *            the source
	 */
	public NodeFactoryHelper(final String source) {
		/*
		 * The aim here is to perform the following statement in Lua: ast =
		 * mlc.luastring_to_ast('" + source + "')
		 */

		// Bear source in mind
		this();
		this.source = source;

		// Parsing function
		String parseFunction = "luastring_to_ast";

		// Lua utils
		assert state.getTop() == 0 : "Stack is unbalanced before AST generation.";

		// Retrieve function
		state.getGlobal("mlc");
		state.getField(-1, parseFunction);
		state.remove(-2);
		assert state.isFunction(-1) : "Unable to load parsing function: "
				+ parseFunction;

		// Provide parameter
		state.pushString(this.source);

		// Build Lua AST
		switch (state.pcall(1, 1, 0)) {
		case 0:
			assert state.getTop() == 1 && state.isTable(-1) : "Lua AST generation failed.";

			// So far, no syntax errors
			_syntaxErrors = false;

			// Store result in global variable 'ast' in Lua side
			state.setGlobal("ast");
			assert state.getTop() == 0 : "Lua stack is unbalanced after AST generation";
			break;
		default:
			// TODO: Store error
			// Retrieve error from Lua stack
			String error = state.toString(-1);
			_syntaxErrors = true;
			state.setTop(0);
			/*
			 * AST computation result is stored in the 'ast' global variable, in
			 * order to avoid any kind of trouble, let's define an empty AST as
			 * we got an error
			 */
			state.LdoString("ast = {}");
			System.err.println("Bind error: o/ " + error + " \\o");
			break;
		}
		// Index AST made from sources
		index();
	}

	/**
	 * Instantiates a new node factory helper.
	 * 
	 * @param sourceFile
	 *            the source file
	 */
	public NodeFactoryHelper(final URL sourceFile) {
		this();
		state.LdoString("ast = mlc.luafile_to_ast('" + sourceFile.getPath()
				+ "')");
		index();
	}

	/**
	 * Children.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the list< long>
	 */
	public List<Long> children(final long id) {

		// Work on empty stack
		assert state.getTop() == 0;
		List<Long> child = new ArrayList<Long>();

		// Fetch Lua function
		state.getGlobal("children");

		// Provide parameter
		state.pushNumber((double) id);

		/*
		 * Effective call
		 */
		assert state.isNumber(-1) : "Number parameter should be in stack.";
		assert state.isFunction(-2) : "Attemp to call a non Lua function.";
		state.call(1, 2);

		// Check results
		assert state.isNumber(-1);
		assert state.isTable(-2);

		// Retrieve children count
		long count = (long) state.toNumber(-1);
		state.pop(1);

		// Store children
		assert state.isTable(-1) : "Can't access children IDs table.";
		for (long k = 0; k < count; k++) {
			// Provide requested index of result table
			state.pushNumber((double) k + 1);

			// Store table value at this index
			assert state.getTop() == 2 : "Stack alea";
			state.getTable(-2);
			Long nodeID = (long) state.toNumber(-1);
			child.add(nodeID);
			state.pop(1);
		}
		// Flush stack
		state.setTop(0);

		/*
		 * Sort list
		 */
		Collections.sort(child, this.comparator);
		return child;
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof NodeFactoryHelper)) {
			return false;
		}
		NodeFactoryHelper node = (NodeFactoryHelper) o;
		return getSource().equals(node.getSource())
				&& getComparator().equals(node.getComparator())
				&& getState().equals(node.getState());
	}

	protected Comparator<Long> getComparator() {
		return comparator;
	}

	public int getEndPosition(final long id) {
		int position;
		assert state.getTop() == 0 : "Lua stack should be empty";
		state.getGlobal("getEnd");
		state.pushNumber((double) id);
		assert state.isNumber(-1) : "Unable to load ID of node.";
		assert state.isFunction(-2) : "Unable to load function to compute end"
				+ " position in source.";
		switch (state.pcall(1, 1, 0)) {
		case 0:
			assert state.isNumber(-1) : "Wrong type for end position in code";
			position = (int) state.toNumber(-1);
			state.setTop(0);
			assert state.getTop() == 0 : "Lua stack should be empty";
			return position;
		}
		state.setTop(0);
		return 0;
	}

	public int getStartPosition(final long id) {
		int position;
		assert state.getTop() == 0 : "Lua stack should be empty";
		state.getGlobal("getStart");
		state.pushNumber((double) id);
		if (state.pcall(1, 1, 0) == 0) {
			assert state.isNumber(-1) : "Wrong type for start position in code";
			position = (int) state.toNumber(-1);
			state.setTop(0);
			assert state.getTop() == 0 : "Lua stack should be empty";
			return position;
		}
		state.setTop(0);
		return 0;
	}

	protected String getSource() {
		return source;
	}

	protected LuaState getState() {
		return state;
	}

	/**
	 * Gets the value.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the value
	 */
	public String getValue(final long id) {
		String value;
		assert state.getTop() == 0 : "Lua stack should be empty";

		// Retrieve Lua procedure
		state.getGlobal("getValue");

		// Provide node ID, push parameter in stack
		state.pushNumber((double) id);

		// Call Lua function with 1 parameter and 1 result
		state.call(1, 1);
		assert state.getTop() == 1 && state.isString(-1) : "A problem occured during value retrieval";

		// Bear value in mind
		value = state.toString(-1);

		// Flush stack
		state.setTop(0);
		return value;
	}

	public boolean nodeHasLineInfo(final long id) {
		boolean hasLineInfo;
		assert state.getTop() == 0 : "Lua stack should be empty.";
		state.getGlobal("hasLineInfo");
		state.pushNumber((double) id);
		state.call(1, 1);
		assert state.isBoolean(-1) : "Boolean sould be on top of stack";
		hasLineInfo = state.toBoolean(-1);
		state.setTop(0);
		return hasLineInfo;
	}

	/**
	 * Index.
	 */
	private void index() {
		/*
		 * Create index in AST
		 */

		// Stack should be empty
		assert state.getTop() == 0 : "Lua stack should be empty before "
				+ "indexation, stack size: " + state.getTop();

		// Retrieve procedure index
		state.getField(LuaState.LUA_GLOBALSINDEX, "index");

		// Retrieve current AST
		state.getField(LuaState.LUA_GLOBALSINDEX, "ast");

		// Index AST
		state.call(1, 1);

		// Remove procedure and parameter from Lua stack
		state.pop(1);

		// Lua stack should be empty again
		assert state.getTop() == 0 : "Lua stack should be empty at this point, "
				+ "instead stack size is " + state.getTop();
	}

	/**
	 * Operation identifier.
	 * 
	 * @param s
	 *            the s
	 * 
	 * @return Numeric value for type
	 * 
	 * @see
	 */
	public static int opid(final String s) {

		if ("sub".equals(s)) {
			return LuaExpressionConstants.E_MINUS;
		} else if ("mul".equals(s)) {
			return LuaExpressionConstants.E_MULT;
		} else if ("div".equals(s)) {
			return LuaExpressionConstants.E_DIV;
		} else if ("eq".equals(s)) {
			return LuaExpressionConstants.E_EQUAL;
		} else if ("concat".equals(s)) {
			return LuaExpressionConstants.E_CONCAT;
		} else if ("mod".equals(s)) {
			return LuaExpressionConstants.E_MOD;
		} else if ("pow".equals(s)) {
			return LuaExpressionConstants.E_POWER;
		} else if ("lt".equals(s)) {
			return LuaExpressionConstants.E_LT;
		} else if ("le".equals(s)) {
			return LuaExpressionConstants.E_LE;
		} else if ("and".equals(s)) {
			return LuaExpressionConstants.E_LAND;
		} else if ("or".equals(s)) {
			return LuaExpressionConstants.E_LOR;
		} else if ("not".equals(s)) {
			return LuaExpressionConstants.E_BNOT;
		} else if ("len".equals(s)) {
			return LuaExpressionConstants.E_LENGTH;
		} else if ("unm".equals(s)) {
			return LuaExpressionConstants.E_UN_MINUS;
		} else {
			// Assume it's an addition
			assert "add".equals(s) : "Unhandled operator: " + s;
			return LuaExpressionConstants.E_PLUS;
		}
	}

	/**
	 * Node name.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the string
	 */
	public String nodeName(final long id) {
		String name = null;

		// Stack should be empty
		assert state.getTop() == 0 : NodeFactoryHelper.class.toString()
				+ ": Lua stack should be empty";

		// Retrieve Lua function
		state.getField(LuaState.LUA_GLOBALSINDEX, "getTag");

		// Pass given ID as parameter
		state.pushNumber((double) id);

		// Call function
		state.call(1, 1);

		/*
		 * Convert type name in numeric value
		 */
		if (state.isString(-1)) {
			name = state.toString(-1);
		} else {
			assert state.isNil(-1);
		}

		// Flush stack
		state.setTop(0);
		return name;
	}

	public boolean hasSyntaxErrors() {
		return _syntaxErrors;
	}

	/**
	 * Statement or expression.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the int
	 */
	public int statementOrExpression(final long id) {
		/*
		 * Determine type of operator
		 */
		String typeName = nodeName(id);
		if ("Op".equals(typeName)) {
			/*
			 * Check if it's an unary operation
			 */
			String value = getValue(id);
			if ("unm".equals(value)) {
				return LuaExpressionConstants.E_UN_MINUS;
			} else if ("not".equals(value)) {
				return LuaExpressionConstants.E_BNOT;
			} else if ("len".equals(value)) {
				return LuaExpressionConstants.E_BNOT;
			}

			// So, it should be a binary operation
			return LuaExpressionConstants.E_BIN_OP;

		} else if ("Set".equals(typeName)) {
			return LuaExpressionConstants.E_ASSIGN;
		} else if ("Do".equals(typeName)) {
			return LuaStatementConstants.S_BLOCK;
		} else if ("Id".equals(typeName)) {
			return LuaExpressionConstants.E_IDENTIFIER;
		} else if ("Number".equals(typeName)) {
			return LuaExpressionConstants.NUMBER_LITERAL;
		} else if ("Nil".equals(typeName)) {
			return LuaExpressionConstants.NIL_LITTERAL;
		} else if ("True".equals(typeName)) {
			return LuaExpressionConstants.BOOL_TRUE;
		} else if ("False".equals(typeName)) {
			return LuaExpressionConstants.BOOL_FALSE;
		} else if ("Table".equals(typeName)) {
			return LuaExpressionConstants.E_TABLE;
		} else if ("Paren".equals(typeName)) {
			return LuaExpressionConstants.E_PAREN;
		} else if ("Pair".equals(typeName)) {
			return LuaExpressionConstants.E_PAIR;
		} else if ("String".equals(typeName)) {
			return LuaExpressionConstants.STRING_LITERAL;
		} else if ("Function".equals(typeName)) {
			return Declaration.D_METHOD;
		} else if ("Return".equals(typeName)) {
			return LuaStatementConstants.S_RETURN;
		} else if ("Break".equals(typeName)) {
			return LuaStatementConstants.S_BREAK;
		} else if ("While".equals(typeName)) {
			return LuaStatementConstants.S_WHILE;
		} else if ("Repeat".equals(typeName)) {
			return LuaStatementConstants.S_UNTIL;
		} else if ("Local".equals(typeName)) {
			return ASTNode.D_VAR_DECL;
		} else if ("Fornum".equals(typeName)) {
			return LuaStatementConstants.S_FOR;
		} else if ("Forin".equals(typeName)) {
			return LuaStatementConstants.S_FOREACH;
		} else if ("If".equals(typeName)) {
			return LuaStatementConstants.S_IF;
		} else if ("Call".equals(typeName)) {
			return LuaExpressionConstants.E_CALL;
		} else if ("Index".equals(typeName)) {
			return LuaExpressionConstants.E_INDEX;
		} else if ("Localrec".equals(typeName)) {
			return LuaStatementConstants.D_FUNC_DEC;
		} else if ("Dots".equals(typeName)) {
			return LuaExpressionConstants.E_DOTS;
		}

		// Typical blocks do not have tags
		return LuaStatementConstants.S_BLOCK;
	}

	/**
	 * Type of node.
	 * 
	 * @param id
	 *            the id
	 * 
	 * @return the int
	 */
	public int typeOfNode(final long id) {
		return statementOrExpression(id);
	}
}
