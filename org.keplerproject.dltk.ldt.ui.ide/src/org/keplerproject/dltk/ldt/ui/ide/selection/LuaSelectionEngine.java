package org.keplerproject.dltk.ldt.ui.ide.selection;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.dltk.ast.ASTVisitor;
import org.eclipse.dltk.ast.declarations.MethodDeclaration;
import org.eclipse.dltk.ast.declarations.ModuleDeclaration;
import org.eclipse.dltk.codeassist.ISelectionEngine;
import org.eclipse.dltk.compiler.env.ISourceModule;
import org.eclipse.dltk.core.DLTKCore;
import org.eclipse.dltk.core.IModelElement;
import org.eclipse.dltk.core.IModelElementVisitor;
import org.eclipse.dltk.core.ModelException;
import org.eclipse.dltk.core.SourceParserUtil;

public class LuaSelectionEngine implements ISelectionEngine {
	private org.eclipse.dltk.core.ISourceModule sourceModule;

	public IModelElement[] select(ISourceModule module, final int offset, int i) {
		sourceModule = (org.eclipse.dltk.core.ISourceModule) module.getModelElement();

		ModuleDeclaration moduleDeclaration = SourceParserUtil
				.getModuleDeclaration(sourceModule, null);
		final List<IModelElement> results = new ArrayList<IModelElement>();

		try {
			moduleDeclaration.traverse(new ASTVisitor() {
				@Override
				public boolean visit(MethodDeclaration s) throws Exception {
					if (s.getNameStart() <= offset && offset <= s.getNameEnd()) {
						findDeclaration(s.getName(), results);
					}
					return super.visit(s);
				}
			});
		} catch (Exception e) {
			if (DLTKCore.DEBUG) {
				e.printStackTrace();
			}
		}
		return results.toArray(new IModelElement[results.size()]);
	}

	private void findDeclaration(final String name,
			final List<IModelElement> results) {
		try {
			sourceModule.accept(new IModelElementVisitor() {
				public boolean visit(IModelElement element) {
					if (element.getElementName().equals(name)) {
						results.add(element);
					}
					return true;
				}
			});
		} catch (ModelException e) {
			if (DLTKCore.DEBUG)
				e.printStackTrace();
		}
	}

	public void setOptions(Map options) {
	}

}
