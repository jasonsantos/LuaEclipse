/**************************************
 * 
 **********************************/
package org.keplerproject.ldt.ui.editors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.ResourceBundle;

import org.eclipse.core.resources.IResourceChangeEvent;
import org.eclipse.core.resources.IResourceChangeListener;
import org.eclipse.jface.text.IDocument;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.source.Annotation;
import org.eclipse.jface.text.source.ISourceViewer;
import org.eclipse.jface.text.source.IVerticalRuler;
import org.eclipse.jface.text.source.projection.ProjectionAnnotation;
import org.eclipse.jface.text.source.projection.ProjectionAnnotationModel;
import org.eclipse.jface.text.source.projection.ProjectionSupport;
import org.eclipse.jface.text.source.projection.ProjectionViewer;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.editors.text.TextEditor;
import org.eclipse.ui.texteditor.DefaultRangeIndicator;
import org.eclipse.ui.texteditor.IDocumentProvider;
import org.eclipse.ui.texteditor.TextOperationAction;
import org.keplerproject.ldt.ui.LDTUIPlugin;
import org.keplerproject.ldt.ui.compiler.LuaChangeListener;

/**
 * Lua Text Editor.
 * 
 * @author Guilherme Martins
 * 
 */

public class LuaEditor extends TextEditor {

	private LuaColorManager colorManager;

	private LuaSourceViewerConfiguration sourceViewer;

	private ProjectionSupport projectionSupport;

	private ProjectionAnnotationModel annotationModel;

	public LuaEditor() {
		super();
		// initialize the color manager
		colorManager = createColorManager();
		// creates the sourceviewerConfig
		sourceViewer = createSourceConfiguration(colorManager);
		// IMPORTANT.. sets the EditorPart to the viewer
		sourceViewer.setEditor(this);
		// setup the source viewer
		setSourceViewerConfiguration(sourceViewer);
		// Setup the LuaDocumentProvider
		setDocumentProvider(createDocumentProvider(this));
		setRangeIndicator(new DefaultRangeIndicator());

		// This register a resourceChangeListener to compile de lua script and
		// get the error message
		IResourceChangeListener listener = createResourceChangeListener(this);
		LDTUIPlugin.getWorkspace().addResourceChangeListener(listener,
				IResourceChangeEvent.POST_BUILD);
	}

	/**
	 * This method must create a ResourceChangeListener to be registered at the
	 * workspace. This listener will be notified when the la file has been
	 * saved.
	 * 
	 * @param editor
	 * @return The resource change Listener
	 */
	protected IResourceChangeListener createResourceChangeListener(
			LuaEditor editor) {
		return new LuaChangeListener();
	}

	/**
	 * This method creates a Document provider to the lua editor. This document
	 * provider must create partitions of the code. This defaul implementation
	 * uses a LDT extension point.
	 * 
	 * @param editor
	 * @return
	 */
	protected IDocumentProvider createDocumentProvider(LuaEditor editor) {
		return new LuaDocumentProvider();
	}

	/**
	 * This method creates a lua Source configuraton to the code viewer.
	 * Providing Content assist, syntax highlight and code formating to the lua
	 * code. This implementation loads the contributor os LDT extension points.
	 * 
	 * @param colorManager
	 * @return
	 */
	protected LuaSourceViewerConfiguration createSourceConfiguration(
			LuaColorManager colorManager) {
		return new LuaSourceViewerConfiguration(colorManager);
	}

	/**
	 * Creates a LuaEditor Color Manager for general propouse.
	 * 
	 * @return
	 */
	protected LuaColorManager createColorManager() {
		return new LuaColorManager();
	}

	public void dispose() {
		colorManager.dispose();
		super.dispose();
	}

	/**
	 * Unusable for now.!
	 */
	protected void createActions() {
		super.createActions();
		ResourceBundle bundle = LDTUIPlugin.getDefault().getResourceBundle();

		setAction("LuaContentAssistProposal", new TextOperationAction(bundle,
				"ContentAssistProposal.", this,
				ISourceViewer.CONTENTASSIST_PROPOSALS));
		setAction("LuaContentAssistTipProposal", new TextOperationAction(
				bundle, "ContentAssistTipProposal.", this,
				ISourceViewer.CONTENTASSIST_CONTEXT_INFORMATION));
	}

	/**
	 * This method was re-implemented to create a code folding feature.
	 * 
	 */
	public void createPartControl(Composite parent) {
		super.createPartControl(parent);
		ProjectionViewer viewer = (ProjectionViewer) getSourceViewer();

		projectionSupport = new ProjectionSupport(viewer,
				getAnnotationAccess(), getSharedColors());
		projectionSupport.install();
		projectionSupport.addSummarizableAnnotationType(
        "org.eclipse.ui.workbench.texteditor.error");
		
		// TODO add this to have a better result.. 
		/*projectionSupport.setHoverControlCreator(new IInformationControlCreator() {
			   public IInformationControl createInformationControl(Shell shell) {
			     return new CustomSourceInformationControl(shell, IDocument.DEFAULT_CONTENT_TYPE);
			   }
			});*/
		// turn projection mode on
		viewer.doOperation(ProjectionViewer.TOGGLE);

		annotationModel = viewer.getProjectionAnnotationModel();
	}

	
	/**
	 * This method was re-implemented to create a code folding feature.
	 * 
	 */
	protected ISourceViewer createSourceViewer(Composite parent,
			IVerticalRuler ruler, int styles) {
		fAnnotationAccess = createAnnotationAccess();
		fOverviewRuler = createOverviewRuler(getSharedColors());

		ISourceViewer viewer = new ProjectionViewer(parent, ruler,
				getOverviewRuler(), isOverviewRulerVisible(), styles);

		// ensure decoration support has been created and configured.
		getSourceViewerDecorationSupport(viewer);

		return viewer;
	}
	
	private Annotation[] oldAnnotations;
	/**
	 * Folding update Structure
	 * @param positions
	 */
	public void updateFoldingStructure(ArrayList positions)
	{
	   Annotation[] annotations = new Annotation[positions.size()];

	   //this will hold the new annotations along
	   //with their corresponding positions
	   HashMap newAnnotations = new HashMap();

	   for(int i = 0; i < positions.size();i++)
	   {
	      ProjectionAnnotation annotation = new ProjectionAnnotation();

	      newAnnotations.put(annotation, positions.get(i));

	      annotations[i] = annotation;
	   }

	   annotationModel.modifyAnnotations(oldAnnotations, newAnnotations,null);

	   oldAnnotations = annotations;
	}


}
