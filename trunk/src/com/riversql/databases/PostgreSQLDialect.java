
package com.riversql.databases;

public class PostgreSQLDialect 
    
    implements IDialect {


    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().toLowerCase().startsWith("postgresql")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}
    
   
}

