
package com.riversql.databases;


/**
 * An extension to the standard Hibernate MySQL dialect
 */

public class HSQLDialect 
                         implements IDialect {
    

    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().startsWith("HSQL")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}    
    
   
}
