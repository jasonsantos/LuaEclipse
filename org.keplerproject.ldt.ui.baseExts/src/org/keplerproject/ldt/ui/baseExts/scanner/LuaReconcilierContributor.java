package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.TextAttribute;
import org.eclipse.jface.text.presentation.PresentationReconciler;
import org.eclipse.jface.text.rules.DefaultDamagerRepairer;
import org.eclipse.jface.text.rules.IPredicateRule;
import org.eclipse.jface.text.rules.IToken;
import org.eclipse.jface.text.rules.ITokenScanner;
import org.eclipse.jface.text.rules.SingleLineRule;
import org.eclipse.jface.text.rules.Token;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.swt.SWT;
import org.keplerproject.ldt.ui.editors.LuaBaseScanner;
import org.keplerproject.ldt.ui.editors.LuaColorManager;
import org.keplerproject.ldt.ui.editors.NonRuleBasedDamagerRepairer;
import org.keplerproject.ldt.ui.editors.ext.ILuaReconcilierExtension;
import org.keplerproject.ldt.ui.text.rules.LuaWhitespaceRule;
import org.keplerproject.ldt.ui.text.rules.LuaWordRule;

public class LuaReconcilierContributor implements ILuaReconcilierExtension,
		ILuaSyntax {

	private LuaColorManager fColorManager;

	public void contribute(LuaColorManager colorManager,
			PresentationReconciler reconciler, ISourceViewer viewer) {
		this.fColorManager = colorManager;
		DefaultDamagerRepairer dr = new DefaultDamagerRepairer(getCodeScanner());
		reconciler.setDamager(dr, IDocument.DEFAULT_CONTENT_TYPE);
		reconciler.setRepairer(dr, IDocument.DEFAULT_CONTENT_TYPE);

		NonRuleBasedDamagerRepairer ndr = new NonRuleBasedDamagerRepairer(
				new TextAttribute(colorManager
						.getColor(ILuaColorConstants.LUA_MULTI_LINE_COMMENT)));
		ndr.setDocument(viewer.getDocument());

		reconciler.setDamager(ndr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);
		reconciler.setRepairer(ndr, ILuaPartitions.LUA_MULTI_LINE_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager
				.getColor(ILuaColorConstants.LUA_SINGLE_LINE_COMMENT)));
		ndr.setDocument(viewer.getDocument());

		reconciler.setDamager(ndr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);
		reconciler.setRepairer(ndr, ILuaPartitions.LUA_SINGLE_LINE_COMMENT);

		ndr = new NonRuleBasedDamagerRepairer(new TextAttribute(colorManager
				.getColor(ILuaColorConstants.LUA_STRING)));
		ndr.setDocument(viewer.getDocument());
		dr.setDocument(viewer.getDocument());
		reconciler.setDamager(ndr, ILuaPartitions.LUA_STRING);
		reconciler.setRepairer(ndr, ILuaPartitions.LUA_STRING);
	}

	private ITokenScanner getCodeScanner() {
		LuaBaseScanner scanner = new LuaBaseScanner();

		IToken keyword = new Token(new TextAttribute(
				this.fColorManager.getColor(ILuaColorConstants.LUA_KEYWORD),
				null, SWT.BOLD));
		IToken constant = new Token(new TextAttribute(
				this.fColorManager.getColor(ILuaColorConstants.LUA_CONSTANTS),
				null, SWT.ITALIC));
		IToken string = new Token(new TextAttribute(fColorManager
				.getColor(ILuaColorConstants.LUA_STRING)));
		 IToken function = new Token(new
		 TextAttribute(this.fColorManager.getColor(ILuaColorConstants.LUA_METHOD_NAME), null, SWT.BOLD | SWT.ITALIC));
		IToken other = new Token(new TextAttribute(this.fColorManager
				.getColor(ILuaColorConstants.LUA_DEFAULT), null, SWT.NONE));
		// Add generic whitespace rule.
		LuaWhitespaceRule whiteRule = new LuaWhitespaceRule(
				new LuaWhitespaceDetector());

		// Add word rule for keywords and constants.
		LuaWordRule wordRule = new LuaWordRule(new LuaWordDetector(), other);
		for (int i = 0; i < reservedwords.length; i++)
			wordRule.addWord(reservedwords[i], keyword);
		for (int i = 0; i < constants.length; i++)
			wordRule.addWord(constants[i], constant);
				for (int i = 0; i < functions.length; i++)
			wordRule.addWord(functions[i], function);	 

		scanner.setPredicateRules(new IPredicateRule[] { wordRule,
				new SingleLineRule("\"", "\"", string, '\\'),
				new SingleLineRule("'", "'", string, '\\'),
				whiteRule });

		return scanner;
	}

}
