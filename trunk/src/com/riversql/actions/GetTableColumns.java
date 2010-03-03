
package com.riversql.actions;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.TableNode;




public class GetTableColumns implements JSONAction {

	String id=null;
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		List<String> cols=tn.getColumnNames();
		JSONArray arr=new JSONArray();
		for(int i=0;i<cols.size();i++){
			JSONObject obj=new JSONObject();
			obj.put("cname",cols.get(i));
			arr.put(obj);
		}
		JSONObject ret=new JSONObject();
		ret.put("columns",arr);
		return ret;
	}

}
