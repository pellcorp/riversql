
package com.riversql.plugins.mysql.actions;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class ShowProcesses extends Show {

	public ShowProcesses(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW FULL PROCESSLIST";
	}
}
