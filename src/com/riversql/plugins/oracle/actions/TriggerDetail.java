
package com.riversql.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.plugin.BasePluginType;
import com.riversql.plugins.oracle.TriggerNode;


public class TriggerDetail {
	String id;
	public TriggerDetail(String id) {
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
		TriggerNode trigger=(TriggerNode)obj;
		final String sql="SELECT trigger_type, triggering_event, table_owner,"+
   		"table_name,referencing_names,"+
   		"when_clause, status, action_type "+
			"FROM sys.all_triggers where owner=? and   trigger_name=?";
		
		ResultSet rs=null;
		PreparedStatement ps=null;
		String[]detailKeys={"Type","Event","Table Owner","Table Name", "Reference Names","When","Status", "Action Type"};
  		try{
			ps=trigger.getConn().prepareStatement(sql);
			BasePluginType tot=(BasePluginType)trigger.getParent();
			String owner=tot.getParent().getName();
			ps.setString(1,owner);
			ps.setString(2,trigger.getName());
			rs=ps.executeQuery();
			
			if(rs.next()){
				for(int i=0;i<detailKeys.length;i++){
					JSONArray record=new JSONArray();
					record.put(detailKeys[i]);
					record.put(rs.getString(i+1));
					data.put(record);
				}
			}
			
  		}catch(Exception e){
  			
  		}finally{
  			try{
  				if(rs!=null)
  					rs.close();
  			}catch(Exception e){
  			}
  			try{
  				if(ps!=null)
  					ps.close();
  			}catch(Exception e){
  			}
  		}
		
  		results.put("meta",meta);
		results.put("data",data);
		return results;
	}

}
