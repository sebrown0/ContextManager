/**
 * 
 */
package context_manager.states;

import java.util.Optional;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.openqa.selenium.WebDriver;

import context_manager.ContextState;
import library.common.interfaces.IFrame;
import library.common.panels.JsPanelHeaderBar;

/**
 * @author Steve Brown
 *
 */
public class StateFactory {
//	private ContextManager manager;
	private WebDriver driver;
	private JsPanelHeaderBar bar;
	private IFrame iFrame;
	
	private State state;
	private String clazzName;
	private Logger logger = LogManager.getLogger();
		
	public StateFactory(StateFactorySetter setter) {
//		this.manager = setter.getContextManager();
		this.driver = setter.getWebDriver();
		this.bar = setter.setJsPanelHeaderBar();
		this.iFrame = setter.getIFrame();
	}

	public <T extends State> Optional<State> getNewInstanceOfState(Class<T> clazzState, ContextState cs) {
		clazzName = clazzState.getSimpleName();
		logger.debug("Getting instance of [" + clazzName +"]");
		if(clazzName.equals("StateLeftMenu")) {				
			state = new StateLeftMenu(cs, driver);
		}else if (clazzName.equals("StateHeaderForm")) {
//			state = new StateHeaderForm(manager, null, null, driver);
			logger.error("*** NOT IMPLEMENTED ***");
			System.out.println("*** NOT IMPLEMENTED ***"); // TODO - remove or log 	
		}else if (clazzName.equals("StateHeaderPanel")) {
			state = new StateHeaderPanel(cs, bar, iFrame, driver);
		}else if (clazzName.equals("StateIframe")) {
			state = new StateIframe(cs, null, driver);
		}else if (clazzName.equals("StateModule")) {
			state = new StateModule(cs, driver);
		}else if (clazzName.equals("StateTop")) {
			state = new StateTop(cs, driver);
		}else if (clazzName.equals("StateTopRightNavBar")) {
			state = new StateTopRightNavBar(cs, driver);
		}else if (clazzName.equals("StateTopLeftNavBar")) {
			state = new StateTopLeftNavBar(cs, driver);
		}							
		
		return Optional.ofNullable(state);
	}
	
}

