
package com.riversql.plugins.mysql.actions;

import com.riversql.sql.SQLConnection;

public class ShowCollations extends Show {

	public ShowCollations(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW COLLATION";
	}
}
