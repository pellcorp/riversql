
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
import com.riversql.dbtree.ColumnModel;
import com.riversql.dbtree.IStructureNode;
import com.riversql.dbtree.TableNode;
import com.riversql.dbtree.TablesNode;
import com.riversql.plugin.BasePluginType;




public class GetMeta implements JSONAction {
	String id;
	
	public void setId(String id) {
		this.id = id;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		
		
		
		//String id=request.getParameter("id");
		Object obj=IDManager.get().get(id);
		if(obj!=null && obj instanceof TableNode){
			String []strs={"Column Name","Data Type","Type Name","Size","Decimal Digits","Default Value","Accept Null Value","Remarks"};
			
			for(int i=0;i<strs.length;i++){
				meta.put(strs[i]);	
			}
			TableNode table=((TableNode)obj);
			try {
				List<ColumnModel>listCols=table.getColumns();
				for (ColumnModel columnModel : listCols) {
					JSONArray record=new JSONArray();
					for(int j=0;j<8;j++){
						record.put(columnModel.getValue(j));
					}
					
					data.put(record);
				}
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}else if(obj!=null && obj instanceof TablesNode){
			meta.put("Name");
			meta.put("Remarks");
			TablesNode tables=((TablesNode)obj);
			List<IStructureNode> ls=tables.getChildren();
			for (IStructureNode tableNode : ls) {
				JSONArray record=new JSONArray();
				record.put(tableNode.getName());
				String remarks = ((TableNode)tableNode).getRemarks();
				if(remarks==null)
					remarks="";
				record.put(remarks);
				data.put(record);
			}
		}else if(obj!=null && (obj instanceof com.riversql.plugins.mysql.UsersNode || obj instanceof com.riversql.plugins.mysql.FunctionTypeNode || obj instanceof com.riversql.plugins.mysql.ProcedureTypeNode || obj instanceof com.riversql.plugins.mysql.TriggersNode)){
			meta.put("Name");
			meta.put("Remarks");
			BasePluginType baseType=((BasePluginType)obj);
			List<IStructureNode> ls=baseType.getChildren();
			for (IStructureNode baseChildType : ls) {
				JSONArray record=new JSONArray();
				record.put(baseChildType.getName());
				String remarks = "";
				record.put(remarks);
				data.put(record);
			}
		}
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}

}
