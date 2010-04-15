
package com.riversql.actions;

import java.util.Iterator;
import java.util.TreeSet;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.sql.DataTypeInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.DBNode;


public class GetTypesAlterTable implements JSONAction {
	String id=null;
	public void setId(String id) {
		this.id = id;
	}
	

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		DBNode dbNode=(DBNode)IDManager.get().get(id);
		DataTypeInfo[] dataTypeInfos = dbNode.getConn().getSQLMetaData().getDataTypes();
		JSONArray arr=new JSONArray();
		TreeSet<String> set=new TreeSet<String>();
		for(int i=0;i<dataTypeInfos.length;i++){
			//JSONObject obj=new JSONObject();
			set.add(dataTypeInfos[i].getSimpleName());
			//arr.put(obj);
		}
		for (Iterator<String> iterator = set.iterator(); iterator.hasNext();) {
			String type = iterator.next();
			JSONObject obj=new JSONObject();
			obj.put("tp",type);
			arr.put(obj);
		}
		JSONObject ret=new JSONObject();
		ret.put("types",arr);
		return ret;
	}

}
