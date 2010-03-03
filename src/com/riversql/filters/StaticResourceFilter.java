
package com.riversql.filters;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;



public class StaticResourceFilter implements Filter {
	ServletContext sc;
	FilterConfig fc;
	long cacheTimeout = 3600*24*60*1000;

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain)
	throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		response.setHeader("Cache-Control", "max-age="+cacheTimeout);
		long now = System.currentTimeMillis();
		response.setDateHeader("Expires", now + cacheTimeout);
		chain.doFilter(request, response);
	
	}

	public void init(FilterConfig filterConfig) {
	}

	public void destroy() {
	}
}
