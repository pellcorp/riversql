/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.plugins.mysql.actions;

import com.riversql.plugin.BasePluginType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import com.riversql.sql.SQLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author river.liao
 */
public class GetUserPrivileges {
        private SQLConnection conn;
        private BasePluginType node;
	public GetUserPrivileges(SQLConnection conn, BasePluginType node) {
		this.conn=conn;
                this.node=node;
	}

        public final JSONObject execute() throws Exception{

		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();

		String sql="SELECT table_schema,privilege_type FROM information_schema.SCHEMA_PRIVILEGES where grantee='"+node.getName()+"' and is_grantable='YES' order by table_schema,privilege_type;";
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			ps=(PreparedStatement) conn.prepareStatement(sql);
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
