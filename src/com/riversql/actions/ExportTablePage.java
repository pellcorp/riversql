
package com.riversql.actions;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Random;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.sql.SQLConnection;
import com.riversql.IDManager;
import com.riversql.IPageAction;
import com.riversql.dbtree.TableNode;

public class ExportTablePage implements IPageAction {
	String id;
	public void setId(String id) {
		this.id = id;
	}
	public void execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		request.setAttribute("qname", tn.getQualifiedName());
		String sql="SELECT COUNT(*) FROM "+tn.getQualifiedName();
		SQLConnection conn = tn.getConn();
		PreparedStatement ps=null;
		ResultSet rs=null;
		long count=0;
		try{
			ps=conn.prepareStatement(sql);
			rs = ps.executeQuery();
			rs.next();
			count=rs.getLong(1);
			
		}catch(Exception e){
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
		request.setAttribute("rowCount", count);
		request.setAttribute("rnd",new Random().nextInt());
		request.getRequestDispatcher("exportTable.jsp").forward(request, response);

	}

}
