
package com.riversql.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.plugin.BasePluginType;
import com.riversql.plugins.oracle.FunctionNode;


public class FunctionParametersDetail {
	
	private String id;

	public FunctionParametersDetail(String id) {
		this.id=id;
	}

	public JSONObject execute() throws Exception{
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		//String[]detailKeys={"Param Name","Max Value","Increment By","Cycle Flag", "Order","Cache Size","Last Number"};
		String []strs={"Param Name","Data Type","Data Length", "Data Precision","In/Out"};
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		Object obj=IDManager.get().get(id);
		FunctionNode funz=(FunctionNode)obj;
		final String sql="SELECT argument_name, DATA_TYPE, data_length, data_precision,in_out,sequence FROM SYS.ALL_ARGUMENTS WHERE OWNER = ? and data_level=0 and object_id=(SELECT OBJECT_ID FROM SYS.ALL_OBJECTS WHERE OWNER = ? AND OBJECT_NAME = ? AND OBJECT_TYPE='FUNCTION') order by sequence";
		
		ResultSet rs=null;
		PreparedStatement ps=null;
  		try{
			ps=funz.getConn().prepareStatement(sql);
			BasePluginType tot=(BasePluginType)funz.getParent();
			String owner=tot.getParent().getName();
			ps.setString(1,owner);
			ps.setString(2,owner);
			ps.setString(3,funz.getName());
			rs=ps.executeQuery();
			
			while(rs.next()){
				JSONArray record=new JSONArray();
				for(int i=0;i<strs.length;i++){
					record.put(rs.getString(i+1));
				}
				data.put(record);
			}
			
  		}catch(Exception e){
  			
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
