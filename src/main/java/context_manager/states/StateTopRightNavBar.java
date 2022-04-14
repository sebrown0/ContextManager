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
 * @since 1.0
 */
public class StateTopRightNavBar extends State {

	public StateTopRightNavBar(ContextState cs, WebDriver driver) {
		super(cs, driver);		
	}

	@Override
	public State getNextNewState() {
		return super.next;
	}

	@Override
	public State switchToMe() {
		logger.debug("Switching to state [top-right nav-bar]");
		switchToDefaultContentAndThenElement(By.cssSelector("body > form > header > ul.nav.navbar-nav.ml-auto"));
		setCurrentContextToThisStatesContext();		
		return this;
	}

	@Override
	public boolean isContextCloser() {
		return false;
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
