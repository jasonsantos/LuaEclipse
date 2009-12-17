/**
 * @author	Kevin KIN-FOO <kkinfoo@anyware-tech.com>
 * @date $Date: 2009-07-29 17:56:04 +0200 (mer., 29 juil. 2009) $
 * $Author: kkinfoo $
 * $Id: NodeFactory.java 2190 2009-07-29 15:56:04Z kkinfoo $
 */
package com.anwrt.ldt.internal.parser;

import java.net.URL;
import java.util.List;

import org.eclipse.dltk.ast.ASTNode;
import org.eclipse.dltk.ast.declarations.Declaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.ast.expressions.CallArgumentsList;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.statements.Statement;

import com.anwrt.ldt.parser.LuaExpressionConstants;
import com.anwrt.ldt.parser.ast.expressions.BinaryExpression;
import com.anwrt.ldt.parser.ast.expressions.Boolean;
import com.anwrt.ldt.parser.ast.expressions.Call;
import com.anwrt.ldt.parser.ast.expressions.Dots;
import com.anwrt.ldt.parser.ast.expressions.Function;
import com.anwrt.ldt.parser.ast.expressions.Identifier;
import com.anwrt.ldt.parser.ast.expressions.Index;
import com.anwrt.ldt.parser.ast.expressions.Nil;
import com.anwrt.ldt.parser.ast.expressions.Number;
import com.anwrt.ldt.parser.ast.expressions.Pair;
import com.anwrt.ldt.parser.ast.expressions.Parenthesis;
import com.anwrt.ldt.parser.ast.expressions.String;
import com.anwrt.ldt.parser.ast.expressions.Table;
import com.anwrt.ldt.parser.ast.expressions.UnaryExpression;
import com.anwrt.ldt.parser.ast.statements.Break;
import com.anwrt.ldt.parser.ast.statements.Chunk;
import com.anwrt.ldt.parser.ast.statements.ElseIf;
import com.anwrt.ldt.parser.ast.statements.ForInPair;
import com.anwrt.ldt.parser.ast.statements.ForNumeric;
import com.anwrt.ldt.parser.ast.statements.If;
import com.anwrt.ldt.parser.ast.statements.Local;
import com.anwrt.ldt.parser.ast.statements.LocalRec;
import com.anwrt.ldt.parser.ast.statements.LuaStatementConstants;
import com.anwrt.ldt.parser.ast.statements.Repeat;
import com.anwrt.ldt.parser.ast.statements.Return;
import com.anwrt.ldt.parser.ast.statements.Set;
import com.anwrt.ldt.parser.ast.statements.While;

/**
 * A factory for creating Node objects.
 * 
 * From Lua source this class is capable of producing a DLTK AST using the
 * Metalua library.
 */
public class NodeFactory implements LuaExpressionConstants,
		LuaStatementConstants {

	/** Root of all the nodes produced by this instance of {@link NodeFactory}. */
	private ModuleDeclaration root;

	/** The helper. */
	private NodeFactoryHelper helper;

	/**
	 * Initialize factory with current Lua context, assumes that an AST named
	 * "ast" already exits in Lua context.
	 * 
	 * @param NodeFactoryHelper
	 *            help Tool making communication with Lua a lot easier
	 * @param int sourceLength the source's length
	 */
	protected NodeFactory(NodeFactoryHelper help, int sourceLength) {
		this.helper = help;
		this.root = new ModuleDeclaration(sourceLength);
	}

	/**
	 * Instantiates a new node factory.
	 * 
	 * @param NodeFactoryHelper
	 *            helper Tool making communication with Lua a lot easier
	 */
	protected NodeFactory(NodeFactoryHelper helper) {
		this(helper, 0);
	}

	/**
	 * Instantiates a new node factory.
	 * 
	 * @param source
	 *            the source
	 */
	public NodeFactory(final java.lang.String source) {
		this(new NodeFactoryHelper(source), source.length());
	}

	/**
	 * Just indicates if there is any syntax error in parsed code
	 * 
	 * @return True is code contains any syntax error, false else way
	 */
	public boolean errorDetected() {
		return helper.hasSyntaxErrors();
	}

	/**
	 * Instantiates a new node factory.
	 * 
	 * @param sourceFile
	 *            the source file
	 */
	public NodeFactory(final URL sourceFile) {
		this(new NodeFactoryHelper(sourceFile));
	}

	/**
	 * Gets the node.
	 * 
	 * @param long id ID of the node in Lua indexed AST
	 * 
	 * @return DLTK compliant node from Lua AST node for ID
	 */
	public ASTNode getNode(final long id) {

		// Used for binaries expressions
		Statement left, right;
		Chunk chunk, altChunk;
		Expression expression, altExpression;

		// Child node IDs will help for recursive node instantiation
		List<Long> childNodes = helper.children(id);

		// Define position in code
		int childCount = childNodes.size();
		int end = helper.getEndPosition(id);
		int start = helper.getStartPosition(id);

		// Check if gap of 2 characters in Lua string is allowed
		assert (start - 1) <= (end + 1) : "Wrong code offsets for node: " + id
				+ ". Begins at " + start + ", ends at " + end;

		/*
		 * Fetch root type
		 */
		ASTNode node = null;
		int kindOfNode = helper.typeOfNode(id);
		switch (kindOfNode) {
		/*
		 * Numbers
		 */
		case LuaExpressionConstants.NUMBER_LITERAL:
			int value = Integer.valueOf(helper.getValue(id));
			node = new Number(start, end, value);
			break;
		/*
		 * Strings
		 */
		case LuaExpressionConstants.STRING_LITERAL:
			node = new String(start, end, helper.getValue(id));
			break;
		/*
		 * Tables
		 */
		case LuaExpressionConstants.E_TABLE:
			// Define Table
			node = new Table(start, end);

			// Fill with values
			for (Long child : childNodes) {
				((Table) node).addStatement((Statement) getNode(child));
			}
			break;
		/*
		 * Pairs
		 */
		case LuaExpressionConstants.E_PAIR:
			left = (Expression) getNode(childNodes.get(0));
			right = (Expression) getNode(childNodes.get(1));
			node = new Pair(start, end, (Expression) left, (Expression) right);
			break;
		/*
		 * Logical Values {Nil, True, False}
		 */
		case LuaExpressionConstants.NIL_LITTERAL:
			node = new Nil(start, end);
			break;
		case LuaExpressionConstants.BOOL_TRUE:
			node = new Boolean(start, end, true);
			break;
		case LuaExpressionConstants.BOOL_FALSE:
			node = new Boolean(start, end, false);
			break;
		/*
		 * Unary Operations
		 */
		case LuaExpressionConstants.E_LENGTH:
		case LuaExpressionConstants.E_UN_MINUS:
		case LuaExpressionConstants.E_BNOT:
			expression = (Expression) getNode(childNodes.get(0));
			node = new UnaryExpression(start, end, kindOfNode, expression);
			break;
		/*
		 * Binary Operations
		 */
		case LuaExpressionConstants.E_BIN_OP:
			assert childCount > 1 : "Too many expressions "
					+ "in binary operation: " + childCount;
			// Determine king of expression
			int kind = NodeFactoryHelper.opid(helper.getValue(id));

			// Compute both sides of '='
			left = (Expression) getNode(childNodes.get(0));
			right = (Expression) getNode(childNodes.get(1));
			node = new BinaryExpression(start, end, (Expression) left, kind,
					(Expression) right);
			break;
		/*
		 * Assignment
		 */
		case LuaExpressionConstants.E_ASSIGN:
			// Deal with assignment
			assert childCount == 2 : "Invalid number of parameters "
					+ "for a 'Set' instruction :" + childCount;
			chunk = (Chunk) getNode(childNodes.get(0));
			altChunk = (Chunk) getNode(childNodes.get(1));
			/*
			 * In case of function assigned to variables, use variables names as
			 * function name. Mainly useful for having valid value on outline. A
			 * factory is needed to sort this before object instantiation.
			 */
			node = Set.factory(start, end, chunk, altChunk);
			break;
		/*
		 * Identifiers
		 */
		case LuaExpressionConstants.E_IDENTIFIER:
			// TODO: Deal with multiple expressions
			assert childCount == 0 : "Id has child nodes: " + childCount;
			node = new Identifier(start, end, helper.getValue(id));
			break;
		/*
		 * "Do" and Chunks statements
		 */
		case LuaStatementConstants.S_BLOCK:
			node = new Chunk(start, end);
			chunk = (Chunk) node;
			// Inflate block
			for (Long childID : childNodes) {
				chunk.addStatement(getNode(childID));
			}
			break;
		/*
		 * Functions
		 */
		case Declaration.D_METHOD:
			assert childCount == 2 : "Wrong child nodes count for a function: "
					+ childCount;
			chunk = (Chunk) getNode(childNodes.get(0));
			altChunk = (Chunk) getNode(childNodes.get(1));
			node = new Function(start, end, chunk, altChunk);
			break;
		/*
		 * Return
		 */
		case LuaStatementConstants.S_RETURN:
			// Define return statement and values
			node = new Return(start, end);
			for (long returnIndex : childNodes) {
				expression = (Expression) getNode(returnIndex);
				((Return) node).addExpression(expression);
			}
			break;
		/*
		 * Break
		 */
		case LuaStatementConstants.S_BREAK:
			node = new Break(start, end);
			break;
		/*
		 * Parenthesis
		 */
		case LuaExpressionConstants.E_PAREN:
			assert childCount == 1 : "Too many expressions between parenthesis.";
			expression = (Expression) getNode(childNodes.get(0));
			node = new Parenthesis(start, end, expression);
			break;
		/*
		 * While
		 */
		case S_WHILE:
			assert childCount == 2 : "Wrong parameters count to build while statement: "
					+ childCount;
			expression = (Expression) getNode(childNodes.get(0));
			chunk = (Chunk) getNode(childNodes.get(1));
			node = new While(start, end, expression, chunk);
			break;
		/*
		 * Repeat
		 */
		case S_UNTIL:
			assert childCount == 2 : "Wrong parameters count to build repeat statement: "
					+ childCount;
			chunk = (Chunk) getNode(childNodes.get(0));
			expression = (Expression) getNode(childNodes.get(1));
			node = new Repeat(start, end, chunk, expression);
			break;
		/*
		 * Local variable declaration
		 */
		case ASTNode.D_VAR_DECL:
			assert childCount == 2 : "Wrong count of parameters "
					+ "for local declaration: " + childCount;

			// Handle assignment at declaration
			chunk = (Chunk) getNode(childNodes.get(0));
			if (helper.nodeHasLineInfo(childNodes.get(1))) {
				altChunk = (Chunk) getNode(childNodes.get(1));
				node = new Local(start, end, chunk, altChunk);
			} else {
				node = new Local(start, end, chunk);
			}
			break;
		/*
		 * If statement
		 */
		case S_IF:
			/*
			 * We're dealing with a mutant statement. A regular `If has 3 child
			 * nodes. Besides, it could have one more option for the "else"
			 * part. Furthermore, "elseif" nodes could indefinitely follow an if
			 * statement.
			 */
			assert childCount < 3 : "Not enough clauses for if statement: "
					+ childCount;

			// Extract if components
			expression = (Expression) getNode(childNodes.get(0));
			chunk = (Chunk) getNode(childNodes.get(1));

			/*
			 * Deal with the multiple "elseif" case
			 */
			if (childCount > 2) {

				// `If node that can handle "elseif"
				node = new ElseIf(start, end, expression, chunk);

				/*
				 * Elseif nodes goes by pair: Expression then Chunk. That's why
				 * we'll use a range of 2.
				 */
				for (int pair = 2; pair < childCount - 1; pair += 2) {

					// Cast Expression then Chunk
					expression = (Expression) getNode(childNodes.get(pair));
					chunk = (Chunk) getNode(childNodes.get(pair + 1));

					// Append ElseIf nodes' expression and chunk
					((ElseIf) (node)).addExpressionAndRelatedChunk(expression,
							chunk);
				}

				// Append else chunk
				if ((childCount % 2) == 1) {
					altChunk = (Chunk) getNode(childNodes.get(childCount - 1));
					((ElseIf) (node)).setAlternative(altChunk);
				}

			} else {
				// Regular `If case
				node = new If(start, end, expression, chunk);
			}
			break;
		/*
		 * For loop
		 */
		case S_FOR:
			assert childCount > 3 : "Not enough parameter to built numeric for: "
					+ childCount;
			// Extract common informations
			Identifier variable = (Identifier) getNode(childNodes.get(0));
			expression = (Expression) getNode(childNodes.get(1));
			altExpression = (Expression) getNode(childNodes.get(2));
			chunk = (Chunk) getNode(childNodes.get(childCount - 1));

			// Deal with optional expression
			if (childCount > 4) {
				Expression optionnal = (Expression) getNode(childNodes.get(3));
				node = new ForNumeric(start, end, variable, expression,
						altExpression, optionnal, chunk);
			} else {
				// Regular numeric for
				node = new ForNumeric(start, end, variable, expression,
						altExpression, chunk);
			}
			break;
		case S_FOREACH:
			assert childCount > 2 : "Not enough parameter to built for each: "
					+ childCount;
			chunk = (Chunk) getNode(childNodes.get(0));
			altChunk = (Chunk) getNode(childNodes.get(1));
			Chunk lastChunk = (Chunk) getNode(childNodes.get(2));
			node = new ForInPair(start, end, chunk, altChunk, lastChunk);
			break;
		/*
		 * Call to function
		 */
		case E_CALL:
			// Allocate function with its name
			assert childCount > 0 : "No name given for function call.";
			altExpression = (Expression) getNode(childNodes.get(0));

			// Append parameters for call
			if (childCount > 1) {
				CallArgumentsList args = new CallArgumentsList();
				for (int parameter = 1; parameter < childCount; parameter++) {
					expression = (Expression) getNode(childNodes.get(parameter));
					args.addNode(expression);

					// Define parameter list position in code
					if (parameter == 1) {
						args.setStart(expression.matchStart());
					} else if (parameter == (childCount - 1)) {
						args.setEnd(expression.matchStart()
								+ expression.matchLength());
					}
				}

				node = new Call(start, end, altExpression, args);
			} else {
				node = new Call(start, end, altExpression);
			}
			break;
		/*
		 * Index
		 */
		case E_INDEX:
			// Indexed array and value of index
			assert childCount == 2 : "Wrong parameter count for index: "
					+ childCount;
			expression = (Expression) getNode(childNodes.get(0));
			altExpression = (Expression) getNode(childNodes.get(1));
			node = new Index(start, end, expression, altExpression);
			break;
		/*
		 * Local recursion
		 */
		case D_FUNC_DEC:
			assert childCount == 2 : "Too many parameters for local declaration "
					+ "of recursive function: " + childCount;

			// Handle assignment at declaration
			chunk = (Chunk) getNode(childNodes.get(0));
			if (helper.nodeHasLineInfo(childNodes.get(1))) {
				altChunk = (Chunk) getNode(childNodes.get(1));
				node = new LocalRec(start, end, chunk, altChunk);
			} else {
				// Average declaration
				node = new LocalRec(start, end, chunk);
			}
			break;
		/*
		 * Dots
		 */
		case E_DOTS:
			node = new Dots(start, end);
			break;
		/*
		 * Invoke
		 */
//		case E_INVOKE:
//			assert childCount > 1 : "No name defined for invocation.";
//			expression = (Expression) getNode(childNodes.get(0));
//			altExpression = (String) getNode(childNodes.get(1));
//			if (childCount > 2) {
//				CallArgumentsList args = new CallArgumentsList();
//				for (int parameter = 2; parameter < childCount; parameter++) {
//					Expression expr = (Expression) getNode(childNodes.get(parameter));
//					args.addNode(expr);
//
//					// Define parameter list position in code
//					if (parameter == 2) {
//						args.setStart(expr.matchStart());
//					} else if (parameter == (childCount - 1)) {
//						args.setEnd(expr.matchStart()
//								+ expr.matchLength());
//					}
//				}
//				node = new Invoke(start, end, expression, altExpression, args);
//			} else {
//				node = new Invoke(start, end, expression, altExpression);
//			}
//			break; 
		}

		return node;
	}

	/**
	 * Gets the root of DLTK AST
	 * 
	 * @see ModuleDeclaration
	 * @return ModuleDeclaration root of of any DLTK compliant AST
	 */
	public ModuleDeclaration getRoot() {
		root.addStatement((Statement) getNode(1));
		return root;
	}
}
