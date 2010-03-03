
package com.riversql.databases;


public class HADBDialect  implements IDialect {

   
    public boolean supportsDatabase(String databaseProductName,
                                   String databaseProductVersion) {
        if (databaseProductName == null) {
            return false;
        }
        String prodName = "sun java system high availability";
        if (databaseProductName.trim().toLowerCase().startsWith(prodName)) {
            // We don't yet have the need to discriminate by version.
            return true;
        }
        return false;
    }
    
}
