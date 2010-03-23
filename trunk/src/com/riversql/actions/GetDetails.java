
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.plugin.PluginManager;





public class GetDetails  implements JSONAction {
	String nodeType,sessionid;
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONArray array=new JSONArray();
		if("tb".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Columns Detail");
			record.put("do?action=getMeta");
			array.put(record);
			record=new JSONArray();
			record.put("Preview");
			record.put("do?action=preview");
			array.put(record);
			
			record=new JSONArray();
			record.put("Indexes");
			record.put("do?action=getIndexes");
			array.put(record);
			record=new JSONArray();
			record.put("Primary Key");
			record.put("do?action=getPK");
			array.put(record);
			record=new JSONArray();
			record.put("Foreign Keys");
			record.put("do?action=getFK");
			array.put(record);
			record=new JSONArray();
			record.put("Grants");
			record.put("do?action=getGrants");
			array.put(record);
			record=new JSONArray();
			record.put("Exported Keys");
			record.put("do?action=getExportedKeys");
			array.put(record);
			
		}else if ("view".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Columns Detail");
			record.put("do?action=getMeta");
			array.put(record);
			record=new JSONArray();
			record.put("Preview");
			record.put("do?action=preview");
			array.put(record);
		}else if("tbs".equals(nodeType)||"views".equals(nodeType)||"mysql_functs".equals(nodeType)||"mysql_users".equals(nodeType)||"mysql_procs".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Objects");
			record.put("do?action=getMeta");
			array.put(record);
		}else if("mysql_funct".equals(nodeType)||"mysql_user".equals(nodeType)||"mysql_proc".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Objects");
			record.put("do?action=getMeta");
			array.put(record);
		}else if("dtbs".equals(nodeType)){
			JSONArray record=new JSONArray();
			record.put("Database Metadata");
			record.put("do?action=getDatabaseMetadata");
			array.put(record);
			record=new JSONArray();
			record.put("Connection Status");
			record.put("do?action=getConnectionStatus");
			array.put(record);
		}
		PluginManager.getInstance().loadAddedDetails(array,sessionid,nodeType);
		JSONObject ret = new JSONObject();
		ret.put("details", array);
		return ret;
	}

}
