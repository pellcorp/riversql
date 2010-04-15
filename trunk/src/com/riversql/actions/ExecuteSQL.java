
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.riversql.sql.QueryTokenizer;
import com.riversql.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.WebSQLSession;
import com.riversql.dbtree.SQLSession;
import com.riversql.utils.SQLExecutor;

public class ExecuteSQL implements JSONAction {
	String sql, sessionid;
	public void setSql(String sql) {
		this.sql = sql;
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
			if(sqlsession==null){
				
				sqlsession = null;//GetTree.createSQLSession(request, em,sessionid);
			}
			if(sqlsession!=null)
				conn=sqlsession.getConn();
		}

		
		
		
		JSONArray info=new JSONArray();
		
		long init=0;
		
		init=System.nanoTime();
		
		JSONArray resultSets=new JSONArray();
		
		QueryTokenizer qt=new QueryTokenizer(";","--",false);
		qt.setScriptToTokenize(sql);
		String nextQuery=null;
		int limit=10;
		int maxLimit=5000;
		HttpSession session = request.getSession(true);
		WebSQLSession sessions=(WebSQLSession)session.getAttribute("sessions");
		
		while(qt.hasQuery()){
			nextQuery=qt.nextQuery();
			SQLExecutor he = new SQLExecutor(conn,limit,maxLimit,nextQuery);
			sessions.getExecutors().add(he);
			JSONObject resultSet=new JSONObject();
			JSONArray meta=new JSONArray(); 
			JSONArray data=new JSONArray();
			JSONArray info2=new JSONArray();
			
			
			try{
				he.executeQuery( data, meta,info2);
				info2.put(true);
			}catch(Exception e){
				info2.put(false);
				info2.put(e.getMessage());
			}
			resultSet.put("meta", meta);
			resultSet.put("data", data);
			resultSet.put("info", info2);
			resultSets.put(resultSet);
		}

		long end=System.nanoTime();
		
			
		info.put((end-init)/1000000);		
		
		JSONObject ret = new JSONObject();
	
		
		ret.put("info", info);
		ret.put("resultSets",resultSets);
		return ret;
	}

}
