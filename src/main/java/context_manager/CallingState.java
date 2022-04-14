/**
 * 
 */
package context_manager;

import context_manager.states.State;

/**
 * @author SteveBrown
 *
 */
public interface CallingState {
	State getState(ContextState context);	
}
