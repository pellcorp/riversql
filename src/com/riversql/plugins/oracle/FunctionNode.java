
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class FunctionNode extends BasePluginType {

	//private String status;

	public FunctionNode(FunctionTypeNode functionTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,functionTypeNode,conn);
		//this.status=status;
		
	}
	
	
	 
	@Override
	public void load() {
	}

	public String getCls() {
		return "obj";
	}

	public String getType() {
		
		return "ora_funct";
	}

	public boolean isLeaf() {
		
		return true;
	}

}
