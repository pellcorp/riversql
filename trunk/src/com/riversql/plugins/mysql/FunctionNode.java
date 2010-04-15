
package com.riversql.plugins.mysql;

import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class FunctionNode extends BasePluginType {

	public FunctionNode(FunctionTypeNode functionTypeNode, String name,
			SQLConnection conn) {
		super(name,functionTypeNode,conn);
	}
	
	
	@Override
	public void load() {
	}

	public String getCls() {
		return "obj";
	}

	public String getType() {
		
		return "mysql_funct";
	}

	public boolean isLeaf() {
		
		return true;
	}

}
