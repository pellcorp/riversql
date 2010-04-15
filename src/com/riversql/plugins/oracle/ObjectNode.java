
package com.riversql.plugins.oracle;

import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class ObjectNode extends BasePluginType {
	
	//private String status;
	

	public ObjectNode(ObjectTypeNode objectTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,objectTypeNode,conn);
		//this.status=status;
	}
	
	

	 
	@Override
	public void load() {
	}

	public String getCls() {
		return "obj";
	}

	public String getType() {
		return "ora_type";
	}

	public boolean isLeaf() {
		return true;
	}

}
