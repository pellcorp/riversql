
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.dao.DriversDAO;
import com.riversql.entities.Driver;




public class GetDrivers implements JSONAction {

	

	 
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONArray arr=new JSONArray();
		for(Driver drv: DriversDAO.getDrivers(em,"")){//sessions.getUser())){
			JSONObject obj=new JSONObject();
			obj.put("id",drv.getId());
			obj.put("drvname", drv.getDriverName());
			obj.put("drvclassname",drv.getDriverClassName());
			obj.put("exampleurl",drv.getExampleUrl());
			obj.put("valid",drv.isValid());
			obj.put("icon",drv.getIconurl());
			arr.put(obj);
		}
		
		
	
		
		JSONObject ret = new JSONObject();
		ret.put("drivers", arr);
		return ret;
	}

}
