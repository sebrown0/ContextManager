/**
 * 
 */
package context_manager;

import java.util.Optional;

import context_manager.states.State;

/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 */
public class ContextUpdater {
	private StateManager manager;

	public ContextUpdater(StateManager manager) {
		this.manager = manager;
	}

	public void updateContextAfterStateDeletion(State state) {
		ContextState statesContext = state.getMyContext();
		if(state.isContextCloser()) { 	
			manager.getLogger().debug("State [" + state + "] is a context closer so will close current context");
			/*
			 * This does not call the state's close method.
			 * That is done before in: StateManager.closeState(State closeState); 
			 */
			manager.getQueue().removeContextAndReset(statesContext);
			manager.setDefaultStateAfterClosingContext();
		}else {
			Optional<State> prev = state.getPrev();
			if(prev != null && prev.isPresent()) {
				manager.revertToPreviousStateInContext(prev, statesContext);
			}else {
				manager.getQueue().removeContextAndReset(statesContext);
			}			
		}
	}
}
