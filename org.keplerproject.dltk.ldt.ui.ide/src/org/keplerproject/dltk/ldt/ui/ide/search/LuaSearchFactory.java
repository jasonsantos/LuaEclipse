package org.keplerproject.dltk.ldt.ui.ide.search;

import org.eclipse.dltk.core.search.AbstractSearchFactory;
import org.eclipse.dltk.core.search.IMatchLocatorParser;
import org.eclipse.dltk.core.search.matching.MatchLocator;

public class LuaSearchFactory extends AbstractSearchFactory {
 
	public IMatchLocatorParser createMatchParser(MatchLocator locator) {
		return new LuaMatchLocationParser(locator);
	}

}
