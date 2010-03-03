
package com.riversql.databases;



public interface IDialect {

    boolean supportsDatabase(String databaseProductName, 
    					    String databaseProductVersion);
    
}
