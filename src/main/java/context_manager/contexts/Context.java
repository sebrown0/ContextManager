/**
 * 
 */
package context_manager.contexts;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import context_manager.ContextId;
import context_manager.ContextIdGetter;
import context_manager.ContextManager;
import context_manager.ContextState;
import context_manager.states.State;
import context_manager.states.StateFactory;
import context_manager.states.StateFactorySetter;
import context_manager.states.StateTop;
import core_data.CoreData;
import library.common.forms.ContainerAction;

/**
 * @author SteveBrown
 * @version 1.0
 * 	Initial
 * @since 1.0
 *
 * Represents a context in the app, i.e. a panel, form etc.
 * Each context can have different states, i.e. header/container, iFrame etc.
 * 
 */
public abstract class Context implements ContextState { 
	protected Logger logger = LogManager.getLogger();
	
	private ContextManager contextManager;
	private ContextId contextId;
	private ContainerAction containerAction;
	private State firstState;
	private State callingState;
	private State currentState;
	private State searchedState;
		
	public Context(CoreData coreDate, ContextIdGetter idGetter, ContainerAction containerAction) {
		this.contextManager = coreDate.getContextManager();		
		this.containerAction = containerAction;
		
		setContextId(idGetter);
		setFirstState();
		setCallingState();
	}

	public void setContextId(ContextIdGetter idGetter) {
		contextId = idGetter.getContextId();
	}
	
	private void setFirstState() { 		
		this.setState(new StateTop(this, contextManager.getDriver()));
		firstState = currentState;	
	}
		
	private void setCallingState() {
		callingState = contextManager.getLatestCallingState().getState(this);
	}
		
	@Override
	public ContainerAction getContinerAction() {
		return containerAction;
	}
		
	@Override
	public void setCurrentState(State currentState) {
		this.currentState = currentState;
	}
		
	@Override
	public <T extends State> Optional<State> setLastState(Class<T> clazz, StateFactorySetter factorySetter){				
		getNewInstanceOfState(clazz, factorySetter).ifPresentOrElse(s -> 
				{ this.setState(s); 
					logger.debug("Set last (and current state) to [" + s +"]");
				}, 
				new Runnable() {					
					@Override
					public void run() {
						logger.error("Could not set last state to [" + clazz.getSimpleName() +"]");	
					}
				});
		
		return Optional.ofNullable(currentState);
	}

	private <T extends State> Optional<State> getNewInstanceOfState(Class<T> clazzState, StateFactorySetter factorySetter) {
		StateFactory factory = new StateFactory(factorySetter);
		return factory.getNewInstanceOfState(clazzState, this);
	}
	
	/*
	 * is setting to left menu
	 */
	@Override
	public void setState(State newState) {
		setCurrentsNextToNewState(newState);
		setNewStatesPrevToCurrent(newState);		
		logger.debug("Setting new state [" + newState.toString() + "]"); 	
	}	
	
	private void setCurrentsNextToNewState(State state) {
		if(currentState != null) {
			currentState.getCurrentNextState().ifPresentOrElse(
					nxt -> {
						if(!nxt.equals(state)) {
							currentState.setNext(Optional.ofNullable(state));
						}	
					}, 
					new Runnable() {						
						@Override
						public void run() {
							currentState.setNext(Optional.ofNullable(state));							
						}
					}					
			);
		}
	}
		
	private void setNewStatesPrevToCurrent(State state) {
		State temp = currentState; 
		currentState = state; 
		currentState.setPrev(Optional.ofNullable(temp));	
	}
	
	@Override
	public void removeContextAndResetQueue() {
		contextManager.removeAndCloseContext(this);
	}
	
	@Override
	public boolean isStateInContext(Class<?> clazz) {
		String stateName = clazz.getSimpleName();		
		State start = getTopState();
		Optional<State> s = Optional.ofNullable(start);
		
		while(s != null) {			
			if(s.isPresent()) {
				State temp = s.get(); 	
				if(temp.getClass().getSimpleName().equals(stateName)) {
					searchedState = temp;
					return true;
				}
				s = temp.getCurrentNextState();
			}else {
				s = null;
			}			
		}
		return false;
	}
	
	@Override
	public State getContextCloser() {
		return getLastStateCloser();
	}

	@Override
	public State getLastStateCloser() {
		State top = getTopState();
		State s = top;
		State closer = null;

		while (s != null) {
			Optional<State> next = s.getCurrentNextState();
			if(next.isPresent()) {
				
				if(next.get().isContextCloser()) {
					closer = next.get();					
				}
				s = next.get();
			}else {
				s = null;
			}	
		}	
		return closer;
	}

	@Override
	public State getTopState() {
		return firstState;
	}
	
	@Override
	public State getState() {
		return currentState;
	}
	
	@Override
	public <T extends State>  Optional<State> moveToState(Class<T> clazz) {
		if(isStateInContext(clazz)) {
			currentState = searchedState;
			return Optional.of(currentState);
		}
		return Optional.empty();
	}

	@Override
	public State getCallingState() {
		return callingState;
	}

	@Override
	public Optional<State> getPreviousState() {
		return currentState.getPrev();
	}

	@Override
	public void moveNext() {
		Optional<State> next = Optional.ofNullable(currentState.getNextNewState());
		next.ifPresentOrElse(n -> {	
			this.setState(n); 
			}, 
			new Runnable() {				
				@Override
				public void run() {
					logger.debug("No next state for [" + currentState.toString() + "]");
				}
			});		 	
	}

	@Override
	public void moveToDefaultState() { 
		contextManager.moveToDefaultStateInContext(this);
	}
	
	@Override
	public void switchToDefaultState() {
		contextManager.switchToDefaultStateInContext(this);		
	}

	@Override
	public void driverSwitchToDefaultContent() {
		contextManager.getDriver().switchTo().defaultContent();
	}

	@Override
	public void setNullState() {
		currentState = null;		
	}

	@Override
	public State getFirstState() {
		return firstState;
	}

	@Override
	public ContextId getContextId() {
		return contextId;
	}
	
	@Override
	public ContextManager getContextManager() {
		return contextManager;
	}

	@Override
	public String toString() {
		return "Context [contextId=" + contextId + "]";
	}
		
}

