
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class PackageBodyNode extends BasePluginType {

	
	//private String status;
	

	public PackageBodyNode(PackageBodyTypeNode packageTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,packageTypeNode,conn);
	
		//this.status=status;
	}

	
	

	 
	@Override
	public void load() {
	}

	public String getCls() {
		return "packageb";
	}

	public String getType() {
		
		return "ora_pkgbody";
	}

	public boolean isLeaf() {
		return true;
	}

}
