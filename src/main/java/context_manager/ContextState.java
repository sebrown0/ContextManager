/**
 * 
 */
package context_manager;

import java.util.Optional;

import context_manager.states.State;
import context_manager.states.StateFactorySetter;
import library.common.forms.ContainerAction;


/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 * Manipulate states within a Context.
 */
public interface ContextState {
	void setNullState();
	void setCurrentState(State currentState);	
	void setState(State state);
	void moveNext();
	void driverSwitchToDefaultContent();
	void removeContextAndResetQueue();
	void switchToDefaultState();
	void moveToDefaultState();
	
	State getState();
	State getFirstState();
	State getCallingState();
	State getTopState();
	State getLastStateCloser();
	State getContextCloser();
	
	<T extends State> Optional<State> moveToState(Class<T> clazzState);
	<T extends State> Optional<State> setLastState(Class<T> clazzState, StateFactorySetter factorySetter);

	boolean isStateInContext(Class<?> clazz);	
	Optional<State> getPreviousState();
	ContextId getContextId();
	ContainerAction getContinerAction();	
	ContextManager getContextManager();
}
