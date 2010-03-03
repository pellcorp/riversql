

package com.riversql;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

public interface JSONAction {
	public JSONObject execute(HttpServletRequest request, HttpServletResponse response,  EntityManager em, EntityTransaction et) throws Exception;
}
