
package com.riversql.actions;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

import com.riversql.IFileUploadAction;

public class ExcelExport implements IFileUploadAction {

	String ex;
	public void setEx(String ex) {
		this.ex = ex;
	}
	public void execute(Map<String, FileItem> fileMap, Map<String,String> parameterMap,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		
		ex=parameterMap.get("ex");
		
		response.setHeader("Pragma" ,"public");
		response.setHeader("Expires", "0"); // set expiration time
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setContentType("application/vnd.ms-excel");
		response.setHeader("Content-Disposition","attachment;filename=export.xls");
		response.setContentLength(ex.length());
		ServletOutputStream os = response.getOutputStream();
		os.write(ex.getBytes());
		os.flush();
	}

}
