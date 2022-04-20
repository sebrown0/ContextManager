/**
 * 
 */
package context_manager.contexts;

import context_manager.ContextIdGetter;
import core_data.CoreData;
import library.common.forms.ContainerAction;
import library.common.panels.JsPanelHeaderBar;


/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 */
public final class ContextPanel extends Context {

	public ContextPanel(CoreData coreData, ContextIdGetter idGetter, 
												JsPanelHeaderBar hdrBar, ContainerAction containerAction) {
		super(coreData, idGetter, containerAction);
	
	}	

}
