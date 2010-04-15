
package com.riversql.plugins.mysql.actions;

import com.riversql.sql.SQLConnection;

public class ShowVariables extends Show {

	public ShowVariables(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "show variables";
	}
}
