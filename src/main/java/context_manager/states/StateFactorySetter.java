/**
 * 
 */
package context_manager.states;

import org.openqa.selenium.WebDriver;

import context_manager.ContextManager;
import library.common.interfaces.IFrame;
import library.common.panels.JsPanelHeaderBar;

/**
 * @author Steve Brown
 *
 */
public interface StateFactorySetter {
	ContextManager getContextManager();
	WebDriver getWebDriver();
	JsPanelHeaderBar setJsPanelHeaderBar();
	IFrame getIFrame();	
}
