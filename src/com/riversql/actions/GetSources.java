
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.dao.DriversDAO;
import com.riversql.dao.SourcesDAO;
import com.riversql.entities.Driver;
import com.riversql.entities.Source;


public class GetSources implements JSONAction {

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception {
		
		JSONArray arr=new JSONArray();
		
		for (Source source : SourcesDAO.getSources(em)) {
			Driver driver=DriversDAO.getDriver(em, source.getDriverid());
			String iconurl=driver.getIconurl();
			JSONObject obj=new JSONObject();
			obj.put("id", source.getId());
			obj.put("iconurl", iconurl);
			obj.put("sourceName",source.getSourceName());
			obj.put("creationDate",source.getCreationDate());
			obj.put("driverid",source.getDriverid());
			obj.put("jdbcUrl",source.getJdbcUrl());
			obj.put("userName",source.getUserName());
			arr.put(obj);
		}
			
		JSONObject ret = new JSONObject();
		ret.put("sources", arr);
		return ret;

	}

}
