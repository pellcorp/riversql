
package com.riversql.plugins.mysql.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.riversql.sql.SQLConnection;

import org.json.JSONObject;

public class CreateDBDDL {

	private SQLConnection conn;
	private String name;
	private String collation;

	public CreateDBDDL(SQLConnection conn, String name, String collation) {
		this.conn=conn;
		this.name=name;
		this.collation=collation;
	}

	public JSONObject execute()throws Exception {
		String sql="CREATE DATABASE `"+name+"`";
		if(collation!=null && !collation.trim().equals("")){
			String query="SELECT character_set_name FROM information_schema.collations where collation_name =?";
			PreparedStatement ps = conn.prepareStatement(query);
			ps.setString(1,collation);
			ResultSet rs = ps.executeQuery();
			rs.next();
			String characterset = rs.getString(1);
			rs.close();
			ps.close();
			sql=sql+" DEFAULT CHARACTER SET "+characterset+" COLLATE "+collation;
		}
		JSONObject obj=new JSONObject();
		obj.put("string", sql);
		return obj;
	}

}
