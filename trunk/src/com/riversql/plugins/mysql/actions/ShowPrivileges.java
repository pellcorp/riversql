
package com.riversql.plugins.mysql.actions;

import com.riversql.sql.SQLConnection;

public class ShowPrivileges extends Show {

	public ShowPrivileges(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW PRIVILEGES";
	}
}
