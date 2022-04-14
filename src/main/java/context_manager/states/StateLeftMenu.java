/**
 * 
 */
package context_manager.states;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

import context_manager.ContextState;

/**
 * @author SteveBrown
 * @version 1.0
 * 	Initial
 * @version 1.1
 * 	Changed how the element is located.
 * @since 1.0
 *
 */
public class StateLeftMenu extends State {
	
	public StateLeftMenu(ContextState cs, WebDriver driver) {
		super(cs, driver);
	}

	@Override
	public State getNextNewState() {
		return super.next;
	}

	@Override
	public State switchToMe() {
		logger.debug("Switching to state [left menu]");
		switchToDefaultContentAndThenElement(By.cssSelector("div[class='app-body']"), By.id("nav-accordion"));
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

	@Override
	public void close() {
		//N/A
	}
}
