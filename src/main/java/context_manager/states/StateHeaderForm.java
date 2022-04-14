/**
 * 
 */
package context_manager.states;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import context_manager.ContextState;

/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 */
public class StateHeaderForm extends State {
	private WebElement myContainer;
	private By byHeaderLocator;
	
	public StateHeaderForm(ContextState cs, WebElement myContainer, By byHeaderLocator, WebDriver driver) {
		super(cs, driver);
		this.myContainer = myContainer;
		this.byHeaderLocator = byHeaderLocator;		
	}
		
	@Override
	public State getNextNewState() {
		return super.next;
	}	
	@Override
	public void close() {		
		logger.error("*** NOT IMPLEMENTED ***");
		System.out.println("StateHeaderForm->close->*** NOT IMPLEMENTED ***"); // TODO - remove or log				
	}
	@Override
	public State switchToMe() {
		logger.debug("Switch to [StateHeaderForm]");
		myContainer.findElement(byHeaderLocator);
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
