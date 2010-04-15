

package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.sql.SQLConnection;

import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.SQLSession;


public class ChangeCatalog implements JSONAction {
	String catalog, sessionid;
	public void setCatalog(String catalog) {
		this.catalog = catalog;
	}
	public void setSessionid(String sessionid) {
		this.sessionid = sessionid;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
	throws Exception {
		SQLConnection conn=null;
		if(sessionid!=null){
			SQLSession sqlsession=(SQLSession)IDManager.get().get(sessionid);
			if(sqlsession!=null){
				conn=sqlsession.getConn();
				conn.setCatalog(catalog);
			}
		}
		
		JSONObject ret = new JSONObject();
		return ret;
	}

}
