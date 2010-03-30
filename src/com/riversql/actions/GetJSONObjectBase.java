/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 *
 * @author river.liao
 */
public abstract class GetJSONObjectBase implements GetJSONObjectInterface {
        public HttpServletRequest request;
        public HttpServletResponse response;
        public EntityManager em;
        public EntityTransaction et;
        public String nodeId;
	public void init(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
        {
                this.request=request;
                this.response=response;
                this.em=em;
                this.et=et;
                nodeId=request.getParameter("id");
        }
}
