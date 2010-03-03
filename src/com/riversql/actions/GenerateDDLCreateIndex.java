
package com.riversql.actions;

//import java.util.Arrays;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.WebSQLSession;


public class GenerateDDLCreateIndex implements JSONAction {
String tableid;
	
	public void setTableid(String tableid) {
	this.tableid = tableid;
}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, WebSQLSession sessions,
			EntityManager em, EntityTransaction et) throws Exception {
//		TableNode tn=(TableNode)IDManager.get().get(tableid);
//		
//		String[] idxKeys = request.getParameterValues("idxKeys");
//		System.out.println(Arrays.asList(idxKeys));
//	
		JSONObject obj=new JSONObject();
		
//			if(idxKeys==null || idxKeys.length==0)
//				throw new Exception("No Columns have been selected");
//			boolean unique=request.getParameter("unique")!=null;
//			String indexName=request.getParameter("name");
//			
//			String sql="DDL not implemented";
//			SQLConnection conn=tn.getConn();
//			String tableQualifiedName=tn.getITableInfo().getQualifiedName();
//			if(DialectFactory.isOracle(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isMySQL(conn)){
//				sql=buildMySQLString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isMSSQL(conn)){
//				
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isPostgreSQL(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isDerby(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isHssql(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}else if(DialectFactory.isH2(conn)){
//				sql=buildOracleString(idxKeys, tableQualifiedName, unique, indexName);
//			}
//			
//			
//			obj.put("ddl", sql);
		
		return obj;
	}
//	private String buildOracleString(String [] idxKeys, String qualifiedName, boolean unique, String indexName){
//		StringBuilder sbuilder=new StringBuilder();
//		sbuilder.append("CREATE ");
//		if(unique){
//			sbuilder.append("UNIQUE ");
//		}
//		sbuilder.append("INDEX ");
//		sbuilder.append(indexName);
//		sbuilder.append(" ON ").append(qualifiedName);
//		sbuilder.append(" (");
//		for(int j=0;j<idxKeys.length;j++){
//			sbuilder.append(idxKeys[j]);
//			if(j<idxKeys.length-1)
//				sbuilder.append(",");
//		}
//		sbuilder.append(")");
//		return sbuilder.toString();
//	}
//	private String buildMySQLString(String [] idxKeys, String qualifiedName, boolean unique, String indexName){
//		StringBuilder sbuilder=new StringBuilder();
//		sbuilder.append("ALTER TABLE ");
//		
//		sbuilder.append(qualifiedName);
//		sbuilder.append(" ADD " );
//		if(unique){
//			sbuilder.append("UNIQUE ");
//		}else{
//			sbuilder.append("INDEX ");
//		}
//		
//		sbuilder.append(indexName);
//		sbuilder.append(" (");
//		for(int j=0;j<idxKeys.length;j++){
//			sbuilder.append(idxKeys[j]);
//			if(j<idxKeys.length-1)
//				sbuilder.append(",");
//		}
//		sbuilder.append(")");
//		return sbuilder.toString();
//	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		// TODO Auto-generated method stub
		return null;
	}
}
