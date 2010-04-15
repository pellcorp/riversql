
package com.riversql;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;


@SuppressWarnings("serial")
public abstract class DoServlet extends HttpServlet {

    @SuppressWarnings("unchecked")
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
        HttpSession session=req.getSession(true);

        synchronized (session) {
                WebSQLSession sessions=(WebSQLSession)session.getAttribute("sessions");
                if(sessions!=null){
                        IDManager idmanager=sessions.getIDManager();
                        IDManager.set(idmanager);
                }else{
                        WebSQLSession newsessions=new WebSQLSession();
                        IDManager.set(newsessions.getIDManager());
                        session.setAttribute("sessions", newsessions);
                        sessions=newsessions;
                }
        }


        EntityManagerFactory emf=(EntityManagerFactory)session.getServletContext().getAttribute("emf");
        EntityManager em=emf.createEntityManager();

        EntityTransaction et=null;


        String usernamesession=(String)session.getAttribute("loggeduser");
        String username=null;
        // Do we allow that user?
        if(usernamesession==null){
            String auth = req.getHeader("Authorization");
            username=allowUser(em,auth);
        }
        if (usernamesession==null && username==null) {
            em.close();
            resp.setContentType("text/plain");
            resp.setHeader("WWW-Authenticate", "BASIC realm=\"dwLoader users\"");
            resp.sendError(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }else if(usernamesession==null){
            usernamesession=username;
            session.setAttribute("loggeduser", username);
            int dot=usernamesession.indexOf(".");
                if(dot>-1){
                    String shortName=username.substring(0,dot);
                    shortName=shortName.substring(0, 1).toUpperCase() + shortName.substring(1).toLowerCase();
                    session.setAttribute("shortName", shortName);
                }else{
                    String capitalized=username.substring(0, 1).toUpperCase() + username.substring(1).toLowerCase();
                    session.setAttribute("shortName", capitalized);
                }
        }

        String action=req.getParameter("action");
        if(action!=null && action.equals("main")){
                em.close();
                req.getRequestDispatcher("/main.jsp").forward(req, resp);
                return;
        }
        try {
            execute(req, resp, em, et);
        } catch (Exception ex) {
            //Logger.getLogger(DoServlet.class.getName()).log(Level.SEVERE, null, ex);
            System.out.println(ex.toString());
        }
    }
	
    public abstract void execute(HttpServletRequest request, HttpServletResponse response,  EntityManager em, EntityTransaction et) throws Exception;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
                    throws ServletException, IOException {
        doGet(req, resp);
    }

    protected String allowUser(EntityManager em, String auth)  {
        return "User";
    }
}
