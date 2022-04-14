/**
 * 
 */
package context_manager.contexts;

import context_manager.ContextIdGetter;
import library.common.forms.ContainerAction;
import library.dakar_hr.pages.homepage.CoreData;


/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 */
public final class ContextForm extends Context {	
	
	public ContextForm(CoreData coreData, ContextIdGetter idGetter, ContainerAction containerAction) {
		super(coreData, idGetter, containerAction); 	
	}
		
}
