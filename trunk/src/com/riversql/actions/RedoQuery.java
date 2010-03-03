
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.utils.SQLExecutor;

public class RedoQuery implements JSONAction {
	String queryID=null;
	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		SQLExecutor executor=(SQLExecutor)IDManager.get().get(queryID);
		JSONObject resultSet=new JSONObject();
		JSONArray meta=new JSONArray(); 
		JSONArray data=new JSONArray();
		JSONArray info2=new JSONArray();
		
		
		try{
			executor.redoQuery( data, meta,info2);
			info2.put(true);
		}catch(Exception e){
			info2.put(false);
			info2.put(e.getMessage());
		}
		resultSet.put("meta", meta);
		resultSet.put("data", data);
		resultSet.put("info", info2);
		return resultSet;
	}

}
