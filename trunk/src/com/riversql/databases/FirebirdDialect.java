
package com.riversql.databases;


/**
 * An extension to the standard Hibernate Firebird dialect
 */
public class FirebirdDialect 
                             implements IDialect {
    

    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().startsWith("Firebird")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}    
    
}
