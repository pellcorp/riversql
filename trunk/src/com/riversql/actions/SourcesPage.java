
package com.riversql.actions;

import java.util.List;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.IPageAction;
import com.riversql.dao.SourcesDAO;
import com.riversql.entities.Source;

public class SourcesPage implements IPageAction {

	String pageid;
	public void setPageid(String pageid) {
		this.pageid = pageid;
	}
	 
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		List<Source> ls = SourcesDAO.getSources(em);
		Random rn=new Random();
		request.setAttribute("rn", Math.abs(rn.nextInt()));
		request.setAttribute("pageid", pageid);
		request.setAttribute("ls", ls);
		request.getRequestDispatcher("sources.jsp").forward(request, response);

	}

}
