/**
 * 
 */
package context_manager;

/**
 * @author Steve Brown
 *
 * Each context should have an ID.
 * This is the 
 * 	expected name of the context, i.e. "Employee Details".
 * And
 *  the rendered id of the context, i.e jsPanel-1
 *  
 */
public class ContextId {
	private String expectedName;
	private String actualId = null;
	private ContextMatch matchType;
	private boolean matchFound;
	
	public enum ContextMatch{ 
		NONE,
		STRING_CONTEXT_ID, STRING_EXPECTED, STRING_ACTUAL, 
		OBJ_EXACT, OBJ_CONTEXT_ID, OBJ_EXPECTED, OBJ_ACTUAL 
	}
	
	public ContextId(String expectedName, String actualId) {
		this.expectedName = expectedName;
		this.actualId = actualId;
	}
		
	public String getId() {
		return expectedName + ":" + actualId;
	}
	
	public String getExpectedName() {
		return expectedName;
	}
	public String getActualId() {
		return actualId;
	}
		
	@Override
	public boolean equals(Object obj) {
		matchFound = false;
		matchType = ContextMatch.NONE;
		
		if(obj == this) {
			matchType = ContextMatch.OBJ_EXACT;
			matchFound = true;
		}else	if(obj instanceof String) {
			checkForStringMatch((String) obj);
		}else if(obj instanceof ContextId){
			checkForObjectMatch((ContextId) obj);
		}else if(obj instanceof ContextState){
			ContextState cs = (ContextState) obj;
			checkForObjectMatch(cs.getContextId());
		}
		return matchFound;		
	}
	
	private void checkForStringMatch(String find) {
		if(getId().equalsIgnoreCase(find)) {
			matchType = ContextMatch.STRING_CONTEXT_ID;
			matchFound = true;
		}
		else if(expectedName.equalsIgnoreCase(find)) {
			matchType = ContextMatch.STRING_EXPECTED;
			matchFound = true;
		}else if (actualId.equalsIgnoreCase(find)) {
			matchType = ContextMatch.STRING_ACTUAL;
			matchFound = true;
		}
	}
	
	// TODO - we don't have case for getContextId() !! 
	private void checkForObjectMatch(ContextId find) {
		if(getId().equalsIgnoreCase(find.getId())){
			matchType = ContextMatch.OBJ_CONTEXT_ID;
			matchFound = true;
		}else if(expectedName.equalsIgnoreCase(find.getExpectedName())){
			matchType = ContextMatch.OBJ_EXPECTED;
			matchFound = true;
		}else if( actualId.equalsIgnoreCase(find.getActualId())) {
			matchType = ContextMatch.OBJ_ACTUAL;
			matchFound = true;
		}
	}
	

	public ContextMatch getMatch() {
		return matchType;
	}

	@Override
	public String toString() {
		return "ContextId [id = " + getId() + ", expectedName=" + expectedName + ", actualId=" + actualId + ", match=" + matchType + "]";
	}

}


//private void checkForClassMatch(Object obj) {
//System.out.println("->checkForClassMatch" ); // TODO - remove or log
////isContextIdGetter(obj);
//Optional<Class<ContextIdGetter>> idGetter = isContextIdGetter(obj);
//idGetter.ifPresent(getter -> {
//	System.out.println("------>" + getter); // TODO - remove or log 	
//	ContextIdGetter id =  getter.getClass();//.cast(obj);
//	if(expectedName.equalsIgnoreCase(id.getContextExpectedName())) {
//		matchType = ContextMatch.STRING_EXPECTED;
//		matchFound = true;
//	}
//});
//}

//@SuppressWarnings("unchecked")
////private boolean isContextIdGetter(Object obj) {
//private <T extends ContextIdGetter> Optional<Class<T>> isContextIdGetter(Object obj) {
////if(obj instanceof ContextIdGetter) {
////	System.out.println("->Yeeeeeeeeeeeeeeeeeee"); // TODO - remove or log 	
////	return true;
////}else {
////	System.out.println("->Nooooooooooooooooooo"); // TODO - remove or log
////	return false;
////}
//
////Class<T> x = (Class<T>) obj.getClass();
//Class<T> idGetter = null;
//try {
//	idGetter = (Class<T>) obj.getClass();
//} catch (Exception e) {		
//	System.out.println("->XXXXXXXXXXXXXXXXXXXXXXXXXXX" + e ); // TODO - remove or log 	
//}
//return Optional.ofNullable(idGetter);
//}
