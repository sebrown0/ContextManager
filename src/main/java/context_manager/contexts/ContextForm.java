/**
 * 
 */
package context_manager.contexts;

import context_manager.ContextIdGetter;
import core_data.CoreData;
import library.common.forms.ContainerAction;


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
