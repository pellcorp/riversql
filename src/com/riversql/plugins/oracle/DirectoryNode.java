
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class DirectoryNode extends BasePluginType {

	
	//private String status;

	public DirectoryNode(DirectoryTypeNode dirTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,dirTypeNode,conn);
		//this.status=status;
		
	}

	 
	@Override
	public void load() {
	}

	public String getCls() {
		
		return "obj";
	}

	public String getType() {
		
		return "ora_directory";
	}

	public boolean isLeaf() {
		
		return true;
	}

	

}
