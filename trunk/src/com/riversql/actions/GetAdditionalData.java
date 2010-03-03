
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


public class GetAdditionalData implements JSONAction {
	String queryID=null;
	public void setQueryID(String queryID) {
		this.queryID = queryID;
	}
	int all=0;
	public void setAll(int all) {
		this.all = all;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		SQLExecutor executor=(SQLExecutor)IDManager.get().get(queryID);
		JSONArray data=new JSONArray();
		executor.next(data,all==1);	
		JSONObject ret = new JSONObject();
		ret.put("data", data);
		return ret;
	}

}
