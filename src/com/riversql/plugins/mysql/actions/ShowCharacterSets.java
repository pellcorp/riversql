
package com.riversql.plugins.mysql.actions;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class ShowCharacterSets extends Show {

	public ShowCharacterSets(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW CHARACTER SET";
	}
}
