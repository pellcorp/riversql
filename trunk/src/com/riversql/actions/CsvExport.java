
package com.riversql.actions;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;
import org.json.JSONArray;

import com.riversql.IFileUploadAction;
import com.riversql.actions.export.impl.CSVTableExporter;

public class CsvExport implements IFileUploadAction {

	public void execute(Map<String, FileItem> fileMap, Map<String,String> parameterMap,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		
		String meta_ = parameterMap.get("meta");
		String data_ = parameterMap.get("data");
		//String info_ = parameterMap.get("info");
		JSONArray meta=new JSONArray(meta_); 
		JSONArray data=new JSONArray(data_);
		//JSONArray info2=new JSONArray(info_);
		CSVTableExporter tableExporter=new CSVTableExporter(meta.length(),meta);

		for(int i=0;i<data.length();i++){
			JSONArray row=data.getJSONArray(i);
			tableExporter.newLine();
			for(int j=0;j<row.length();j++){
				tableExporter.newCell(row.get(j));
			}
		}
		tableExporter.finish();

		response.setHeader("Pragma" ,"public");
		response.setHeader("Expires", "0"); // set expiration time
		response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
		response.setContentType(tableExporter.getMimeType());
		response.setHeader("Content-Disposition","attachment;filename=export.pdf");
		response.setContentLength(tableExporter.getContentSize());
		ServletOutputStream os = response.getOutputStream();
		tableExporter.copyTo(os);
	}

}
