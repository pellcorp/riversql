
package com.riversql.actions;

import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.TreeMap;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.TableNode;



public class GetTableIndexesForAlterTable implements JSONAction {
	String id=null;
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		String pkName=tn.getPkName();
		
		ResultSet rs=null;
		class IndexInfo{
			
			boolean unique;
			List<String> ls=new ArrayList<String>();
		}
		TreeMap<String,IndexInfo> mp=new TreeMap<String,IndexInfo>();
		try{
			rs=tn.getIndexes();
			while(rs.next()){
				boolean nonUnique=rs.getBoolean("NON_UNIQUE");
				String column_name=rs.getString("COLUMN_NAME");
				String index_name=rs.getString("INDEX_NAME");
				if(index_name!=null){
					if(index_name.equals(pkName))
						continue;
					IndexInfo ii =mp.get(index_name);
					if(ii==null){
						ii =new IndexInfo();
						
						ii.unique=!nonUnique;
						mp.put(index_name,ii);
					}
					ii.ls.add(column_name);
				}
			}
		}finally{
			try {
				if(rs!=null){
					Statement st=rs.getStatement();
					rs.close();
					if(st!=null)
						st.close();
				}
			} catch (Exception e) {
				
			}
		}
		
		JSONArray arr=new JSONArray();
		for (Iterator<String> iterator = mp.keySet().iterator(); iterator.hasNext();) {
			String key=iterator.next();
			IndexInfo ii=mp.get(key);
			JSONObject obj=new JSONObject();
			obj.put("iname", key);
			obj.put("unique", ii.unique);
			JSONArray colArray=new JSONArray();
			for(int i=0;i<ii.ls.size();i++){
				colArray.put(ii.ls.get(i));
			}
			obj.put("cols", colArray);
			arr.put(obj);
		}
		JSONObject ret=new JSONObject();
		ret.put("indexes", arr);
		return ret;
	}

}
