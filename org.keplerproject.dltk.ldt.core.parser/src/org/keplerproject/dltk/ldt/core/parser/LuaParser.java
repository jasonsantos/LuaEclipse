package org.keplerproject.dltk.ldt.core.parser;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.expressions.BooleanLiteral;
import org.eclipse.dltk.ast.expressions.Expression;
import org.eclipse.dltk.ast.expressions.FloatNumericLiteral;
import org.eclipse.dltk.ast.expressions.NilLiteral;
import org.eclipse.dltk.ast.expressions.StringLiteral;
import org.eclipse.dltk.ast.statements.Block;
import org.eclipse.dltk.ast.statements.Statement;
import org.keplerproject.dltk.ldt.core.parser.LuaLexer.LuaToken;
import org.keplerproject.dltk.ldt.core.parser.ast.LuaModuleDeclaration;
import org.keplerproject.dltk.ldt.core.parser.ast.declarations.LuaArgument;
import org.keplerproject.dltk.ldt.core.parser.ast.declarations.LuaFunctionDeclaration;
import org.keplerproject.dltk.ldt.core.parser.ast.expressions.AssignmentExpression;
import org.keplerproject.dltk.ldt.core.parser.ast.expressions.BinaryExpression;
import org.keplerproject.dltk.ldt.core.parser.ast.expressions.Closure;
import org.keplerproject.dltk.ldt.core.parser.ast.references.LuaVariableReference;
import org.keplerproject.dltk.ldt.core.parser.ast.statements.BreakStatement;
import org.keplerproject.dltk.ldt.core.parser.ast.statements.IfStatement;
import org.keplerproject.dltk.ldt.core.parser.ast.statements.RepeatStatement;
import org.keplerproject.dltk.ldt.core.parser.ast.statements.ReturnStatement;
import org.keplerproject.dltk.ldt.core.parser.ast.statements.WhileStatement;

public class LuaParser {
	private static final Logger LOGGER = Logger.getLogger(LuaParser.class.getName());
	private LuaLexer lexer;
	private LuaModuleDeclaration moduleDeclaration;

	public LuaParser(final LuaLexer lexer, final boolean rebuild) {
		this.lexer = lexer;
		this.moduleDeclaration = new LuaModuleDeclaration(lexer.getLength(),
				rebuild);
	}

	private LuaToken name() {
		final LuaToken name = lexer.consume(LuaLexer.Token._NAME);
		return name;
	}

	private List<LuaToken> namelist() {
		final List<LuaToken> names = new ArrayList<LuaToken>();
		do {
			names.add(name());
		} while (lexer.advance(LuaLexer.Token._COMMA));
		return names;
	}

	private List<LuaToken> paramlist() {
		LuaToken t = lexer.getToken();
		switch (t.getToken()) {
		case _COMMA:
			break;
		case _DOTS:
			break;
		case _NAME:
			return namelist();
		}
		return null;
	}

	private void funcbody(LuaFunctionDeclaration f) {
		lexer.consume(LuaLexer.Token._LPAREN); // Consome o (

		if (lexer.match(LuaLexer.Token._NAME)) {
			List<LuaToken> params = paramlist();
			for (LuaToken token : params) {
				LuaArgument l = new LuaArgument();
				l.setName(token.getLexeme());
				l.setNameStart(token.getPosition());
				l.setNameEnd(token.getPosition() + token.getLexeme().length());
				l.setStart(token.getPosition());
				l.setEnd(token.getPosition());
				f.addArgument(l);
			}
		}

		lexer.consume(LuaLexer.Token._RPAREN); // Consome o )
		f.acceptBody(block(LuaLexer.Token.END));
		LuaToken end = lexer.consume(LuaLexer.Token.END); // Consome o end
		f.setEnd(end.getPosition() + 3);
	}

	private Statement blockStatement() {
		Statement result;
		if (lexer.match(LuaLexer.Token.RETURN) || lexer.match(LuaLexer.Token.BREAK)) {
			result = lastStat();
		} else {
			result = stat();
		}
		lexer.advance(LuaLexer.Token._SC);
		return result;
	}
	
	private Block block(LuaLexer.Token... ends) {
		Block b = new Block();
		
		while (!Arrays.asList(ends).contains(lexer.getToken().getToken())) {
			b.addStatement(blockStatement());
		}
		return b;
	}
	/*
	private Block block(LuaLexer.Token end) {
		Block b = new Block();
		while (!lexer.match(end)) {
			b.addStatement(blockStatement());
		}
		return b;
	}*/

	private LuaFunctionDeclaration function() {
		lexer.consume(LuaLexer.Token.FUNCTION);
		LuaFunctionDeclaration f = new LuaFunctionDeclaration("", 0, 0, 0, 0);
		funcbody(f);
		return f;
	}

	private LuaFunctionDeclaration funcname() {
		// TODO Aceitar '.' e ':'
		lexer.consume(LuaLexer.Token.FUNCTION);
		LuaToken name = name();
		LuaFunctionDeclaration f = new LuaFunctionDeclaration(name.getLexeme(),
				name.getPosition(), name.getPosition()
						+ name.getLexeme().length(), name.getLine(), name
						.getLine());

		f.setStart(name.getPosition() - 9);
		f.setEnd(name.getPosition());
		f.setNameStart(name.getPosition());
		f.setNameEnd(name.getPosition() + name.getLexeme().length());
		return f;
	}

	private Statement stat() {
		LuaToken t = lexer.getToken();
		switch (t.getToken()) {
		case _NAME:
			return varlistOrFunctionCall();	
		case DO:
			lexer.consume(LuaLexer.Token.DO);
			Block block = block(LuaLexer.Token.END);
			lexer.consume(LuaLexer.Token.END);
			return block;
		case WHILE:
			lexer.consume(LuaLexer.Token.WHILE);
			Expression condition = exp();
			lexer.consume(LuaLexer.Token.DO);
			block = block(LuaLexer.Token.END);
			lexer.consume(LuaLexer.Token.END);
			return new WhileStatement(condition, block);
		case REPEAT:
			lexer.consume(LuaLexer.Token.REPEAT);
			block = block(LuaLexer.Token.UNTIL);
			lexer.consume(LuaLexer.Token.UNTIL);
			condition = exp();
			return new RepeatStatement(condition, block);
		case IF:
			return ifStatement();
		case FUNCTION:
			LuaFunctionDeclaration f = funcname();
			funcbody(f);
			return f;
		case LOCAL:
			t = lexer.getNextToken();
			if (lexer.match(LuaLexer.Token.FUNCTION)) {
				// TODO Parecido com o funcname mas sem o escopo, refatorar isso
				lexer.consume(LuaLexer.Token.FUNCTION);
				LuaToken name = name();
				f = new LuaFunctionDeclaration(name.getLexeme(), name.getPosition(),
						name.getPosition() + name.getLexeme().length(), name.getLine(),
						name.getLine());
				
				f.setStart(name.getPosition() - 9);
				f.setEnd(name.getPosition());
				f.setNameStart(name.getPosition());
				f.setNameEnd(name.getPosition() + name.getLexeme().length());

				funcbody(f);
				return f;
			} else {
				List<LuaToken> lhs = namelist();
				List<Expression> rhs = Collections.emptyList();
				if (lexer.match(LuaLexer.Token._ASSIG)) {
					lexer.consume(LuaLexer.Token._ASSIG);
					rhs = expList1();
				}
				
				List<LuaVariableReference> lhsr = new ArrayList<LuaVariableReference>();
				for (LuaToken token : lhs) {
					lhsr.add(new LuaVariableReference(token.getPosition(), token.getPosition() + token.getLexeme().length(), token.getLexeme()));
				}
				return new AssignmentExpression(lhsr, rhs);
			}
		}
		return null;
	}

	private Statement varlistOrFunctionCall() {
		if (lexer.match(LuaLexer.Token._NAME)) {
			LuaVariableReference var = var();
			LuaToken t = lexer.getToken();
			List<LuaVariableReference> lhs = new LinkedList<LuaVariableReference>();
			switch (t.getToken()) {
			case _COMMA:
				lexer.consume(LuaLexer.Token._COMMA);
				lhs = varlist1();
			case _ASSIG:
				lhs.add(0, var);				
				lexer.consume(LuaLexer.Token._ASSIG);
				List<Expression> rhs = expList1();
				return new AssignmentExpression(lhs, rhs);
			default:
				return functionCall(var);
			}
		} else {
			return functionCall();
		}
	}

	private List<LuaVariableReference> varlist1() {
		List<LuaVariableReference> varlist = new LinkedList<LuaVariableReference>();
		do {
			varlist.add(var());
		} while(lexer.match(LuaLexer.Token._COMMA));
		return varlist;
	}

	private Statement ifStatement() {
		Block block;
		Expression condition;
		// XXX TESTAR ESSA AST
		Block emptyElseBlock = new Block();
		Block currentElseBlock = new Block();
		Block elseBlock = null;
		IfStatement lastIf = null;
		lexer.consume(LuaLexer.Token.IF);
		condition = exp();
		lexer.consume(LuaLexer.Token.THEN);
		while (lexer.match(LuaLexer.Token.ELSEIF)) {
			lexer.consume(LuaLexer.Token.ELSEIF);
			Expression elsifCondition = exp();
			lexer.consume(LuaLexer.Token.THEN);
			Block elsifBlock = block(LuaLexer.Token.END, LuaLexer.Token.ELSE, LuaLexer.Token.ELSEIF);
			lastIf = new IfStatement(elsifCondition, elsifBlock, emptyElseBlock);
			currentElseBlock.addStatement(lastIf);
			elseBlock = (elseBlock == null) ? currentElseBlock : elseBlock;
			currentElseBlock = emptyElseBlock;
			emptyElseBlock = new Block();
		}
		block = block(LuaLexer.Token.END, LuaLexer.Token.ELSE, LuaLexer.Token.ELSEIF);

		if (lexer.match(LuaLexer.Token.ELSE)) {
			lexer.consume(LuaLexer.Token.ELSE);
			Block properElseBlock = block(LuaLexer.Token.END);
			if (lastIf != null)
				lastIf.setElseStatement(properElseBlock);
			elseBlock = (elseBlock == null) ? properElseBlock : elseBlock;
		}

		lexer.consume(LuaLexer.Token.END);
		return new IfStatement(condition, block, elseBlock);
	}

	private Statement lastStat() {
		LuaToken t = lexer.getToken();
		switch (t.getToken()) {
		case RETURN:
			lexer.consume(LuaLexer.Token.RETURN);
			return new ReturnStatement(expList1()); // expList? TODO tratar o
													// return vazio
		case BREAK:
			lexer.consume(LuaLexer.Token.BREAK);
			return new BreakStatement();
		default:
			throw new RuntimeException(
					"Last statement should be a RETURN or a BREAK");
		}
	}

	private LuaVariableReference var() {
		LuaToken t = lexer.getToken();
		LuaVariableReference var = null;
		switch (t.getToken()) {
		case _NAME:
			LuaToken name = name();
			var = new LuaVariableReference(name.getPosition(),
					name.getPosition() + name.getLexeme().length(), name
							.getLexeme());
			break;
		case _LPAREN:
			lexer.consume(LuaLexer.Token._LPAREN);
			exp(); // Onde eu guardo isso?
			varSuffix();
			break;
		}

		varSuffix();
		// TODO varSuffix*
		
		return var;
	}

	private void var1(LuaVariableReference var) {
		LuaToken acessor = lexer.getToken();
		switch (acessor.getToken()) {
		case _LBRACK:
			lexer.consume(LuaLexer.Token._LBRACK);
			var.addReference(exp());
			lexer.consume(LuaLexer.Token._RBRACK); // Consome o ]
			break;
		case _DOT:
			LuaToken name = lexer.getNextToken();
			// TODO usar uma estrutura mais simples, já q é apenas um name
			LuaVariableReference ref = new LuaVariableReference(name
					.getPosition(), name.getPosition()
					+ name.getLexeme().length(), name.getLexeme());
			var.addReference(ref);
			lexer.getNextToken(); // Avança o token
			break;
		default:
			return;
		}
		var1(var);
	}

	private Statement functionCall() {
		return functionCall(null);
	}
	private Statement functionCall(Statement var) {
		if (var == null)
			varOrExp();
		// TODO nameAndArgs+
		nameAndArgs();

		// TODO decidir o que retornar aqui
		return new Statement() {

			@Override
			public int getKind() {
				return 0;
			}

			@Override
			public void traverse(ASTVisitor visitor) throws Exception {

			}

		};
	}

	private Expression varOrExp() {
		Expression e = null;
		if (lexer.match(LuaLexer.Token._NAME)) {
			e = var();
		} else {
			e = exp();
			varSuffix();
		}
		return e;
	}

	private void varSuffix() {
		//nameAndArgs();
		// nameAndArgs* TODO
		LOGGER.log(Level.INFO, String.format("Variable suffix using %s", lexer.getToken().getLexeme()));
		if (lexer.match(LuaLexer.Token._LBRACK)) {
			lexer.consume(LuaLexer.Token._LBRACK);
			exp();
			lexer.consume(LuaLexer.Token._RBRACK);
		} else if (lexer.match(LuaLexer.Token._DOT)) {
			lexer.consume(LuaLexer.Token._DOT);
			name();
		}
	}

	private void nameAndArgs() {
		if (lexer.match(LuaLexer.Token._COLON)) {
			lexer.consume(LuaLexer.Token._COLON);
			name();			// TODO colocar na referência
		}
		args();
	}

	private void args() {
		LuaToken token = lexer.getToken();
		switch (token.getToken()) {
		case _STRING:
			lexer.consume(LuaLexer.Token._STRING);
			break;
		case _LPAREN:
			lexer.consume(LuaLexer.Token._LPAREN);
			if (!lexer.match(LuaLexer.Token._RPAREN))
				expList1();
			lexer.consume(LuaLexer.Token._RPAREN);
			break;
		case _LCURLY:
			// tableConstructor TODO
		default:
			break;
		}

	}

	private List<Expression> expList1() {
		// TODO ver o q retorna
		List<Expression> exps = new ArrayList<Expression>();
		exps.add(exp());
		while (lexer.match(LuaLexer.Token._COMMA)) {
			lexer.consume(LuaLexer.Token._COMMA);
			exps.add(exp());
		}
		return exps;
	}

	private Expression exp() {
		// TODO review
		LuaToken t = lexer.getToken();
		Expression e = null;
		switch (t.getToken()) {
		case NIL:
			lexer.consume(LuaLexer.Token.NIL);
			e = new NilLiteral(t.getPosition(), t.getPosition() + 3);
			break;
		case FALSE:
			lexer.consume(LuaLexer.Token.FALSE);
			e = new BooleanLiteral(t.getPosition(), t.getPosition() + 5, false);
			break;
		case TRUE:
			lexer.consume(LuaLexer.Token.TRUE);
			e = new BooleanLiteral(t.getPosition(), t.getPosition() + 4, true);
			break;
		case _NUMBER:
			lexer.consume(LuaLexer.Token._NUMBER);
			e = new FloatNumericLiteral(t.getPosition(), t.getPosition()
					+ t.getLexeme().length(), t.getLexeme());
			break;
		case _STRING:
			lexer.consume(LuaLexer.Token._STRING);
			e = new StringLiteral(t.getPosition(), t.getPosition()
					+ t.getLexeme().length(), t.getLexeme());
			break;
		case _LCURLY:
			tableConstructor();
			break;
		default:
			// TODO fazer o dots
			// TODO fazer os operadores unários
		}
		
		if (e == null && lexer.match(LuaLexer.Token.FUNCTION)) {
			e = new Closure(function());
		}
		
		if (e == null) {
			e = prefixExpression();			
		}

		t = lexer.getToken();

		if (LuaLexer.isBinaryExpressionToken(t)) {
			lexer.getNextToken(); // TODO Arrumar isso, é só pra consumir
			Expression l = e;
			Expression r = exp();
			return new BinaryExpression(l, 0, r); // TODO colocar o tipo da
													// relação
		}
		return e;
	}

	private void tableConstructor() {
		lexer.consume(LuaLexer.Token._LCURLY);
		if (!lexer.match(LuaLexer.Token._RCURLY))
			fieldList();
		lexer.consume(LuaLexer.Token._RCURLY);
	}

	private void fieldList() {
		field();
		while (lexer.match(LuaLexer.Token._COMMA) || lexer.match(LuaLexer.Token._SC)) {
			lexer.getNextToken(); // Consume-like
			field();
		}
		// Optional separator
		if (lexer.match(LuaLexer.Token._COMMA) || lexer.match(LuaLexer.Token._SC)) {
			lexer.getNextToken(); // Consume-like
		}
	}

	private void field() {
		if (lexer.match(LuaLexer.Token._LBRACK)) {
			lexer.consume(LuaLexer.Token._LBRACK);
			exp();
			lexer.consume(LuaLexer.Token._RBRACK);
			lexer.consume(LuaLexer.Token._ASSIG);
			exp();
		} else if (lexer.match(LuaLexer.Token._NAME)) {
			lexer.consume(LuaLexer.Token._NAME);
			if (lexer.match(LuaLexer.Token._ASSIG)) {
				lexer.consume(LuaLexer.Token._ASSIG);
				exp();
				return;
			} else {
				exp();
			}
		} else {
			exp();
		}
	}

	private Expression prefixExpression() {
		LuaToken t = lexer.getToken();
		Expression e;
		switch (t.getToken()) {
		case _NAME:
		case _LPAREN:
			e = varOrExp();
			break;
		default:
			return null;
		}

		t = lexer.getToken();

		switch (t.getToken()) {
		case _LPAREN:
		case _LCURLY:
		case _STRING:
			nameAndArgs(); // TODO nameAndArgs*
			break;
		}
		return e;
	}

	private void chunk() {
		lexer.getNextToken();
		while (!lexer.match(LuaLexer.Token._EOF)) {
			Statement stm;
			if (lexer.match(LuaLexer.Token.RETURN)
					|| lexer.match(LuaLexer.Token.BREAK)) {
				stm = lastStat();
			} else {
				stm = stat();
			}
			if (stm == null) {
				LOGGER.log(Level.INFO, "Statement null?");
				throw new RuntimeException("Statement null?");
			}
			LOGGER.log(Level.INFO, String.format("Statement: %s", stm.toString()));
			moduleDeclaration.addStatement(stm);
			lexer.advance(LuaLexer.Token._SC);
		}

	}

	public LuaModuleDeclaration parse() {
		chunk();
		return moduleDeclaration;
	}
}
