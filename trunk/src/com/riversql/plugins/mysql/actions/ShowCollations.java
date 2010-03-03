
package com.riversql.plugins.mysql.actions;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class ShowCollations extends Show {

	public ShowCollations(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW COLLATION";
	}
}
