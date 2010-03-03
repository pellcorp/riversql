
package com.riversql.databases;



public class H2Dialect 
implements IDialect {


    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().startsWith("H2")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}    
    
 
}

