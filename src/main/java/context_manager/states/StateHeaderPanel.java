/**
 * 
 */
package context_manager.states;

import org.openqa.selenium.WebDriver;

import context_manager.ContextState;
import library.common.interfaces.IFrame;
import library.common.panels.JsPanelHeaderBar;

/**
 * @author Steve Brown
 *
 */
public class StateHeaderPanel extends State {
	private JsPanelHeaderBar bar;
	private IFrame iFrame;
	
	public StateHeaderPanel(ContextState cs, JsPanelHeaderBar bar, IFrame iFrame, WebDriver driver) {
		super(cs, driver);
		this.bar = bar;
		this.iFrame = iFrame;
	}
	
	@Override
	public State getNextNewState() {
		return new StateIframe(myContext, iFrame, driver);
	}
	
	@Override
	public void close() { 	
		logger.debug("Closing state [" + this + "]");		
		switchToDefaultContent();		
		bar.getControlBar().clickClose();		
	}
	
	@Override
	public State switchToMe() {
		//Done by JsPanelToolBar
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
