/**
 * 
 */
package context_manager.states;

import java.util.Optional;

import org.openqa.selenium.WebDriver;

import context_manager.CallingState;
import context_manager.ContextState;
import library.common.interfaces.IFrame;

/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 */
public class StateIframe extends State implements CallingState {
	private IFrame iFrame;
	/*
	 *  Need to set this if closing the the context that this state is in.
	 *  Then IFrame will switch back to the default content, 
	 *  i.e. the iFrame or Panel/Form in the previous context.
	 */
	private boolean isAfterClose;
	
	public StateIframe(ContextState cs, IFrame iFrame, WebDriver driver) {
		super(cs, driver);
		this.iFrame = iFrame;
	}

	@Override
	public State getNextNewState() {
		return null;
	}

	@Override
	public void close() {
		logger.debug("Closing state [" + this + "]");
		switchToDefaultContent();
		moveToPrevOrDefaultState();
	}	
		
	private void moveToPrevOrDefaultState() {
		Optional<State> prev = this.getPrev();
		prev.ifPresentOrElse( p -> {
			myContext.setCurrentState(p);	
		}, 
			new Runnable() {				
				@Override
				public void run() {
					myContext.switchToDefaultState();
				}			
		});	
	}
	
	@Override
	public State switchToMe() {
		iFrame.switchUsingLocator(this);		
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
	public State getState(ContextState context) {
		return this;
	}

	public void setAfterClose(boolean isAfterClose) {
		this.isAfterClose = isAfterClose;
	}
	public boolean isAfterClose() {
		return isAfterClose;
	}
}
