
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.SQLSession;



public class CommitConnection implements JSONAction {
	String sessionid;
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	
	 
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		SQLSession sqlsession=null;
		if(sessionid!=null){
			sqlsession=(SQLSession)IDManager.get().get(sessionid);
			sqlsession.getConn().commit();
		}
		
		return new JSONObject();
	}

}
