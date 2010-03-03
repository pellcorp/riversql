
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.dao.SourcesDAO;

public class CreateSource implements JSONAction {

	String url, sourceName,user;int driverid;
	
	public void setUrl(String url) {
		this.url = url;
	}

	public void setSourceName(String sourceName) {
		this.sourceName = sourceName;
	}

	

	public void setUser(String user) {
		this.user = user;
	}

	

	
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		SourcesDAO.addSource(em, sourceName, url, driverid, user);
		JSONObject obj=new JSONObject();
		obj.put("success",true);
		return obj;
	}

	public void setDriverid(int driverid) {
		this.driverid = driverid;
	}

}
