
package com.riversql.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.plugin.BasePluginType;
import com.riversql.plugins.oracle.SequenceNode;


public class SeqDetail {
	
	private String id;

	public SeqDetail(String id) {
		this.id=id;
	}

	public JSONObject execute()throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		String[]detailKeys={"Min Value","Max Value","Increment By","Cycle Flag", "Order","Cache Size","Last Number"};
		String []strs={"Property","Value"};
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		Object obj=IDManager.get().get(id);
		SequenceNode seq=(SequenceNode)obj;
		final String sql="select min_value,max_value,increment_by,cycle_flag,order_flag, cache_size, last_number from sys.all_sequences "+
		"where sequence_owner=? and sequence_name=?";
		
		ResultSet rs=null;
		PreparedStatement ps=null;
  		try{
			ps=seq.getConn().prepareStatement(sql);
			BasePluginType tot=(BasePluginType)seq.getParent();
			String owner=tot.getParent().getName();
			ps.setString(1,owner);
			ps.setString(2,seq.getName());
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
