
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
import com.riversql.dbtree.TableNode;





public class GenerateDDLAlterTable implements JSONAction {

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(tableid);
		JSONArray arrChangedCols=new JSONArray(changedCols);
		JSONArray arrNewCols=new JSONArray(newCols);
		JSONArray arrDroppedCols=new JSONArray(droppedCols);
		JSONArray arrDroppedIdxs=new JSONArray(droppedIdx);
		JSONArray arrDroppedPKs=new JSONArray(droppedPKs);
		JSONArray arrNewIdxs=new JSONArray(newIdxs);
		JSONArray arrNewPKs=new JSONArray(newPKs);
		
	
		ISyntaxGenerator syntaxGen=DBSyntaxGeneratorFactory.getSyntaxGenerator(tn.getConn());
		
		JSONObject resultObj=new JSONObject();
		String qualifiedName=tn.getITableInfo().getQualifiedName();
                StringBuilder strbuilder=new StringBuilder();
                if(syntaxGen!=null){

                        for(int i=0;i<arrDroppedPKs.length();i++){
                                String droppedPK=arrDroppedPKs.getString(i);
                                syntaxGen.dropPK(strbuilder, qualifiedName, droppedPK);
                        }

                        for(int i=0;i<arrDroppedIdxs.length();i++){
                                String droppedIndex=arrDroppedIdxs.getString(i);
                                syntaxGen.dropIndex(strbuilder, qualifiedName, droppedIndex);
                        }
                        for(int i=0;i<arrDroppedCols.length();i++){
                                String droppedCol=arrDroppedCols.getString(i);
                                syntaxGen.dropColumn(strbuilder, qualifiedName, droppedCol);
                        }

                        for(int i=0;i<arrChangedCols.length();i++){
                                JSONObject changedObj=arrChangedCols.getJSONObject(i);
                                String oldColumnName=changedObj.getString("cnameold");
                                String columnName=changedObj.getString("cname");
                                String ctype=changedObj.getString("ctype");
                                String csize=changedObj.getString("csize");
                                String cdecdig=changedObj.getString("cdecdig");
                                boolean acceptNull=changedObj.getBoolean("cacceptnull");
                                boolean acceptNullOld=changedObj.getBoolean("cacceptnullold");
                                String remarks=changedObj.getString("ccom");
                                String def=changedObj.getString("cdefault");

                                if(oldColumnName!=null && !oldColumnName.equals(columnName)){
                                        syntaxGen.renameColumn(strbuilder,qualifiedName,oldColumnName,columnName);
                                }
                                syntaxGen.changeColumn(strbuilder, qualifiedName, oldColumnName, columnName, ctype, csize, cdecdig, remarks, def, acceptNull, acceptNullOld);

                        }
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
	
	public void setTableid(String tableid) {
		this.tableid = tableid;
	}
	String changedCols;
	String newCols;
	String droppedCols;
	String droppedIdx;
	String newIdxs;
	String newPKs;
	String droppedPKs;

	public void setNewPKs(String newPKs) {
		this.newPKs = newPKs;
	}
	public void setNewIdxs(String newIdxs) {
		this.newIdxs = newIdxs;
	}
	public void setDroppedIdx(String droppedIdx) {
		this.droppedIdx = droppedIdx;
	}
	public void setDroppedCols(String droppedCols) {
		this.droppedCols = droppedCols;
	}
	
	public void setChangedCols(String changedCols) {
		this.changedCols = changedCols;
	}

	public void setNewCols(String newCols) {
		this.newCols = newCols;
	}
	public void setDroppedPKs(String droppedPKs) {
		this.droppedPKs = droppedPKs;
	}
}
