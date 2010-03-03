
package com.riversql.databases;

public class SybaseDialect 
                           implements IDialect {    
        

    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	String lname = databaseProductName.trim().toLowerCase();
    	if (lname.startsWith("sybase") 
                || lname.startsWith("adaptive")
                || lname.startsWith("sql server")) 
        {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}

    
}


