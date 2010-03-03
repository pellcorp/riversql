
package com.riversql.databases;


/**
 * An extension to the standard Hibernate dialect
 * 
 */
public class FrontBaseDialect 
                             implements IDialect {

    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().startsWith("FrontBase")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}        
    
    
}
