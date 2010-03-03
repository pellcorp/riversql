
package com.riversql.plugins.mysql.actions;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class ShowDatabases extends Show {

	public ShowDatabases(SQLConnection conn) {
		super(conn);
		
	}

	@Override
	public String getShowString() {
		
		return "select schema_name as 'Database', default_collation_name as 'Collation' from information_schema.schemata";
	}

}
