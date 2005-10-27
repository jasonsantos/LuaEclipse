package org.keplerproject.ldt.ui.editors;

import java.util.ResourceBundle;

import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.Separator;
import org.eclipse.ui.IWorkbenchActionConstants;
import org.eclipse.ui.editors.text.TextEditorActionContributor;
import org.eclipse.ui.texteditor.RetargetTextEditorAction;
import org.keplerproject.ldt.ui.LDTUIPlugin;

public class LuaEditorActionContributor extends TextEditorActionContributor {

	private IAction fContentAssistProposal;
	private IAction fContentAssistTipProposal;

	public LuaEditorActionContributor() {
		super();
		ResourceBundle bundle = LDTUIPlugin.getDefault().getResourceBundle();

		fContentAssistProposal = new RetargetTextEditorAction(bundle,"ContentAssistProposal.");
		fContentAssistTipProposal = new RetargetTextEditorAction(bundle,"ContentAssistTipProposal.");
		
	}
	public void contributeToMenu(IMenuManager menu) {
		IMenuManager editMenu = menu.findMenuUsingPath(IWorkbenchActionConstants.M_EDIT);
		if(editMenu != null)
		{
			editMenu.add(new Separator());
			editMenu.add(fContentAssistProposal);
			editMenu.add(fContentAssistTipProposal);
		}
		super.contributeToMenu(menu);
	}
	
}
