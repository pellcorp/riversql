
package com.riversql.plugins.oracle;

import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class JavaResourceNode extends BasePluginType {

	
	//private String status;

	public JavaResourceNode(JavaResourceTypeNode javaResTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,javaResTypeNode,conn);
		//this.status=status;
		
	}


	 
	@Override
	public void load() {
	}

	public String getCls() {
		
		return "obj";
	}

	public String getType() {
		
		return "ora_javares";
	}

	public boolean isLeaf() {
		return true;
	}

}
