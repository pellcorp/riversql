
package com.riversql.databases;



public class MAXDBDialect 
                          implements IDialect {
   
    

    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	String lname = databaseProductName.trim().toLowerCase();
    	if (lname.startsWith("sap") || lname.startsWith("maxdb")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}        

  
    
}
