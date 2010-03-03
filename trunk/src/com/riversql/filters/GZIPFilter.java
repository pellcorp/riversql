
package com.riversql.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


public class GZIPFilter implements Filter {


    public static final String HTTP_ACCEPT_ENCODING = "Accept-Encoding";

    
	
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {
		if (req instanceof HttpServletRequest) {
			HttpServletRequest request = (HttpServletRequest) req;
			HttpServletResponse response = (HttpServletResponse) res;
			
			
			
			
			String ae = request.getHeader(HTTP_ACCEPT_ENCODING);
			if (ae != null && ae.indexOf("gzip") != -1) {
				
				
				
				GZIPResponseWrapper wrappedResponse = new GZIPResponseWrapper(response);

			      try {

			        chain.doFilter(req, wrappedResponse);

			      }

			      catch (ServletException ex) {throw ex;}

			      catch (IOException ex) {throw ex;}

			      catch (Exception ex) {throw new ServletException(ex);} // this is in case a Servet caused an internal error

			      finally {

			        wrappedResponse.finishResponse();

			      }


				
			}
			else
				chain.doFilter(req, res);
		}
	}

	public void init(FilterConfig filterConfig) {
		// noop
	}

	public void destroy() {
		// noop
	}
}
