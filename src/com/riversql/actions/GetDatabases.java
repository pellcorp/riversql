
package com.riversql.actions;

import java.sql.SQLException;
import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.WebSQLSession;
import com.riversql.dao.DriversDAO;
import com.riversql.dao.SourcesDAO;
import com.riversql.dbtree.SQLSession;
import com.riversql.entities.Driver;


public class GetDatabases implements JSONAction {

	
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONArray arr=new JSONArray();
		WebSQLSession sessions=(WebSQLSession)request.getSession(true).getAttribute("sessions");
		for (SQLSession sqlsession : sessions.getSqlsessions()) {
			
			Driver driver=DriversDAO.getDriver(em, SourcesDAO.getSource(em, sqlsession.getSourceid()).getDriverid());
			String iconurl=driver.getIconurl();
			JSONObject obj=new JSONObject();
			obj.put("id",sqlsession.getId());
			obj.put("iconurl",iconurl);
			obj.put("name",sqlsession.getSessionName());
			try {
				obj.put("autocommit",sqlsession.getConn().getAutoCommit());
			} catch (SQLException e) {
				obj.put("autocommit",false);
			}
			boolean hasCatalogs=sqlsession.getDatabaseNode().supportsCatalogs();
			if(hasCatalogs){
				String catalogs[]=sqlsession.getDatabaseNode().getCatalogs();
				obj.put("catalogs", Arrays.asList(catalogs));
				obj.put("catalog",sqlsession.getConn().getCatalog());
			}
			obj.put("hasCatalogs", hasCatalogs);
			arr.put(obj);
		}
		
		JSONObject ret = new JSONObject();
		ret.put("databases", arr);
		return ret;
	}

}
