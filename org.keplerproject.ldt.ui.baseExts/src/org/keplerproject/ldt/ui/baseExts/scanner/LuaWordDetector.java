package org.keplerproject.ldt.ui.baseExts.scanner;

import org.eclipse.jface.text.rules.IWordDetector;

public class LuaWordDetector
    implements IWordDetector, ILuaSyntax
{

    public LuaWordDetector()
    {
    }

    public boolean isWordStart(char c)
    {
    	
    	for(int i = 0; i < ILuaSyntax.reservedwords.length; i++)
            if(ILuaSyntax.reservedwords[i].charAt(0) == c)
                return true;

        for(int i = 0; i < ILuaSyntax.constants.length; i++)
            if(ILuaSyntax.constants[i].charAt(0) == c)
                return true;
        return false;
    }

    public boolean isWordPart(char c)
    {
         	
    	for(int i = 0; i < ILuaSyntax.reservedwords.length; i++)
            if(ILuaSyntax.reservedwords[i].indexOf(c) != -1)
                return true;

        for(int i = 0; i < ILuaSyntax.constants.length; i++)
            if(ILuaSyntax.constants[i].indexOf(c) != -1)
                return true;
       
        return false;
    }
}