package org.keplerproject.ldt.ui.editors;

import org.eclipse.core.runtime.CoreException;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentPartitioner;
import org.eclipse.jface.text.rules.FastPartitioner;
import org.eclipse.ui.editors.text.FileDocumentProvider;

public class LuaDocumentProvider extends FileDocumentProvider {

	protected IDocument createDocument(Object element) throws CoreException {
		IDocument document = super.createDocument(element);
		if (document != null) {
			IDocumentPartitioner partitioner =
				new FastPartitioner(
					new LuaPartitionScanner(),
					new String[] {
						ILuaPartitions.LUA_MULTI_LINE_COMMENT,
						ILuaPartitions.LUA_SINGLE_LINE_COMMENT,
						ILuaPartitions.LUA_STRING,
						ILuaPartitions.LUA_SKIP});
			partitioner.connect(document);
			document.setDocumentPartitioner(partitioner);
		}
		return document;
	}
}