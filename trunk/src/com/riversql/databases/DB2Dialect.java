
package com.riversql.databases;


/**
 * An extension to the standard Hibernate DB2 dialect
 */
public class DB2Dialect 
                        implements IDialect {

    public boolean supportsDatabase(String databaseProductName, 
								   String databaseProductVersion) 
	{
    	if (databaseProductName == null) {
    		return false;
    	}
    	if (databaseProductName.trim().startsWith("DB2")) {
    		// We don't yet have the need to discriminate by version.
    		return true;
    	}
		return false;
	}    
}
