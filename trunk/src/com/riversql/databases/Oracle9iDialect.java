
package com.riversql.databases;



/**
 * A description of this class goes here...
 */

public class Oracle9iDialect 
                             implements IDialect {


    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().toLowerCase().startsWith("oracle")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}

}
