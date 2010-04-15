
package com.riversql.plugins.oracle.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.riversql.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.dbtree.TableNode;
import com.riversql.dbtree.TablesNode;
import com.riversql.plugin.BasePluginType;
import com.riversql.plugins.oracle.FunctionNode;
import com.riversql.plugins.oracle.PackageNode;
import com.riversql.plugins.oracle.ProcedureNode;


public class DependentObjects {

	private String id;

	public DependentObjects(String id) {
		this.id=id;
	}

	public JSONObject execute()throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		Object obj=IDManager.get().get(id);
		String []strs={"Owner","Type","Name"};
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		String type="";
		SQLConnection conn=null;
		String owner=null;
		String objectname=null;
		if(obj instanceof BasePluginType){
			BasePluginType bot=(BasePluginType)obj;
			conn=bot.getConn();
			BasePluginType tot=(BasePluginType)bot.getParent();
			owner=tot.getParent().getName();
			if(bot instanceof PackageNode){
				type="PACKAGE";
			}
			else if(bot instanceof ProcedureNode){
				type="PROCEDURE";
			}
			else if(bot instanceof FunctionNode){
				type="FUNCTION";
			}
		}else if(obj instanceof TableNode){
			TableNode tn=(TableNode)obj;
			objectname=tn.getName();
			conn=tn.getConn();
			TablesNode parent=tn.getParent();
			owner=parent.getParent().getName();
			type=parent.getName();
			
		}
		String sql="select owner,type,name from sys.ALL_DEPENDENCIES " +
				" where referenced_owner=? and referenced_name=? " +
				" and referenced_type=? order by owner,type,name";
		ResultSet rs=null;
		PreparedStatement ps=null;
		
  		try{
  			if(conn==null)
  				return results;
			ps=conn.prepareStatement(sql);
			ps.setString(1,owner);
			ps.setString(2,objectname);
			ps.setString(3,type);
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
