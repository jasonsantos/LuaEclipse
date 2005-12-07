package org.keplerproject.ldt.core;

import org.eclipse.core.resources.IResource;

/**
 * Lua File or Project interface
 * @author guilherme
 *
 */
public interface LuaElement
{
    public abstract IResource getUnderlyingResource();
}