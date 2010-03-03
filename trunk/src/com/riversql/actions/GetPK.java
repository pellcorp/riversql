
package com.riversql.actions;

import java.sql.ResultSet;
import java.sql.Statement;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.TableNode;




public class GetPK  implements JSONAction {
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
			String []strs={"Primary Key","Column Name","Order"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			ResultSet rs=null;
			try{
				rs=table.getPK();
				while(rs.next()){
					String colName=rs.getString("COLUMN_NAME"); 
					short order=rs.getShort("KEY_SEQ");
					String pkName=rs.getString("PK_NAME");  
					if(pkName!=null){
						JSONArray record=new JSONArray();
						record.put(pkName);
						record.put(colName);
						record.put(order);
						data.put(record);
					}
				}
				
			}
			finally{
				try {
					if(rs!=null){
						Statement st=rs.getStatement();
						rs.close();
						if(st!=null)
							st.close();
					}
				} catch (Exception e) {
					
				}
			}
		}
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}
	

}
