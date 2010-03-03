
package com.riversql.databases;

/**
 * An extension to the standard Derby dialect
 */
public class DerbyDialect 
                          implements IDialect {

    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().startsWith("Apache Derby")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}    
    
  
}
