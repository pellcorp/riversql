
package com.riversql.actions;


import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.databases.DBSyntaxGeneratorFactory;
import com.riversql.databases.ISyntaxGenerator;
import com.riversql.dbtree.DBNode;





public class GenerateDDLCreateTable implements JSONAction {

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		DBNode dbNode=(DBNode)IDManager.get().get(tableid);
		JSONArray arrNewCols=new JSONArray(newCols);
		JSONArray arrNewIdxs=new JSONArray(newIdxs);
		JSONArray arrNewPKs=new JSONArray(newPKs);
		

		ISyntaxGenerator syntaxGen=DBSyntaxGeneratorFactory.getSyntaxGenerator(dbNode.getConn());

		JSONObject resultObj=new JSONObject();
		String qualifiedName=tablename;
                
                StringBuilder strbuilder=new StringBuilder();
                strbuilder.append("Create Table ").append(schemaname).append(".").append(tablename);
                if(syntaxGen!=null){

                        for(int i=0;i<arrNewCols.length();i++){
                                JSONObject newObj=arrNewCols.getJSONObject(i);
                                String columnName=newObj.getString("cname");
                                String ctype=newObj.getString("ctype");
                                String csize=newObj.getString("csize");
                                String cdecdig=newObj.getString("cdecdig");
                                boolean acceptNull=newObj.getBoolean("cacceptnull");
                                String remarks=newObj.getString("ccom");
                                String def=newObj.getString("cdefault");
                                syntaxGen.newColumn(strbuilder, qualifiedName, columnName, ctype, csize, cdecdig, remarks, def, acceptNull);
                        }

                        for(int i=0;i<arrNewPKs.length();i++){
                                JSONObject newObj=arrNewPKs.getJSONObject(i);
                                String pkname=newObj.getString("pkname");
                                JSONArray cols=newObj.getJSONArray("cols");

                                List<String> lsCols=new ArrayList<String>();
                                for( int j=0;j<cols.length();j++){
                                        lsCols.add(cols.getString(j).trim());
                                }
                                syntaxGen.newPK(strbuilder,qualifiedName,pkname,lsCols);
                        }
                        for(int i=0;i<arrNewIdxs.length();i++){
                                JSONObject newObj=arrNewIdxs.getJSONObject(i);
                                String iName=newObj.getString("iname");
                                boolean unique=newObj.getBoolean("unique");
                                JSONArray cols=newObj.getJSONArray("cols");

                                List<String> lsCols=new ArrayList<String>();
                                for( int j=0;j<cols.length();j++){
                                        lsCols.add(cols.getString(j).trim());
                                }
                                syntaxGen.newIndex(strbuilder,qualifiedName,iName,unique,lsCols);
                        }



                }else{
                        strbuilder.append("DDL not yet implemented for this database");
                }

                resultObj.put("ddl", strbuilder.toString());
		return resultObj;
	}
	String tableid;
        String schemaname;
        String tablename;

	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
        
        public void setSchemaName(String schemaname) {
		this.schemaname = schemaname;
	}

        public void setTablename(String tablename) {
		this.tablename = tablename;
	}

	String newCols;
	String newIdxs;
	String newPKs;

	public void setNewPKs(String newPKs) {
		this.newPKs = newPKs;
	}
	public void setNewIdxs(String newIdxs) {
		this.newIdxs = newIdxs;
	}

	public void setNewCols(String newCols) {
		this.newCols = newCols;
	}
}
