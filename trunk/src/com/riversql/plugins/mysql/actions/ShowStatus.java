
package com.riversql.plugins.mysql.actions;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class ShowStatus extends Show {

	public ShowStatus(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "show status";
	}
}
