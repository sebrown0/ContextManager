/**
 * 
 */
package context_manager;

import java.util.List;
import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.openqa.selenium.WebDriver;

import context_manager.contexts.Context;
import context_manager.states.State;
import context_manager.states.StateLeftMenu;
import context_manager.states.StateTopLeftNavBar;
import context_manager.states.StateTopRightNavBar;
import library.common.panels.JsPanel;
import library.common.panels.PanelSwitcher;


/**  
 * @author SteveBrown
 * @version 1.0
 * 	Initial
 * @since 1.0
 * 
 * Manages the contexts within the app.
 * 
 * TODO - Add garbage collector to remove 
 * missing elements, i.e. FormFadeShow.
 */
public class ContextManager implements CurrentContext {
	private ContextQueue queue = new ContextQueue();
	private WebDriver driver;
	private StateManager stateManager;
	
	// The state that initiates the context, i.e. LeftMenu
	// This has to be added to the firstState as the next state.	
	private CallingState callingState;	
	
	public ContextManager(WebDriver driver) {
		this.driver = driver;
		this.stateManager = new StateManager(this);
	}		
	
	/*
	 * Context Start
	 */
	public void setFirstContext(Context context) {
		queue.addContextToQueue(context);
	}			
	/*
	 * Context End
	 */
	 
	/*
	 * State - Start
	 */
	public ContextManager setNextState(State state) {		
		stateManager.setNextState(state);
		return this;
	}
	
	public Optional<ContextState> getPenultimateContext() {
		return queue.getPenultimate();
	}
	
	public void switchToDefaultStateInContext(ContextState cs) {
		stateManager.switchToDefaultStateInContext(cs);
	}
	
	public void switchToDefaultStateInCurrentContext() {
		stateManager.switchToDefaultStateInCurrentContext();
	}
	
	public <T extends State> Optional<State> switchToStateInCurrentContext(Class<T> clazzRequiredState) {
		return stateManager.switchToStateInCurrentContext(clazzRequiredState);
	}
	
	public void switchToLeftMenu() {
		Optional<ContextState> first = getContextThatIsFirstContext();
		first.ifPresentOrElse(f -> {			 		
			stateManager.switchToStateInContext(StateLeftMenu.class, f);	
			/*
			 * If problems are encountered put a wait in here.
			 * DriverWait.getElementAfterWait(driver, By.id("#nav-accordion"), Duration.ofNanos(1));
			 */
		},
			new Runnable() {				
				@Override
				public void run() {					
					System.out.println("CM -> switchToLeftMenu -> LOAD NEW CONTEXT *NOT IMPLENTED*"); // TODO - remove or log
					LogManager.getLogger().error("CM -> switchToLeftMenu -> LOAD NEW CONTEXT *NOT IMPLENTED*");
				}
		});
	}
	
	public void switchToTopRightNavBar() {
		Optional<ContextState> first = getContextThatIsFirstContext();
		first.ifPresentOrElse(f -> {			 		
			stateManager.switchToStateInContext(StateTopRightNavBar.class, f);	
		},
			new Runnable() {				
				@Override
				public void run() {					
					System.out.println("CM -> switchToTopRightNavBar -> LOAD NEW CONTEXT *NOT IMPLENTED*"); // TODO - remove or log
					LogManager.getLogger().error("CM -> switchToTopRightNavBar -> LOAD NEW CONTEXT *NOT IMPLENTED*");
				}
		});
	}
	public void switchToTopLeftNavBar() { //TODO - CHANGE THIS TO BRAND. IT'S NOT THE NAV BAR.
		Optional<ContextState> first = getContextThatIsFirstContext();
		first.ifPresentOrElse(f -> {			 		
			stateManager.switchToStateInContext(StateTopLeftNavBar.class, f);	
		},
			new Runnable() {				
				@Override
				public void run() {					
					System.out.println("CM -> switchToTopRightNavBar -> LOAD NEW CONTEXT *NOT IMPLENTED*"); // TODO - remove or log
					LogManager.getLogger().error("CM -> switchToTopRightNavBar -> LOAD NEW CONTEXT *NOT IMPLENTED*");
				}
		});
	}
	
	public <T extends JsPanel> void switchToExistingPanel(Class<T> panel) {
		PanelSwitcher<T> switcher = new PanelSwitcher<>(panel, this);
		switcher.switchToExistingPanel();
	}
		
	public <T extends State> void moveToDefaultStateInContext(ContextState cs) {	
		stateManager.moveToDefaultStateInContext(cs);
	}

	public void switchToFirstStateInCurrentContext() {
		stateManager.switchToFirstStateInCurrentContext();
	}
	
	public State moveToNextStateInCurrentContext(){
		return stateManager.moveToNextStateInCurrentContext();
	}
		
	public boolean isCurrentContext(ContextState cs) {
		return cs.equals(getCurrentContext());
	}
	
	public boolean isStateInCurrentContext(Class<?> clazz) {
		return stateManager.isStateInCurrentContext(clazz, this);
	}
	
	public void closeCurrentStateInCurrentContext(){
		stateManager.closeCurrentStateInCurrentContext(this);
	}
		
	public void deleteCurrentContextAndRevertToCallingContext() {		
		Optional<State> callingState = queue.removeContextAndGetCallingState(getCurrentContext());
		callingState.ifPresent(s -> {
			ContextState callingContext = s.getMyContext();
			callingContext.moveToState(s.getClass());
			//Closing current so need to go back to default state.			
			driver.switchTo().defaultContent();			
			s.switchToMe();
			revertToPrevCallingState(callingContext);
		});		
	}
	private void revertToPrevCallingState(ContextState inContext) {
		queue.moveToExistingContext(inContext);
	}
	/*
	 * State - End
	 */
		
	// Helpers
	private void logInvalidContextIfNull(ContextState contextState) {
		if(contextState == null) {
			LogManager.getLogger().error("Context is null");
		}
	}
	
	/*
	 * Getters / Setters
	 */		
	public void setContext(ContextState contextState) {		 	 	
		queue.addContextToQueue(contextState);
	}
	
	public WebDriver getDriver() {
		return driver;
	}
	public void setLatestCallingStateToCurrent() {
		State current = getCurrentContext().getState();
		if(current instanceof CallingState) {
			this.callingState = (CallingState) current;	
		}		
	}
	public void setLatestCallingState(CallingState callingState) {
		this.callingState = callingState;
	}
	public CallingState getLatestCallingState() {
		return callingState;
	}
	
	// Context	
	public Optional<ContextState> getContextThatIsFirstContext() {
		ContextState curr = getCurrentContext();
		if(curr != null && !(curr instanceof FirstContext)){
			curr = null;
			for (ContextState cs : queue.getQueue()) {
				curr = cs;
				if(curr instanceof FirstContext){
					break;
				}
			}			
		}
		return Optional.ofNullable(curr);
	}
	
	public Optional<JsPanel> getContextThatIsPanel() {
		return queue.getNextContextThatIsJsPanel();
	}
	
	public String getExpectedNameOfCurrentContext() {
		ContextState cs = queue.getCurrentContextInQueue();
		return getExpectedName(cs);
	}
	private String getExpectedName(ContextState cs) {
		if(cs != null) {
			return cs.getContextId().getExpectedName();			
		}else {
			return null;
		}
	}
	public String getContextIdOfCurrentContext() {
		ContextState cs = queue.getCurrentContextInQueue();
		return getContextId(cs);
	}	
	private String getContextId(ContextState cs) {
		if(cs != null) {
			return cs.getContextId().getId();			
		}else {
			return null;
		}
	}
	
	public ContextState getLastContext() {
		ContextState cs = queue.getLastContextInQueue(); 
		logInvalidContextIfNull(cs);
		return cs;
	}
	public ContextState getCurrentContext() {
		ContextState cs = queue.getCurrentContextInQueue(); 
		logInvalidContextIfNull(cs);
		return cs;
	}

	public ContextManager moveToExistingContext(ContextState cs) {
		queue.moveToExistingContextAndDefaultState(cs);
		return this;
	}
	
	// Queue
	public ContextQueue getQueue() {
		return queue;
	}
	public Optional<ContextState> getPrevContext(ContextState curr){
		return queue.getPrev(curr);
	}
	public Optional<ContextState> findContext(Object obj) {
		return queue.findContext(obj);
	}
	public ContextState getEndOfQueue() {
		return queue.getLastContextInQueue();
	}

	// If the context has been closed in the DOM by other means
	public void removeContext(ContextState cs) {
		queue.removeContextAndGetCallingState(cs);		
	}
	/*
	 *  Removes the context from the queue and closes it in the DOM
	 *  
	 *  TODO - If the context has a associated context, 
	 *  i.e. modal form, close this as well.
	 */
	public void removeAndCloseContext(ContextState cs) {
		queue.removeAndCloseContext(cs);
	/*
	 *  WE ARE ALREADY SWITCHED TO PREV CONTEXT
	 *  IF GOING TO ANOTHER PANEL WILL HAVE TO LOAD ITS CONTEXT AS WELL
	 */
		revertToNextAvailablePanel(); 
	}
	public boolean removeLastContextFromQueue() {
		return queue.removeLastContext();
	}
	private void revertToNextAvailablePanel() {
		Optional<JsPanel> panel = queue.getNextContextThatIsJsPanel();
		panel.ifPresentOrElse(p -> {
			//THIS SHOULD BE SETTING THE CURRENT CONTEXT AND STATE
			this.switchToExistingPanel(p.getClass());
		}, 
			new Runnable() {						
				@Override
				public void run() {
					// SET TO FIRST STATE
				}
			});
		queue.getCurrentContextInQueue().switchToDefaultState();
	}
	
	public void printContexts() {
		ContextManagerPrinter printer = new ContextManagerPrinter(queue);
		printer.printContexts();
	}	
	public void printQueue() {
		ContextManagerPrinter printer = new ContextManagerPrinter(queue);
		printer.printQueue();
	}

	@Override
	public void setCurrentContextState(ContextState cs) {
		queue.moveToExistingContextAndDefaultState(cs);		
	}

	@Override
	public ContextState getCurrentContextState() {
		return queue.getCurrentContextInQueue();
	}

	@Override
	public List<ContextState> getContextQueue() {
		return queue.getQueue();
	}
	
}
