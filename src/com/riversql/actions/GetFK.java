
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




public class GetFK implements JSONAction {

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
			String []strs={"Foreign Key Name","Primary Key Catalog","Primary Key Schema",
					"Primary Key Table","Primary Key Column","Foreign Key Column",
					"Key Sequence","Update Rule","Delete Rule","Primary Key Name","Deferrability"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			ResultSet rs=null;
			try{
				rs=table.getFK();
				while(rs.next()){
					String fkName=rs.getString("FK_NAME"); 
					String pkCat=rs.getString("PKTABLE_CAT");
					String pkSchema=rs.getString("PKTABLE_SCHEM");
					String pkTable=rs.getString("PKTABLE_NAME");
					String pkColumnName=rs.getString("PKCOLUMN_NAME");
					String fkColumnName=rs.getString("FKCOLUMN_NAME");
					String key_seq=rs.getString("KEY_SEQ");
					short update_rule=rs.getShort("UPDATE_RULE");
					String update_rule_s="";
					if(update_rule==DatabaseMetaData.importedKeyNoAction){
						update_rule_s="No Action";
					}else if(update_rule==DatabaseMetaData.importedKeyCascade){
						update_rule_s="Key Cascade";
					}else if(update_rule==DatabaseMetaData.importedKeySetNull){
						update_rule_s="Key Set Null";
					}else if(update_rule==DatabaseMetaData.importedKeySetDefault){
						update_rule_s="Key Set Default";
					}else if(update_rule==DatabaseMetaData.importedKeyRestrict){
						update_rule_s="Key Restrict";
					}
					String delete_rule_s="";
					short deleteRule=rs.getShort("DELETE_RULE");
					if(deleteRule==DatabaseMetaData.importedKeyNoAction){
						delete_rule_s="No Action";
					}
					else if(deleteRule==DatabaseMetaData.importedKeyCascade){
						delete_rule_s="Key Cascade";
					}
					else if(deleteRule==DatabaseMetaData.importedKeySetNull){
						delete_rule_s="Key Set Null";
					}
					else if(deleteRule==DatabaseMetaData.importedKeyRestrict){
						delete_rule_s="Key Restrict";
					}
					else if(deleteRule==DatabaseMetaData.importedKeySetDefault){
						delete_rule_s="Key Set Default";
					}
					String pkName=rs.getString("PK_NAME");
					short deferrability=rs.getShort("DEFERRABILITY");
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
					
					if(fkName!=null){
						JSONArray record=new JSONArray();
						
						record.put(fkName);
						record.put(pkCat);
						record.put(pkSchema);
						record.put(pkTable);
						record.put(pkColumnName);
						record.put(fkColumnName);
						record.put(key_seq);
						record.put(update_rule_s);
						record.put(delete_rule_s);
						record.put(pkName);
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
