
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.sql.TableColumnInfo;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.TableNode;


public class GetTableColumnsForAlterTable implements JSONAction {
	String id=null;
	public void setId(String id) {
		this.id = id;
	}

	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		TableNode tn=(TableNode)IDManager.get().get(id);
		TableColumnInfo colsInfo[] = tn.getConn().getSQLMetaData().getColumnInfo(tn.getITableInfo());
		JSONObject ret=new JSONObject();
		JSONArray arr=new JSONArray();
		for(int i=0;i<colsInfo.length;i++){
			JSONObject obj=new JSONObject();
			obj.put("cname",colsInfo[i].getColumnName());
			obj.put("cnameold",colsInfo[i].getColumnName());
			obj.put("ctype", colsInfo[i].getTypeName().toUpperCase());
			obj.put("csize",colsInfo[i].getColumnSize());
			obj.put("cdecdig",colsInfo[i].getDecimalDigits());
			String remarks=colsInfo[i].getRemarks();
			if(remarks==null)
				remarks="";
			obj.put("ccom",remarks);
			String def=colsInfo[i].getDefaultValue();
			if(def==null)
				def="";
			obj.put("cdefault",def);
			String nullable=colsInfo[i].isNullable();
			boolean bnull=true;
			if(nullable!=null)
				bnull=!nullable.toUpperCase().equals("NO");
			obj.put("cacceptnull",bnull);
			obj.put("cacceptnullold",bnull);
			arr.put(obj);
		}
		ret.put("columns",arr);
		return ret;
	}

}
