
package com.riversql.dbtree;

import java.sql.SQLException;

import com.riversql.sql.SQLConnection;
import com.riversql.plugin.PluginManager;

public class SchemaNode extends DBNode implements IStructureNode{

	String schemaName;
	
	private String catalogName;
	public SchemaNode( String schemaName,
			SQLConnection conn) {
		this(schemaName,conn,null);
	}
	public SchemaNode(String schemaName, SQLConnection conn, String catalogName) {
		super(conn);
		this.schemaName=schemaName;
		this.catalogName=catalogName;
	}

	
	 
	@Override
	protected void nodeLoad() throws SQLException{
		children.clear();
		String[] tbTypes = conn.getSQLMetaData().getTableTypes();
		for (int i = 0; i < tbTypes.length; ++i) {
			String tableType = tbTypes[i];
			children.add(new TablesNode(this, tableType, conn));
		}
		PluginManager pm=PluginManager.getInstance();
		pm.loadSchemaChildren(this,children,conn);
		
	}
	
	public String getName() {
		return schemaName;
	}
	
	public String getCatalogName() {
		return catalogName;
	}
	public String getCls() {
		return "schema";
	}
	public String getType() {
		return "sc";
	}
	public boolean isLeaf() {
		return false;
	}
}
