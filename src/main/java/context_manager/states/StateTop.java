/**
 * 
 */
package context_manager.states;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import context_manager.ContextState;

/**
 * @author Steve Brown
 *
 * The top state in a chain of states.
 * The Context will always have one of these.
 */
public class StateTop extends State {
	
	public StateTop(ContextState cs, WebDriver driver) {
		super(cs, driver);
	}
	
	@Override
	public State getNextNewState() {		
		return super.next;
	}

	@Override
	public void close() {
		logger.debug("Cannot close state [" + this + "]");
	}

	@Override
	public State switchToMe() {
		logger.debug("Switching to top state");
		switchToDefaultContentAndThenElement(By.cssSelector("form[name='tb']"));		
		setCurrentContextToThisStatesContext();		
		return this;
	}
		
	@Override
	public boolean isContextCloser() {
		return true;
	}

	@Override
	public boolean isDefaultState() {
		return false;
	}
}
