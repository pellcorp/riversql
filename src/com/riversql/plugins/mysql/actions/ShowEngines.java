
package com.riversql.plugins.mysql.actions;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class ShowEngines extends Show {

	public ShowEngines(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW ENGINES";
	}
}
