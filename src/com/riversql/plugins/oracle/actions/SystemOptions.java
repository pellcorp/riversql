
package com.riversql.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.dbtree.DatabaseNode;


public class SystemOptions {

	private String id;

	public SystemOptions(String id) {
		this.id=id;
	}

	public JSONObject execute()throws Exception {
		JSONObject results=new JSONObject();
		DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
		
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		
		String []strs={"Property","Value"};
		
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		String sql="select parameter, value from sys.v_$option";
		PreparedStatement ps = null;
		ResultSet rs=null;
		try {
			ps=dn.getConn().prepareStatement(sql);
			rs=ps.executeQuery();
			while(rs.next()){
				JSONArray record=new JSONArray();
				record.put(rs.getString(1));
				record.put(rs.getString(2));
				data.put(record);
			}
		} catch (SQLException e) {
			
		}finally{
			try {if(rs!=null)
				rs.close();
			} catch (SQLException e) {
			}
			try {if(ps!=null)
				ps.close();
			} catch (SQLException e) {
			}
		}
		
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}

}
