
package com.riversql.dbtree;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

import com.riversql.sql.SQLConnection;
import com.riversql.sql.SQLDatabaseMetaData;
import com.riversql.sql.SQLDatabaseMetaData.IDBMSProductNames;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DatabaseNode extends DBNode implements IStructureNode{

	 
	@Override
	public void refresh() {
		init();
		super.refresh();
	}
	private boolean supportsCatalogs;
	private boolean supportsSchemas;
	
	private String sessionName;
	private String [] catalogs;
	public DatabaseNode(SQLConnection conn, String sessionName){
		super(conn);
		this.sessionName=sessionName;
		init();
	}
	private void init() {
		SQLDatabaseMetaData metadata = conn.getSQLMetaData();
		
		try {
			 supportsCatalogs = metadata.supportsCatalogs();
		} catch (SQLException ex) {
		}

		supportsSchemas = false;
		try {
			supportsSchemas = metadata.supportsSchemas();
		}catch(SQLException e){
		}
		if(supportsCatalogs){
			catalogs =null;
			try{
				 catalogs =metadata.getCatalogs();
				 if(catalogs==null || catalogs.length==0)
					 supportsCatalogs=false;
			}
			catch(Exception e){
				supportsCatalogs=false;
			}
		}
	}
	public String []getCatalogs(){
		return catalogs;
	}
	public boolean supportsCatalogs() {
		return supportsCatalogs;
	}
	public boolean supportsSchemas() {
		return supportsSchemas;
	}
	 
	@Override
	protected void nodeLoad()throws SQLException{
		children.clear();
		SQLDatabaseMetaData metadata = conn.getSQLMetaData();
		
		if (supportsCatalogs && supportsSchemas) { 
			
			if(catalogs!=null){
				String[] schemas = metadata.getSchemas();
				String dbname=metadata.getDatabaseProductName();
				if(dbname.equals(IDBMSProductNames.MICROSOFT_SQL)){
					ArrayList<String> tmpList=new ArrayList<String>();
					tmpList.addAll(Arrays.asList(schemas));
					if(tmpList.indexOf("sys")==-1)
						tmpList.add("sys");
					schemas=tmpList.toArray(new String[tmpList.size()]);
				}
				
				for (int i = 0; i < catalogs.length; ++i) {
					final String catalogName =  catalogs[i];
					children.add(new CatalogOfSchemasNode(catalogName,schemas,conn));
	
				}
			}else{
				if (supportsSchemas){
					final String[] schemas = metadata.getSchemas();
					for (int i = 0; i < schemas.length; ++i) {
						final String schemaName = schemas[i];				
						children.add(new SchemaNode(schemaName,conn));
					}
				}
			}
			
		} else if (supportsCatalogs && !supportsSchemas){
			for (int i = 0; i < catalogs.length; ++i) {
				final String catalogName =  catalogs[i];
				children.add(new CatalogNode(catalogName,conn));
			}
		}
		else if (supportsSchemas) {
			final String[] schemas = metadata.getSchemas();
			for (int i = 0; i < schemas.length; ++i) {
				final String schemaName = schemas[i];				
				children.add(new SchemaNode(schemaName,conn));
			}
		}else {
			children.add(new CatalogNode("-",conn,true));
		}
		
	}
		
	public String getName() {
		return sessionName;
	}
	public JSONObject toJSON() throws JSONException{
		JSONObject obj=new JSONObject();
		obj.put("text", getName() );
		obj.put("id",getId());
		obj.put("leaf",isLeaf());
		obj.put("type",getType());
		obj.put("cls",getCls());
		
		JSONArray arr=new JSONArray();
		arr.put(obj);
		JSONObject ret = new JSONObject();
		ret.put("nodes", arr);
		
		return ret;
	}
	public String getType() {
		return "dtbs";
	}
	public String getCls() {
		return "database";

	}
	public boolean isLeaf() {
		return false;
	}
}
