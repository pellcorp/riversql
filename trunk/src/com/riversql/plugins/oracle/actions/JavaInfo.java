
package com.riversql.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.plugin.BasePluginType;
import com.riversql.plugins.oracle.JavaNode;


public class JavaInfo {
	String id;
	public JavaInfo(String id) {
		this.id=id;
	}

	public JSONObject execute()throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		String []strs={"Property","Value"};
		
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		Object obj=IDManager.get().get(id);
		JavaNode funz=(JavaNode)obj;
		final String sql=
			"SELECT  created,last_ddl_time, timestamp, status,DBMS_JAVA.LONGNAME(object_name) FROM sys.all_objects where owner=? and object_type='JAVA SOURCE' and object_name=?";
		
		ResultSet rs=null;
		PreparedStatement ps=null;
  		try{
			ps=funz.getConn().prepareStatement(sql);
			BasePluginType tot=(BasePluginType)funz.getParent();
			String owner=tot.getParent().getName();
			ps.setString(1,owner);
			ps.setString(2,funz.getName());
			rs=ps.executeQuery();
			
			if(rs.next()){
				JSONArray record=new JSONArray();
				record.put("Created");
				record.put(rs.getString(1));
				data.put(record);
				
				record=new JSONArray();
				record.put("Last DDL Time");
				record.put(rs.getString(2));
				data.put(record);
				
				record=new JSONArray();
				record.put("TimeStamp");
				record.put(rs.getString(3));
				data.put(record);
				
				record=new JSONArray();
				record.put("Status");
				record.put(rs.getString(4));
				data.put(record);
				
				record=new JSONArray();
				record.put("Long Name");
				record.put(rs.getString(5));
				data.put(record);
				
			}
  		}catch(Exception e){
  			e.printStackTrace();
  		}finally{
  			try{if(rs!=null)
  				rs.close();
  			}catch(Exception e){
  			}
  			try{if(ps!=null)
  				ps.close();
  			}catch(Exception e){
  			}
  		}
		
  		results.put("meta",meta);
		results.put("data",data);
		return results;
	}
}
