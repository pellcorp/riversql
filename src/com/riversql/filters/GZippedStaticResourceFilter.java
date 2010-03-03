
package com.riversql.filters;

import java.io.*;

import javax.servlet.*;
import javax.servlet.http.*;

import java.util.zip.GZIPOutputStream;


public class GZippedStaticResourceFilter implements Filter {
	ServletContext sc;
	FilterConfig fc;
	long cacheTimeout = Long.MAX_VALUE;

	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain)
	throws IOException, ServletException {
		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;
		
		String method=request.getMethod();
		if(!"GET".equals(method)){
			chain.doFilter(request, response);
			return;
		}

		
		
		String contextPath=sc.getContextPath();
		String requestURI=request.getRequestURI();
		
		
		if(contextPath!=null){
			requestURI=requestURI.substring(contextPath.length());
		}
				
		File fpath=null;
		String path=null;
		try {
			path= sc.getRealPath(requestURI);
			fpath = new File(path);
		} catch (Exception e) {
			
		}
		if(fpath==null || !fpath.exists()|| !fpath.isFile()){
			chain.doFilter(request, response);
			return;
		}
		File tempDir = (File)sc.getAttribute(
		"javax.servlet.context.tempdir");
		String temp = tempDir.getAbsolutePath();
		
		File filegz = new File(temp+requestURI+".gz");
		if(!filegz.exists()){
			//chain.doFilter(request, response);
			createGZippedFile(fpath, filegz);
			writeFromGZippedFile(response, filegz);
		}
		else{
			if(filegz.lastModified()<fpath.lastModified()){
				filegz.delete();
				//chain.doFilter(request, response);
				createGZippedFile(fpath, filegz);
				writeFromGZippedFile(response, filegz);
			}else{
				writeFromGZippedFile(response, filegz);
			}
		}
		
		return;
	}

	private void writeFromGZippedFile(HttpServletResponse response, File filegz)
			throws FileNotFoundException, IOException {
		response.setHeader("Cache-Control", "max-age=5184000");
		long now = System.currentTimeMillis();
		response.setDateHeader("Expires", now + 5184000);
		
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(filegz));
		ByteArrayOutputStream bos=new ByteArrayOutputStream(512*1024);
		byte[] buff = new byte[1024*64];
		int bytesRead;
		while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			bos.write(buff,0,bytesRead);
		}
		bis.close();
		response.addHeader("Content-Encoding","gzip");
		response.setContentLength(bos.size());
		bos.writeTo(response.getOutputStream());
	}

	private void createGZippedFile(File fpath, File filegz)
			throws FileNotFoundException, IOException {
		new File(filegz.getParent()).mkdirs();
		BufferedOutputStream bos=new BufferedOutputStream(new FileOutputStream(filegz));
		GZIPOutputStream zos=new GZIPOutputStream(bos);
		BufferedInputStream bis=new BufferedInputStream(new FileInputStream(fpath));
		byte[] buff = new byte[1024*64];
		int bytesRead;
		while(-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
			zos.write(buff,0,bytesRead);
		}
		zos.finish();
		zos.close();
		bis.close();
	}

	public void init(FilterConfig filterConfig) {
		this.fc = filterConfig;
		String ct = fc.getInitParameter("cacheTimeout");
		if (ct != null) {
			cacheTimeout = 60*1000*Long.parseLong(ct);
		}
		this.sc = filterConfig.getServletContext();
	}

	public void destroy() {
		this.sc = null;
		this.fc = null;
	}
}
