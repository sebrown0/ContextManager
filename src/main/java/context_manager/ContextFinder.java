package context_manager;

import java.util.Optional;

import library.common.forms.ContainerAction;
import library.common.panels.JsPanel;

//import library.common.forms.ContainerAction;
//import library.common.panels.JsPanel;

/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 */
public class ContextFinder {
	private ContextState currentContextState;
	private CurrentContext currentContext;
		
	public ContextFinder(CurrentContext currentContext) {
		this.currentContext = currentContext;
		this.currentContextState = currentContext.getCurrentContextState();
	}
	
	public Optional<JsPanel> getNextContextThatIsJsPanel(){
		Optional<JsPanel> result = Optional.empty();
		if(currentIsJsPanel()) {			
			result = Optional.ofNullable((JsPanel) currentContextState.getContinerAction());
		}else {
			result = getContextThatIsPanel();
		}
		return result;
	}
	private boolean currentIsJsPanel() {
		boolean result = false;		
		if(currentContextState != null) {
			ContainerAction c = currentContextState.getContinerAction();
			if(c instanceof JsPanel) {
				result = true;
			}
		}
		return result;
	}	
	public Optional<JsPanel> getContextThatIsPanel() {
		Optional<JsPanel> result = Optional.empty();
		ContainerAction curr = currentContextState.getContinerAction();
		if(!(curr instanceof JsPanel)){
			curr = null;
			for (ContextState cs : currentContext.getContextQueue()) {
				curr = cs.getContinerAction();
				if(curr instanceof JsPanel){
					result = Optional.ofNullable((JsPanel) curr); 
					break;
				}
			}			
		}else {
			result = Optional.of((JsPanel)curr);
		}
		return result;
	}
}
