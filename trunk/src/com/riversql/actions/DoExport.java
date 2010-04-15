
package com.riversql.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.sql.SQLConnection;
import com.riversql.IDManager;
import com.riversql.IPageAction;
import com.riversql.actions.export.ITableExporter;
import com.riversql.actions.export.impl.CSVTableExporter;
import com.riversql.actions.export.impl.ExcelTableExporter;
import com.riversql.actions.export.impl.PDFTableExporter;
import com.riversql.dbtree.TableNode;
import com.riversql.utils.ResultSetReader;

public class DoExport implements IPageAction {
	String id; int count;String format;
	public void setId(String id) {
		this.id = id;
	}
	public void setFormat(String format) {
		this.format = format;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		String sql="SELECT * FROM "+tn.getQualifiedName();
		SQLConnection conn = tn.getConn();
		PreparedStatement ps=null;
		ResultSet rs=null;
		ITableExporter tExp=null;
		if("excel".equals(format))
			tExp=new ExcelTableExporter(tn.getQualifiedName());
                else if("csv".equals(format))
			tExp=new CSVTableExporter(tn.getQualifiedName());
                else
			tExp=new PDFTableExporter(tn.getQualifiedName());
		
		try{
			ps=conn.prepareStatement(sql);
			try{if(count>0)ps.setMaxRows(count);}catch(Exception e){}
			try{ps.setFetchSize(500);}catch(Exception e){}
			rs = ps.executeQuery();
			ResultSetMetaData metadata=rs.getMetaData();
			int columncount=metadata.getColumnCount();
			tExp.configure(metadata);
			ResultSetReader reader=new ResultSetReader(rs);
			Object[]row;
			while((row=reader.readRow())!=null){
				tExp.newLine();
				for(int i=0;i<columncount;i++){
					tExp.newCell(row[i]);
				}
			}
			tExp.finish();
		}finally{
			if(rs!=null){
				try {
					rs.close();
				} catch (Exception e1) {
				}
			}
			if(ps!=null){
				try {
					ps.close();
				} catch (Exception e1) {
				}
			}
		}
		response.setContentLength(tExp.getContentSize());
		response.setContentType( tExp.getMimeType() ); 
		tExp.copyTo(response.getOutputStream());

	}

}
