
package com.riversql.databases;


public class InformixDialect
        implements IDialect {

    public boolean supportsDatabase(String databaseProductName,
            String databaseProductVersion) {
        if (databaseProductName == null) {
            return false;
        }
        if (databaseProductName.toLowerCase().contains("informix")) {
            // We don't yet have the need to discriminate by version.
            return true;
        }
        return false;
    }

 

}
