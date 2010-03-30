/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author river.liao
 */
public interface GetJSONObjectInterface {
    public void init(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception;
    public JSONObject execute() throws Exception;
}
