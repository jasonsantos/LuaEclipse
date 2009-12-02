package org.keplerproject.ldt.ui.editors.outline;

import org.eclipse.jface.viewers.LabelProvider;
import org.eclipse.swt.graphics.Image;
import org.keplerproject.ldt.ui.LDTUIPlugin;

public class LuaOutlineLabelProvider extends LabelProvider {
	public static String FUNCTION_ICON_PATH = "icons/lua_function.gif";
	Image functionImage = null;
	
	public Image getImage(Object element) {
		if(functionImage == null) {
			functionImage = LDTUIPlugin.getImageDescriptor(FUNCTION_ICON_PATH).createImage();			
		}
		return functionImage;
	}
	
	public String getText(Object element) {
		return super.getText(element);
	}
		
	public boolean isLabelProperty(Object element, String property) {
		if(LuaOutlineContentProvider.OFFSET_PROPERTY.equals(property)) {
			return false;
		}
        return super.isLabelProperty(element, property);
    }
	
	public void dispose() {
		if(functionImage != null) {
			functionImage.dispose();
			functionImage = null;
		}
	}
}
