
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class ObjectBodyNode extends BasePluginType {
	
	//private String status;
	

	public ObjectBodyNode(ObjectTypeBodyNode objectTypeBodyNode, String name,
			SQLConnection conn, String status) {
		super(name,objectTypeBodyNode,conn);
		//this.status=status;
	}
	

	 
	@Override
	public void load() {
	}

	public String getCls() {
		
		return "obj";
	}

	public String getType() {
		
		return "ora_bodytype";
	}

	public boolean isLeaf() {
		
		return true;
	}

}
