
package com.riversql;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public interface IPageAction {
	public void execute(HttpServletRequest request, HttpServletResponse response,  EntityManager em, EntityTransaction et) throws Exception;
}
