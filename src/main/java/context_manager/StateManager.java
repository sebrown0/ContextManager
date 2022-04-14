/**
 * 
 */
package context_manager;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import context_manager.states.State;

/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 */
public class StateManager {
	private Logger logger = LogManager.getLogger();
	private ContextManager manager;
	private ContextQueue queue;

	public StateManager(ContextManager manager) {
		this.manager = manager;
		this.queue = manager.getQueue();
	}

	public void switchToFirstStateInCurrentContext() {
		ContextState contextState = queue.getLastContextInQueue();
		if(contextState != null) {
			contextState.getFirstState().switchToMe();
		}else {
			logger.debug("Context state is null"); 	
		}			
	}
	
	public <T extends State> Optional<State> switchToStateInCurrentContext(Class<T> clazzRequiredState) {
		String requiredStateName = clazzRequiredState.getSimpleName();		
		if(isCurrentStateRequiredState(requiredStateName)) {
			logger.debug("State [" + requiredStateName + "] is current state in context");
			return Optional.ofNullable(manager.getLastContext().getState());
		}else {
			Optional<State> state = moveToStateInCurrentContext(clazzRequiredState, manager);
			state.ifPresent(s -> s.switchToMe());
			return state;
		}				
	}
	
	public <T extends State> Optional<State> switchToStateInContext(Class<T> clazzRequiredState, ContextState findCs) {
		Optional<State> state = Optional.empty();		
		state = moveToStateInContext(clazzRequiredState, findCs);		
		state.ifPresent(s -> s.switchToMe());
		return state;
	}
	
	public <T extends State> Optional<State> moveToStateInCurrentContext(Class<T> clazzRequiredState, CurrentContext getter) {
		return moveToStateInContext(clazzRequiredState, getter.getCurrentContextState());			
	}
	public <T extends State> Optional<State> moveToStateInContext(Class<T> clazzRequiredState, ContextState cs) {
		if(cs != null) {
			String requiredStateName = clazzRequiredState.getSimpleName();
			Optional<State> state = cs.moveToState(clazzRequiredState);
			
			if(state.isPresent()) {			
				logger.debug("State [" + requiredStateName + "] is present in context. Will move to this state");				
				return state;			
			}else {
				logger.debug("State [" + requiredStateName + "] is not present in context. Adding as the last state in context");				
				return cs.setLastState(clazzRequiredState, cs.getContinerAction().getStateFactorySetter());			
			}		
		}else {
			logger.error("Cannot move to a state in a null context");
			return Optional.empty();
		}							
	}
		
	private boolean isCurrentStateRequiredState(String requiredStateName) {
		String currentStatesName = manager.getCurrentContextState().getState().getClass().getSimpleName(); 	
		return currentStatesName.equals(requiredStateName);
	}
		
	public State moveToNextStateInCurrentContext(){
		ContextState contextState = queue.getCurrentContextInQueue();
		contextState.moveNext();
		return contextState.getState();
	}
	
	public void setNextState(State state) {		
		State current = queue.getLastContextInQueue().getState();
		current.setNext(Optional.ofNullable(state));
	}

	public void setDefaultStateAfterClosingContext() {				

		ContextState cs = queue.getCurrentContextInQueue(); 
		State defaultState = getDefaultState(cs);
		if(defaultState != null) { 	
			logger.debug("Current context is now [" + cs.getContextId() + "]. State (default) is [" + defaultState + "]");
			cs.setCurrentState(defaultState);
		}else { 	
			logger.debug("Current context is now [" + cs.getContextId() + "]. Default state not found so state is current [" + cs.getState() + "]");
		} 	
	}
	
	public void moveToDefaultStateInContext(ContextState cs) {		
		State defaultState = getDefaultState(cs);
		if(defaultState != null) { 	
			logger.debug("Switched to default state [" + defaultState + "]");
			cs.setCurrentState(defaultState);
		}else { 	
			State curr = cs.getState();			
			logger.debug("Default state not found so state is current [" + curr + "]");
			cs.setCurrentState(curr);
		} 	
	}
	
	public void switchToDefaultStateInContext(ContextState cs) {		
		State defaultState = getDefaultState(cs);
		if(defaultState != null) { 	
			logger.debug("Switched to default state [" + defaultState + "]");
			cs.setCurrentState(defaultState);
			defaultState.switchToMe();
		}else { 	
			State curr = cs.getState();			
			logger.debug("Default state not found so state is current [" + curr + "]");
			cs.setCurrentState(curr);
		} 	
	}
	
	public void switchToDefaultStateInCurrentContext() {				
		ContextState cs = queue.getCurrentContextInQueue(); 
		switchToDefaultStateInContext(cs);
	}
	
	public boolean isStateInCurrentContext(Class<?> clazz, CurrentContext getter) {
		return getter.getCurrentContextState().isStateInContext(clazz);
	}
	
	private State getDefaultState(ContextState cs) {				
		State start = cs.getTopState();
		State defaultState = goForwardThruStates(start); 	
		return defaultState;
	}
	
	private State goForwardThruStates(State s) {
		boolean foundDefault = false;
		
		while (foundDefault == false && s != null && s.getCurrentNextState() != null && s.isDefaultState() == false) {
			if(s.getCurrentNextState().isPresent()) {				
				s = s.getCurrentNextState().get();
			}else {
				s = null;
			}
		} 	
		return s;
	}
	
	public void closeCurrentStateInCurrentContext(CurrentContext getter){
		ContextState cs = getter.getCurrentContextState();
		if(cs != null) {
			closeStateAndUpdateContext(cs.getState());
		}else {
			logger.debug("No context so cannot close current state");
		}		
	}
	
	private void closeStateAndUpdateContext(State state){		
		closeState(state);
		updateContextAfterStateClosure(state);
	}

	private void closeState(State closeState) {
		logger.debug("Closing state [" + closeState + "]"); 	
		closeState.close();//err
	}
	
	private void updateContextAfterStateClosure(State state) {
		ContextUpdater updater = new ContextUpdater(this);
		updater.updateContextAfterStateDeletion(state);
	}
	
	public void revertToPreviousStateInContext(Optional<State> prev, ContextState cs) {
		if(cs != null) {
			prev.ifPresentOrElse(
				p -> {					
						logger.debug("Trying to revert to prev state [" + p + "] in context [" + cs + "]");
						cs.setState(p);					
				}, 
				new Runnable() {			
					@Override
					public void run() {
						logger.debug("No prev state so setting current state of context [" + cs + "] to null");
						cs.setNullState();				
					}
			});
		}else {
			logger.error("Cannot revert to prev state because context is null");
		}					
	}
	
	public void revertToPreviousStateInCurrentContext(Optional<State> prev) {
		prev.ifPresentOrElse(
				p -> { 
					manager.getCurrentContextState().setState(p);
				}, 
				new Runnable() {			
					@Override
					public void run() {
						queue.getLastContextInQueue().setNullState();				
					}
		});
	}

//	private ContextState getCurrentContext() {
//		return manager.getCurrentContext();
//	}
	
	//Getters and Setters
	public Logger getLogger() {
		return logger;
	}

	public ContextQueue getQueue() {
		return queue;
	}
}
