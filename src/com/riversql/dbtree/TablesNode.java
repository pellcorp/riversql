
package com.riversql.dbtree;

import java.sql.SQLException;

import com.riversql.sql.ITableInfo;
import com.riversql.sql.SQLConnection;

public class TablesNode extends DBNode implements IStructureNode{

	
	private String tableType;
	private IStructureNode parent;
	String catName = null, schemaName = null;
	public TablesNode(IStructureNode parent,String tableType, SQLConnection conn) {
		super(conn);
		
		this.tableType=tableType;
	
		this.parent=parent;
		if(parent instanceof CatalogNode) {
			if(!((CatalogNode)parent).isDummy())
				catName = parent.getName();
		} else if(parent instanceof SchemaNode) {
			schemaName = parent.getName();
			catName=((SchemaNode)parent).getCatalogName();
		}
	}

	 
	@Override
	public void nodeLoad()throws SQLException{
                children.clear();
		ITableInfo[] tables = conn.getSQLMetaData().getTables(catName,
			schemaName, "%", new String[]{tableType},null); 

		for(int i=0;i<tables.length;i++){
			children.add(new
				TableNode(this,tables[i].getSimpleName(),tables[i].getRemarks(),tables[i],conn));
		}
	}


	public String getName() {
		return tableType;
	}
	
	public IStructureNode getParent() {
		return parent;
	}

	public String getTableType() {
		return tableType;
	}

	public String getCls() {
		
		if(tableType.toUpperCase().indexOf("VIEW")>-1){
			return "views";
		}else if(tableType.toUpperCase().indexOf("TABLE")>-1){
			return "tables";
		}else{
			return "temporary";
		}
	}

	public String getType() {
		if(tableType.toUpperCase().indexOf("VIEW")>-1){
			return "views";
		}else if(tableType.toUpperCase().indexOf("TABLE")>-1){
			return "tbs";
		}else{
			return "temptable";
		}
	}

	public boolean isLeaf() {
		return false;
	}


}
