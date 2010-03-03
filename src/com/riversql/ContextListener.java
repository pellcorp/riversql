
package com.riversql;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.riversql.dao.DriversDAO;


public class ContextListener implements ServletContextListener {

	private static EntityManagerFactory emf;
	public void contextDestroyed(ServletContextEvent sce) {
		EntityManagerFactory emf = (EntityManagerFactory)sce.getServletContext().getAttribute("emf");
		if(emf!=null)
			emf.close();
	}

	public void contextInitialized(ServletContextEvent sce) {
		ServletContext sc = sce.getServletContext();
		EntityManagerFactory emf = Persistence.createEntityManagerFactory("riversql");
		setEntityManagerFactory(emf);
		sc.setAttribute("emf",emf);
		EntityManager em = emf.createEntityManager();
		EntityTransaction tx = em.getTransaction();
		if(DriversDAO.getDrivers(em).isEmpty()){
			tx.begin();
			DriversDAO.initialize(em);
			tx.commit();
		}/*else{
			tx.begin();
			DriversDAO.deleteAll(em);
			DriversDAO.initialize(em);
			tx.commit();
		}*/
		tx.begin();
		DriversDAO.validateDrivers(em);
		tx.commit();
		
//		EntityTransaction tx = em.getTransaction();
		
//			tx.begin();
//			try {
//				SourcesDAO.deleteAllSources(em);
//				tx.commit();
//				tx.begin();
//				SourcesDAO.initialize(em);
//				tx.commit();
//			} catch (Exception e) {
//				tx.rollback();
//			}

		em.close();
		sc.setAttribute("riversql_version", sc.getInitParameter("riversql_version"));
	}
	public static void setEntityManagerFactory(EntityManagerFactory emf ) {
		ContextListener.emf=emf;
	}
	public static EntityManagerFactory getEntityManagerFactory(){
		return emf;
	}
}