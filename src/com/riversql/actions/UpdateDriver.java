
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.dao.DriversDAO;


public class UpdateDriver implements JSONAction {
	String drivername,driverclass,exampleurl;
	int driverid;
		public void setDrivername(String drivername) {
		this.drivername = drivername;
	}
	public void setDriverclass(String driverclass) {
		this.driverclass = driverclass;
	}
	public void setExampleurl(String exampleurl) {
		this.exampleurl = exampleurl;
	}
	public void setDriverid(int driverid) {
		this.driverid = driverid;
	}

	 
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONObject obj=new JSONObject();
		DriversDAO.updateDriver(em, driverid, drivername, driverclass, exampleurl);		
		return obj;
	}

}
