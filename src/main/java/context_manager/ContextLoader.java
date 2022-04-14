/**
 * 
 */
package context_manager;

import java.util.Optional;

import library.common.forms.ContainerAction;

/**
 * @author Steve Brown
 *
 */
public class ContextLoader {
	private ContextManager contextManager;
	
	public ContextLoader(ContextManager contextManager) {
		this.contextManager = contextManager;	
	}
	
	public void setContainerItemAsCurrentContext(Optional<ContainerAction> item) {
		item.ifPresent(itm -> {
			ContextState cs = itm.getMyContext();
			if(cs != null) {
				contextManager.moveToExistingContext(cs);	
			}				
		});
	}	

}
