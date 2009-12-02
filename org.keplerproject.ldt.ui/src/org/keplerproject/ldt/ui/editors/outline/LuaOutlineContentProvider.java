package org.keplerproject.ldt.ui.editors.outline;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.jface.text.DocumentEvent;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IDocumentListener;
import org.eclipse.jface.viewers.ITreeContentProvider;
import org.eclipse.jface.viewers.StructuredViewer;
import org.eclipse.jface.viewers.Viewer;
import org.eclipse.swt.widgets.Display;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.keplerproject.ldt.ui.editors.LuaEditor;


//Do this for the whole class until a compliance level is committed to
@SuppressWarnings("unchecked")
public class LuaOutlineContentProvider implements ITreeContentProvider {
	public static String OFFSET_PROPERTY = "offsets";

	StructuredViewer    fViewer = null;
	LuaEditor 			fEditorInput = null;

	IDocument 			fDocument;
	IDocumentListener	fDocumentListener;

	//Maintain a map of function name -> FunctionDefinition
	//Use this to do incremental updates of only the differences in editing
	HashMap	fFunctionCache = new HashMap();
	
	//TODO: Extract this to an external class
	public static final class FunctionDefinition {
		String fName; 
		int    fCharOffset;
		int    fCharEndOffset;
		
		public FunctionDefinition(String name, int charOffset, int charEndOffset) {
			fName = name;
			fCharOffset = charOffset;
			fCharEndOffset = charEndOffset;
		}
		
		public String getName() {
			return fName;
		}

		public int getCharacterOffset() {
			return fCharOffset;
		}
		
		public void setCharacterOffset(int offset) {
			fCharOffset = offset;
		}

		public int getCharacterEndOffset() {
			return fCharEndOffset;
		}

		public void setCharacterEndOffset(int offset) {
			fCharEndOffset = offset;
		}

		public String toString() {
			return getName();
		}
		
		public boolean offsetsMatch(FunctionDefinition def) {
			if(getCharacterOffset() != def.getCharacterOffset()) {
				return false;
			}
			if(getCharacterEndOffset() != def.getCharacterEndOffset()) {
				return false;
			}
			return true;
		}
	};
	
	public LuaOutlineContentProvider() {
		fDocumentListener = new IDocumentListener() {
			public void documentAboutToBeChanged(DocumentEvent event) {
			}

			public void documentChanged(DocumentEvent event) {
				refreshCache(true);
			}
		};
	}
	
	public void inputChanged(Viewer viewer, Object oldInput, Object newInput) {
		if(fEditorInput != null) {
			if(fDocument != null) {
				fDocument.removeDocumentListener(fDocumentListener);
			}
		}
		
		fViewer = null;
		fEditorInput = null;
		fDocument = null;
		fFunctionCache.clear();
		
		if(newInput instanceof LuaEditor) {
			fViewer = (StructuredViewer)viewer;
			fEditorInput = (LuaEditor)newInput;
			IDocumentProvider provider = fEditorInput.getDocumentProvider();
			fDocument = provider.getDocument(fEditorInput.getEditorInput());
			if(fDocument != null) {
				fDocument.addDocumentListener(fDocumentListener);
			}
			refreshCache(false);
		}
	}

	public Object[] getElements(Object inputElement) {
		if(inputElement == fEditorInput && fDocument != null) {
			return fFunctionCache.values().toArray();
		}
		
		return new Object[0];
	}
	
	public Object[] getChildren(Object parentElement) {
		return getElements(parentElement);
	}

	public Object getParent(Object element) {
		return null;
	}

	public boolean hasChildren(Object element) {
		return getElements(element).length > 0;
	}

	public void dispose() {
	}
	
	protected void refreshCache(boolean doRefresh) {
		//TODO: Put in a background refresh thread
		backgroundRefresh(doRefresh);
	}
	
	protected void backgroundRefresh(boolean doRefresh) {
		if(fViewer == null) {
			fFunctionCache.clear();
			return;
		}
		
		//Parse the document content and extract all function information
		FunctionDefinition [] allDefs;
		String content = fDocument.get();
		if(content != null) {
			allDefs = parseFunctions(content);
		} else {
			allDefs = new FunctionDefinition[0];
		}
				
		//Get a copy of the original keys to use as markers
		Set oldKeys = new HashSet(fFunctionCache.keySet());
		
		final ArrayList updateList = new ArrayList();
		
		//Correlate and update as required
		boolean functionAdded = false;
		for(int i = 0; i < allDefs.length; i++) {
			FunctionDefinition newDef = (FunctionDefinition)allDefs[i];
			final FunctionDefinition oldDef = (FunctionDefinition)fFunctionCache.get(newDef.getName());
			if(oldDef == null) {
				fFunctionCache.put(newDef.getName(), newDef);
				functionAdded = true;
			} else {
				oldKeys.remove(newDef.getName());
				if(!oldDef.offsetsMatch(newDef)) {
					oldDef.setCharacterOffset(newDef.getCharacterOffset());
					oldDef.setCharacterEndOffset(newDef.getCharacterEndOffset());
					updateList.add(oldDef);
				}
			}
		}

		//Check and see if any items were deleted, that causes a full refresh 
		boolean functionRemoved = oldKeys.size() != 0;
		Iterator it = oldKeys.iterator();
		while(it.hasNext()) {
			FunctionDefinition oldDef = (FunctionDefinition)fFunctionCache.get(it.next());
			fFunctionCache.remove(oldDef.getName());
		}

		if(!doRefresh) {
			return;
		}
		
		final boolean fullRefresh = functionAdded || functionRemoved;
		
		Display display = fViewer.getControl().getDisplay();
		display.syncExec(new Runnable() {
			public void run() {
				if(fViewer == null || fViewer.getControl().isDisposed()) {
					return;
				}
				
				if(fullRefresh) {
					fViewer.refresh();
				} else {
					String [] offsetProperty = new String[] { OFFSET_PROPERTY };

					FunctionDefinition [] elements;
					elements = (FunctionDefinition[])updateList.toArray(new FunctionDefinition[updateList.size()]);

					fViewer.update(elements, offsetProperty);									
				}
			}
		});
	}
	
	//This is kind of a weak identification, but it is fairly resilient in the face of errors
	Pattern fLuaFunctionPattern = Pattern.compile("^\\w*function\\s+(\\w+)\\s*\\(.*$", Pattern.MULTILINE);

	protected FunctionDefinition [] parseFunctions(String fileContents) {
		ArrayList<FunctionDefinition> functionList = new ArrayList<FunctionDefinition>();
		
		Matcher matcher = fLuaFunctionPattern.matcher(fileContents);
		int offset = 0;
		while(matcher.find(offset)) {
			int startOffset = matcher.start();
			offset = matcher.end();
			String functionName = matcher.group(1);
			functionList.add(new FunctionDefinition(functionName, startOffset, offset));
		}
	
		return (FunctionDefinition [])functionList.toArray(new FunctionDefinition[functionList.size()]);
	}	
}
