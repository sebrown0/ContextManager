/**
 * 
 */
package context_manager;

import java.util.List;

/**
 * @author SteveBrown
 * @version 1.0
 * @since 1.0
 *
 */
public interface CurrentContext {
	ContextState getCurrentContextState();
	List<ContextState> getContextQueue();
	void setCurrentContextState(ContextState cs);
}
