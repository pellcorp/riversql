
package com.riversql.plugins.mysql.actions;

import com.riversql.sql.SQLConnection;

public class ShowEngines extends Show {

	public ShowEngines(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW ENGINES";
	}
}
