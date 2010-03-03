
package com.riversql.actions;

import java.sql.DatabaseMetaData;
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




public class GetExportedKeys implements JSONAction {

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
			String []strs={"Foreign Key Name","Primary Key","Foreign Key Catalog","Foreign Key Schema",
					"Foreign Key Table","Foreign Key Column",
					"Key Sequence","Update Rule","Delete Rule","Deferrability"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			ResultSet rs=null;
			try{
				rs=table.getExportedKeys();
				while(rs.next()){
					String cat=rs.getString("FKTABLE_CAT"); 
					String schema=rs.getString("FKTABLE_SCHEM");
					String tablen=rs.getString("FKTABLE_NAME");
					String column=rs.getString("FKCOLUMN_NAME");
					String key_seq=rs.getString("KEY_SEQ");
					short update=rs.getShort("UPDATE_RULE");
					short delete=rs.getShort("DELETE_RULE");
					String fkName=rs.getString("FK_NAME");
					String pkName=rs.getString("PK_NAME");
					short deferrability=rs.getShort("DEFERRABILITY");
					
					String update_rule_s="";
					if(update==DatabaseMetaData.importedKeyNoAction){
						update_rule_s="No Action";
					}else if(update==DatabaseMetaData.importedKeyCascade){
						update_rule_s="Key Cascade";
					}else if(update==DatabaseMetaData.importedKeySetNull){
						update_rule_s="Key Set Null";
					}else if(update==DatabaseMetaData.importedKeySetDefault){
						update_rule_s="Key Set Default";
					}else if(update==DatabaseMetaData.importedKeyRestrict){
						update_rule_s="Key Restrict";
					}
					String delete_rule_s="";
					
					if(delete==DatabaseMetaData.importedKeyNoAction){
						delete_rule_s="No Action";
					}
					else if(delete==DatabaseMetaData.importedKeyCascade){
						delete_rule_s="Key Cascade";
					}
					else if(delete==DatabaseMetaData.importedKeySetNull){
						delete_rule_s="Key Set Null";
					}
					else if(delete==DatabaseMetaData.importedKeyRestrict){
						delete_rule_s="Key Restrict";
					}
					else if(delete==DatabaseMetaData.importedKeySetDefault){
						delete_rule_s="Key Set Default";
					}
					
					
					String deferrability_s="";
					if(deferrability==DatabaseMetaData.importedKeyInitiallyDeferred){
						deferrability_s="Initially Deferred";
					}
					else if(deferrability==DatabaseMetaData.importedKeyInitiallyImmediate){
						deferrability_s="Initially Immediate";
					}
					else if(deferrability==DatabaseMetaData.importedKeyNotDeferrable){
						deferrability_s="Not Deferrable";
					}
					
					if(fkName!=null && pkName!=null){
						JSONArray record=new JSONArray();
						record.put(fkName);
						record.put(pkName);
						record.put(cat);
						record.put(schema);
						record.put(tablen);
						record.put(column);
						record.put(key_seq);
						record.put(update_rule_s);
						record.put(delete_rule_s);
						record.put(deferrability_s);
						data.put(record);
						
					}
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
