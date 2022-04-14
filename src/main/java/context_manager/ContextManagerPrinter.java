/**
 * 
 */
package context_manager;

import java.util.Optional;

import context_manager.states.State;

/**
 * @author Steve Brown
 *
 */
public class ContextManagerPrinter {
	private ContextQueue queue;
	
	public ContextManagerPrinter(ContextQueue queue) {
		this.queue = queue;
	}

	public void printContexts() {
		for (ContextState cs : queue.getQueue()) {
			System.out.println("CONTEXT: " + cs.getContextId()); // TODO - remove or log			
		}
	}
	
	public void printQueue() {
		for (ContextState cs : queue.getQueue()) {
			System.out.println("CONTEXT: " + cs.getContextId()); // TODO - remove or log
			printStates(cs);
		}
	}
		
	public boolean printStates(ContextState cs) {		
		State start = cs.getTopState();
		Optional<State> s = Optional.ofNullable(start);
 
		while(s != null) {			
			if(s.isPresent()) {
				State temp = s.get(); 	
				System.out.println("State: " + temp.getClass().getSimpleName()); 
				s = temp.getCurrentNextState();
			}else {
				s = null;
			}			
		}
		return false;
	}
}
