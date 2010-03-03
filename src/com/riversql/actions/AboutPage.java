
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.IPageAction;

public class AboutPage implements IPageAction {
	String pageid;
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	 
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		request.setAttribute("pageid", pageid);
		request.getRequestDispatcher("about.jsp").forward(request, response);

	}

}
