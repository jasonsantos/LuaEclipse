package org.keplerproject.ldt.ui.baseExts.scanner;


import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.EndOfLineRule;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.MultiLineRule;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.swt.SWT;
import org.keplerproject.ldt.ui.editors.LuaBaseScanner;
import org.keplerproject.ldt.ui.editors.LuaColorManager;
import org.keplerproject.ldt.ui.editors.NonRuleBasedDamagerRepairer;
import org.keplerproject.ldt.ui.editors.ext.ILuaReconcilierExtension;
import org.keplerproject.ldt.ui.text.rules.LuaWhitespaceRule;
import org.keplerproject.ldt.ui.text.rules.LuaWordRule;
import org.keplerproject.ldt.ui.text.rules.NestedPatternRule;

public class LuaReconcilierContributor implements ILuaReconcilierExtension ,ILuaSyntax{

	private LuaColorManager fColorManager;

	public void contribute(LuaColorManager colorManager, PresentationReconciler reconciler) {
		this.fColorManager = colorManager;
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCodeScanner());
        reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
        reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);
        // TODO MOdificado para nonRuleBased para testes
        NonRuleBasedDamagerRepairer ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ILuaColorConstants.LUA_MULTI_LINE_COMMENT)));
		reconciler.setDamager(ndr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);
		reconciler.setRepairer(ndr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);		
    
        dr = new DefaultDamagerRepairer(getSinglelineCommentScanner());
        reconciler.setDamager(dr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);
        reconciler.setRepairer(dr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);
        // TODO MOdificado para nonRuleBased para testes
        ndr =
			new NonRuleBasedDamagerRepairer(
				new TextAttribute(
					colorManager.getColor(ILuaColorConstants.LUA_STRING)));
        
        reconciler.setDamager(ndr, ILuaPartitions.LUA_STRING);
        reconciler.setRepairer(ndr, ILuaPartitions.LUA_STRING);
	}

	private ITokenScanner getSinglelineCommentScanner() {
		IToken comment = new Token(new TextAttribute(fColorManager
				.getColor(ILuaColorConstants.LUA_SINGLE_LINE_COMMENT)));		
		LuaBaseScanner scanner = new LuaBaseScanner();
		scanner.addRules(new IPredicateRule[] {new LuaWhitespaceRule(new LuaWhitespaceDetector()),new EndOfLineRule("--", comment)});
		return scanner;
	}

	private ITokenScanner getMultilineCommentScanner() {
		IToken multi_comment = new Token(new TextAttribute(fColorManager
				.getColor(ILuaColorConstants.LUA_MULTI_LINE_COMMENT)));		
		LuaBaseScanner scanner = new LuaBaseScanner();
		scanner.addRules(new IPredicateRule[] {new LuaWhitespaceRule(new LuaWhitespaceDetector()),new NestedPatternRule("--[[","[[","]]",multi_comment)
		});
		return scanner;
	}

	private ITokenScanner getStringScanner() {
		IToken string = new Token(new TextAttribute(fColorManager
				.getColor(ILuaColorConstants.LUA_STRING)));		
		LuaBaseScanner scanner = new LuaBaseScanner();
		scanner.addRules(new IPredicateRule[] {new SingleLineRule("\"", "\"", string, '\\'),
		new SingleLineRule("'", "'", string, '\\'),
		new LuaWhitespaceRule(new LuaWhitespaceDetector()),
		new MultiLineRule("<!--", "-->", string)});
		return scanner;
	}

	private ITokenScanner getCodeScanner() {
		LuaBaseScanner scanner = new LuaBaseScanner();

		IToken keyword = new Token(new org.eclipse.jface.text.TextAttribute(this.fColorManager.getColor(ILuaColorConstants.LUA_KEYWORD), null, SWT.BOLD));
		//IToken function = new Token(new TextAttribute(this.fColorManager.getColor(ILuaColorConstants.LUA_METHOD_NAME), null, SWT.BOLD));
		IToken other = new Token(new TextAttribute(this.fColorManager.getColor(ILuaColorConstants.LUA_DEFAULT), null, SWT.NONE));
		// Add generic whitespace rule.
		LuaWhitespaceRule whiteRule =  new LuaWhitespaceRule(new LuaWhitespaceDetector());

		// Add word rule for keywords and constants.
		LuaWordRule wordRule = new LuaWordRule(new LuaWordDetector(), other);
		for (int i = 0; i < reservedwords.length; i++)
			wordRule.addWord(reservedwords[i], keyword);
		
		/*//TODO  Add word rule for functions.
		WordRule funcRule = new WordRule(new LuaFunctionDetector(), other);
		for (int i = 0; i < functions.length; i++)
			wordRule.addWord(functions[i], function);

		rules.add(funcRule);*/
		
		scanner.addRules(new IPredicateRule[]{wordRule,whiteRule});
		return scanner;
	}

}
