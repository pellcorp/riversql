
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





public class GetPKForAlterTable implements JSONAction {
	String id=null;
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		String pkName=tn.getPkName();
		List<String> pkColumnList=tn.getPrimaryKeyColumns();
		boolean hasPK=pkColumnList.size()>0;
		JSONArray arr=new JSONArray();
		JSONObject ret=new JSONObject();
		//for (Iterator<String> iterator = mp.keySet().iterator(); iterator.hasNext();) {
		if(hasPK){
			JSONObject obj=new JSONObject();
			obj.put("pkname", pkName);
			JSONArray colArray=new JSONArray();
			for(int i=0;i<pkColumnList.size();i++){
				colArray.put(pkColumnList.get(i));
			}
			obj.put("cols", colArray);
			arr.put(obj);
			ret.put("pk",arr);
		}
		//}
		return ret;
	}

}
