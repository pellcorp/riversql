
package com.riversql;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

public class SessionListener implements HttpSessionListener {

	public void sessionCreated(HttpSessionEvent hse) {
	
	}

	public void sessionDestroyed(HttpSessionEvent hse) {
		//System.out.println("sessionDestroyed");
		WebSQLSession sessions=(WebSQLSession)hse.getSession().getAttribute("sessions");
		if(sessions!=null)
			sessions.closeSessions();
	}

}
