
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.DatabaseNode;
import com.riversql.dbtree.IStructureNode;
import com.riversql.dbtree.SQLSession;


public class GetTree implements JSONAction {
	String node, refresh;
	String dbid;
	public void setDbid(String dbid) {
		this.dbid = dbid;
	}
	private boolean refreshing;
	public void setNode(String node) {
		this.node = node;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, 
			EntityManager em, EntityTransaction et) throws Exception {
	
		
		if("root".equals(node)){
			
			SQLSession sqlsession=null;
			if(dbid!=null && !dbid.equals("")){
				
				sqlsession=(SQLSession)IDManager.get().get((dbid));
				if(sqlsession==null){
					return null;
					//sqlsession = createSQLSession(request, em,dbid);
					//sessions.getSqlsessions().add(new SQLSession(alias.getAliasname(),conn));
				}
			}else{
				//JSONObject obj=new JSONObject();
				
				JSONArray arr=new JSONArray();
				//arr.put(obj);
				JSONObject ret = new JSONObject();
				ret.put("nodes", arr);
				return ret;
			}
			
			DatabaseNode dn=sqlsession.getDatabaseNode();
			if(refreshing)
				dn.refresh();
			return dn.toJSON();

		}
		else{
			String id=node;
			Object obj=IDManager.get().get(id);
			if(obj!=null){
				if(obj instanceof IStructureNode){
					if(refreshing)
						((IStructureNode)obj).refresh();
					JSONObject js=((IStructureNode)obj).getChildrenToJSon();
					return js;
				}
			}
		}
		return null;
	}
//	public static SQLSession createSQLSession(HttpServletRequest request,
//			EntityManager em, String dbid) throws ClassNotFoundException,
//			ValidationException, SQLException {
//		SQLSession sqlsession;
//		System.out.println("create session for "+"db"+dbid);
//		String driverName=null;
//		String url=null;
//		String user=null;
//		String pwd=null;
//		String dbname=null;
//		if(dbid.startsWith("S")){
//			Source s=SourcesDAO.getSource(em,Integer.parseInt(dbid.substring(1)));
//			driverName=s.getDriverName();
//			url=s.getJdbcUrl();
//			user=s.getUserName();
//			pwd=s.getPassword();
//			dbname=s.getSourceName();
//			
//		}
//		ClassLoader lod=Thread.currentThread().getContextClassLoader();
//		try{
//			Class.forName(driverName);
//			ISQLDriver idriver=new com.riversql.sql.SQLDriver();
//			idriver.setDriverClassName(driverName);
//			Connection _conn = DriverManager.getConnection(url,user,pwd);
//			SQLConnection conn=new SQLConnection(_conn,null,idriver);
////			try {if(driverName.indexOf("oracle")==-1)
////				_conn.setTransactionIsolation(Connection.TRANSACTION_READ_UNCOMMITTED);
////			}catch(Throwable e){e.printStackTrace();
////			}
//			
//			sqlsession=new SQLSession(dbname,conn);
//			
//			
//			//if(autocommit!=null){
//				//_conn.setAutoCommit(true);
//			//}else{
//				_conn.setAutoCommit(false);
//			//}
//			
//			HttpSession session = request.getSession(true);
//			WebSQLSession sessions=(WebSQLSession)session.getAttribute("sessions");
//			sessions.getSqlsessions().add(sqlsession);
//		}finally{
//			Thread.currentThread().setContextClassLoader(lod);
//		}
//		 
//		IDManager.get().put(("db"+dbid), sqlsession);
//		
//		return sqlsession;
//	}
	public void setRefresh(String refresh) {
		this.refresh = refresh;
		this.refreshing=refresh!=null && "true".equals(refresh);
	}

}
