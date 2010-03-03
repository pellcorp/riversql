
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.JSONAction;





public class GetMenu implements JSONAction {

	String nodeType;
	String sessionid;
	public void setNodeType(String nodeType) {
		this.nodeType = nodeType;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONObject obj_=new JSONObject();
		JSONArray arr=new JSONArray();
//		obj.put("Copy Name");
//		obj.put("icons/page_white_copy.png");
//		obj.put("alert('test '+menuTreeC.nodeid.attributes.type);");
//		arr.put(obj);
		
		if(!"tb".equals(nodeType)){
			JSONArray obj=new JSONArray();
			obj.put("Refresh...");
			obj.put("icons/arrow_refresh.png");
			obj.put("refreshNode(menuTreeC.nodeid);");
			arr.put(obj);
		}
		
		if("tb".equals(nodeType)){
			JSONArray obj=new JSONArray();
			obj.put("Select All");
			obj.put("icons/page_edit.png");
			obj.put("newEditor('select * from '+menuTreeC.nodeid.attributes.qname);");
			arr.put(obj);
			obj=new JSONArray();
			obj.put("Export Table...");
			obj.put("icons/cd_edit.png");
			obj.put("exportTable(menuTreeC.nodeid);");
			arr.put(obj);
			obj=new JSONArray();
			obj.put("Alter Table...");
			obj.put("icons/table_edit.png");
			obj.put("alterTable(menuTreeC.nodeid);");
			arr.put(obj);
			
		}else if("view".equals(nodeType)) {
			JSONArray obj=new JSONArray();
			obj.put("Select All");
			obj.put("icons/page_edit.png");
			obj.put("newEditor('select * from '+menuTreeC.nodeid.attributes.qname);");
			arr.put(obj);
		}
		com.riversql.plugin.PluginManager.getInstance().loadAddedMenu(arr,sessionid,nodeType);
		obj_.put("menu", arr);
		return obj_;
	}

}
