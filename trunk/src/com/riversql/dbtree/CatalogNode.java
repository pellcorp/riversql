
package com.riversql.dbtree;

import java.sql.SQLException;

import com.riversql.sql.SQLConnection;
import com.riversql.plugin.PluginManager;

public class CatalogNode extends DBNode implements IStructureNode{

	String catalogName;
	private boolean dummy;
	public CatalogNode( String catalogName,
			SQLConnection conn) {
		
		this(catalogName,conn,false);
	}
	
	public CatalogNode( String catalogName,
			SQLConnection conn,boolean dummy) {
		super(conn);
		this.dummy=dummy;
		this.catalogName=catalogName;
	}
	
	
	 
	@Override
	public void nodeLoad() throws SQLException{
		children.clear();
		String[] tbTypes = conn.getSQLMetaData().getTableTypes();
		for (int i = 0; i < tbTypes.length; ++i) {
			String tableType = tbTypes[i];
                        if(tableType.equalsIgnoreCase("TABLE"))
                        {
                            tableType = "Table";
                        }
                        else if(tableType.equalsIgnoreCase("VIEW"))
                        {
                            tableType = "View";
                        }
                        else if(tableType.equalsIgnoreCase("LOCAL TEMPORARY"))
                        {
                            tableType = "Temporary";
                        }
			children.add(new TablesNode(this, tableType, conn));
		}
		PluginManager pm=PluginManager.getInstance();
		pm.loadCatalogChildren(this,children,conn);
	}

	public String getName() {
		return catalogName;
	}
	
	public boolean isDummy() {
		return dummy;
	}

	public String getCls() {
		return "catalog";
	}

	public String getType() {
		
		return "ct";
	}

	public boolean isLeaf() {
		return false;
	}
}
