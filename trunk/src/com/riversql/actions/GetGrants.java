
package com.riversql.actions;

import java.sql.ResultSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.TableNode;





public class GetGrants implements JSONAction {
	String id;
	public void setId(String id) {
		this.id = id;
	}
	

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		
		
		
		//String id=request.getParameter("id");
		Object obj=IDManager.get().get(id);
		if(obj!=null && obj instanceof TableNode){
			String []strs={"Grantor","Grantee","Privilege",	"Grantable"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			ResultSet rs=null;
			try{
				rs=table.getGrants();
				while(rs.next()){
					String grantor=rs.getString("GRANTOR"); 
					String grantee=rs.getString("GRANTEE");
					String privilege=rs.getString("PRIVILEGE");
					String grantable=rs.getString("IS_GRANTABLE");
					
					JSONArray record=new JSONArray();
						
					record.put(grantor);
					record.put(grantee);
					record.put(privilege);
					record.put(grantable);
					data.put(record);

				}
				
			}
			finally{
				if(rs!=null)
					rs.close();
			}
		}
		results.put("meta",meta);
		results.put("data",data);
		return results;

	}


}
