
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.IPageAction;

public class SelectTable implements IPageAction {

	String id;
	String dbid;
	public void setId(String id) {
		this.id = id;
	}
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	 
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		
//		SQLSession sqlsession = (SQLSession)IDManager.get().get(("db"+dbid));
//		
//		
//		
//		Random rn=new Random();
//		request.setAttribute("rn", Math.abs(rn.nextInt()));
//		TableNode table=(TableNode)IDManager.get().get(id);
//		//List<String > columns=table.getColumnNames();
//		String qtext="select * ";
//
////		for (Iterator<String> iterator = columns.iterator(); iterator.hasNext();) {
////			String columnName = iterator.next();
////			
////			qtext=qtext+"    "+columnName+",\n";
////		}
////		qtext=qtext.substring(0, qtext.length()-2);
//		qtext=qtext+" from "+table.getQualifiedName();
//		
//		PreparedStatement ps=sqlsession.getConn().prepareStatement(qtext);
//		
//		try {
//			ps.setFetchSize(100);
//		} catch (Throwable e) {
//		}
//		
//		final ResultSet rs = ps.executeQuery();
//		final ArrayList<String > columnNames=new ArrayList<String>();
//		ArrayList<List<Object>> rows=new ArrayList<List<Object>>();
//		final ResultSetMetaData metadata=rs.getMetaData();
//		final int columncount=metadata.getColumnCount();
//		
//		for(int i=1;i<=columncount;i++){
//			String label=metadata.getColumnLabel(i);
//			columnNames.add(label);
//		}
//		
//		ResultSetReader reader = new ResultSetReader(rs);
//		Object[]row;
//		final int max=50;
//		int current=0;
//		while((row=reader.readRow())!=null && current<max){
//			rows.add(Arrays.asList(row));
//			current++;
//		}
//		try{rs.close();}catch(Exception e){}
//		request.setAttribute("qtext", qtext);
//		request.setAttribute("rows", rows);
//		request.setAttribute("columnNames", columnNames);
//		request.getRequestDispatcher("/runselect.jsp").forward(request, response);

	}

}
