
package com.riversql.plugins.mysql.actions;

import com.riversql.sql.SQLConnection;

public class ShowCharacterSets extends Show {

	public ShowCharacterSets(SQLConnection conn) {
		super(conn);
	}

	@Override
	public String getShowString() {
		return "SHOW CHARACTER SET";
	}
}
