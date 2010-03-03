
package com.riversql.plugins.mysql.actions;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class ShowPrivileges extends Show {

	public ShowPrivileges(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW PRIVILEGES";
	}
}
