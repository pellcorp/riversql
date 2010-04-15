
package com.riversql.plugins.mysql.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.riversql.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GetCollations {

	private SQLConnection conn;

	public GetCollations(SQLConnection conn) {
		this.conn=conn;
	}


	public JSONObject execute() throws JSONException {
		JSONArray arr=new JSONArray();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			ps=conn.prepareStatement("select collation_name,character_set_name from information_schema.collations order by character_set_name asc, collation_name asc");
			rs=ps.executeQuery();
			
			
			JSONObject record=new JSONObject(); 
			
			record.put("collation_name","");
			record.put("character_set_name","");
			arr.put(record);
			
			while(rs.next()){
				record=new JSONObject(); 
				
				record.put("collation_name",rs.getString(1));
				record.put("character_set_name",rs.getString(2));
				arr.put(record);
			}
		} catch (SQLException e) {
			//e.printStackTrace();
		}finally{
			try {if(rs!=null)
				rs.close();
			} catch (SQLException e) {
			}
			try {
				if(ps!=null)
					ps.close();
			} catch (SQLException e) {
			}
		}
		
		JSONObject ret = new JSONObject();
		ret.put("collations", arr);
		return ret;
	}

}
