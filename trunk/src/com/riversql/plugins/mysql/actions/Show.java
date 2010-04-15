
package com.riversql.plugins.mysql.actions;


import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;

import com.riversql.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

public abstract class Show {
	private SQLConnection conn;
	public Show(SQLConnection conn) {
		this.conn=conn;
	}
	public abstract String getShowString();
	public final JSONObject execute() throws Exception{
		
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();

		String sql=getShowString();
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			ps=conn.prepareStatement(sql);
			rs=ps.executeQuery();
			ResultSetMetaData metars=rs.getMetaData();
			int icount=metars.getColumnCount();
			for(int i=0;i<icount;i++){
				meta.put(metars.getColumnLabel(i+1));
			}
			
			while(rs.next()){
				JSONArray record=new JSONArray();
				for(int i=0;i<icount;i++){
					record.put(rs.getString(i+1));
				}
				data.put(record);
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
		
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}
}
