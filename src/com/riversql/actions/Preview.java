
package com.riversql.actions;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.TableNode;
import com.riversql.utils.ResultSetReader;


public class Preview  implements JSONAction {
	
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
		
		Object obj=IDManager.get().get(id);
		if(obj!=null && obj instanceof TableNode){
			TableNode table=((TableNode)obj);
			Connection conn=table.getConn().getConnection();
			try{
				String sql="select * from "+table.getITableInfo().getQualifiedName();
				PreparedStatement ps=conn.prepareStatement(sql);
				ps.setMaxRows(20);
				ResultSet rs=ps.executeQuery();
				if(rs!=null){
					ResultSetMetaData metadata=rs.getMetaData();
					int columncount=metadata.getColumnCount();
					
					for(int i=1;i<=columncount;i++){
						
						String label=metadata.getColumnLabel(i);
						meta.put(label);
					}
					ResultSetReader reader=new ResultSetReader(rs);
					//dataset.setResultSet(rs);
					Object[]row;
					while((row=reader.readRow())!=null){
						JSONArray record=new JSONArray();
						for(int i=0;i<columncount;i++){
							Object obj1=row[i];
							record.put(obj1);
						}
						data.put(record);
					}
					rs.close();
				}
				ps.close();
			}catch(Exception e){
				
			}
		}
		results.put("meta",meta);
		results.put("data",data);
		return results;
	}

	

}
