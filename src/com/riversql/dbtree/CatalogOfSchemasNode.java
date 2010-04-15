
package com.riversql.dbtree;

import com.riversql.sql.SQLConnection;

public class CatalogOfSchemasNode extends DBNode implements IStructureNode {


	private String text;
	
	private String[] schemas;
	
	public CatalogOfSchemasNode(String catalogName, String[] schemas,
			SQLConnection conn) {
		super(conn);
		this.text=catalogName;
		this.schemas=schemas;
	}

	

	 
	@Override
	protected void nodeLoad() {
                children.clear();
                if(schemas!=null)
                    for(int i=0;i<schemas.length;i++){
                            SchemaNode sn=new SchemaNode(schemas[i],conn,text);
                            children.add(sn);
                    }
	}

	public String getName() {
		return text;
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
