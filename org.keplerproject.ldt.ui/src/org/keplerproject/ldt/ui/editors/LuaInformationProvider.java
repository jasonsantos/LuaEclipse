package org.keplerproject.ldt.ui.editors;

import org.eclipse.jface.internal.text.html.BrowserInformationControl;
import org.eclipse.jface.internal.text.html.HTMLTextPresenter;
import org.eclipse.jface.text.DefaultInformationControl;
import org.eclipse.jface.text.IInformationControl;
import org.eclipse.jface.text.IInformationControlCreator;
import org.eclipse.jface.text.IRegion;
import org.eclipse.jface.text.ITextHover;
import org.eclipse.jface.text.ITextViewer;
import org.eclipse.jface.text.information.IInformationProvider;
import org.eclipse.jface.text.information.IInformationProviderExtension2;
import org.eclipse.swt.SWT;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.ui.IEditorPart;
import org.eclipse.ui.IPartListener;
import org.eclipse.ui.IPerspectiveDescriptor;
import org.eclipse.ui.IWorkbenchPage;
import org.eclipse.ui.IWorkbenchPart;
import org.eclipse.ui.IWorkbenchWindow;
import org.keplerproject.ldt.ui.text.lua.LuaWordFinder;

/**
 * Lua Documentation Information Provider.
 * @author jasonsantos
 * @version $Id$
 * @since 1.2.1
 */
public class LuaInformationProvider implements IInformationProvider,
		IInformationProviderExtension2 {
	

     class EditorWatcher implements IPartListener {

         /**
          * @see IPartListener#partOpened(IWorkbenchPart)
          */
         public void partOpened(IWorkbenchPart part) {
         }

         /**
          * @see IPartListener#partDeactivated(IWorkbenchPart)
          */
         public void partDeactivated(IWorkbenchPart part) {
         }

         /**
          * @see IPartListener#partClosed(IWorkbenchPart)
          */
         public void partClosed(IWorkbenchPart part) {
             if (part == fEditor) {
                 fEditor.getSite().getWorkbenchWindow().getPartService().removePartListener(fPartListener);
                 fPartListener= null;
             }
         }

         /**
          * @see IPartListener#partActivated(IWorkbenchPart)
          */
         public void partActivated(IWorkbenchPart part) {
             update();
         }

         public void partBroughtToTop(IWorkbenchPart part) {
             update();
         }
     }

     protected IEditorPart fEditor;
     protected IPartListener fPartListener;

     protected String fCurrentPerspective;
     protected LuaTextHover fImplementation;
     private boolean fShowInBrowser;


     public LuaInformationProvider(IEditorPart editor) {

         fEditor= editor;

         if (fEditor != null) {

             fPartListener= new EditorWatcher();
             IWorkbenchWindow window= fEditor.getSite().getWorkbenchWindow();
             window.getPartService().addPartListener(fPartListener);

             update();
         }
     }

     protected void update() {

         IWorkbenchWindow window= fEditor.getSite().getWorkbenchWindow();
         IWorkbenchPage page= window.getActivePage();
         if (page != null) {

             IPerspectiveDescriptor perspective= page.getPerspective();
             if (perspective != null) {
                 String perspectiveId= perspective.getId();

                 if (fCurrentPerspective == null || fCurrentPerspective != perspectiveId) {
                     fCurrentPerspective= perspectiveId;

                     fImplementation= new LuaTextHover();
                     //fImplementation.setEditor(fEditor);
                 }
             }
         }
     }

     /*
      * @see IInformationProvider#getSubject(ITextViewer, int)
      */
     public IRegion getSubject(ITextViewer textViewer, int offset) {

         if (textViewer != null)
             return LuaWordFinder.findWord(textViewer.getDocument(), offset);

         return null;
     }

     /*
      * @see IInformationProvider#getInformation(ITextViewer, IRegion)
      */
     public String getInformation(ITextViewer textViewer, IRegion subject) {
         fShowInBrowser= false;
         if (fImplementation != null) {
             String s= fImplementation.getHoverInfo(textViewer, subject);
             if (s != null && s.trim().length() > 0) {
                 fShowInBrowser= s.indexOf("<LINK REL=\"stylesheet\" HREF= \"") != -1; //$NON-NLS-1$
 return s;
             }
         }

         return null;
     }

     /*
      * @see IInformationProviderExtension2#getInformationPresenterControlCreator()
      * @since 3.1
      */
     public IInformationControlCreator getInformationPresenterControlCreator() {
         return new IInformationControlCreator() {
             public IInformationControl createInformationControl(Shell parent) {
                 int shellStyle= SWT.RESIZE | SWT.TOOL;
                 int style= SWT.V_SCROLL | SWT.H_SCROLL;
                 if (fShowInBrowser && BrowserInformationControl.isAvailable(parent))
                     return new BrowserInformationControl(parent, shellStyle, style);
                 else
                     return new DefaultInformationControl(parent, shellStyle, style, new HTMLTextPresenter(false));
             }
         };
     }
 }
