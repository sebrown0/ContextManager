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
 */
public class StateModule extends State {
	
	public StateModule(ContextState cs, WebDriver driver) {
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
		logger.debug("Switching to state [module]");
		switchToDefaultContentAndThenElement(By.cssSelector("body > form > header > div > a"));
		setCurrentContextToThisStatesContext();		
		return this;
	}

	@Override
	public boolean isContextCloser() {
		return true;
	}

	@Override
	public boolean isDefaultState() {
		return true;
	}
}
