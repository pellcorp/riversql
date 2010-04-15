
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;

public class LibraryNode extends com.riversql.plugin.BasePluginType {

	
	//private String status;

	public LibraryNode(LibraryTypeNode libraryTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,libraryTypeNode,conn);
		//this.status=status;
		
	}


	 
	@Override
	public void load() {
	}

	public String getCls() {
		
		return "obj";
	}

	public String getType() {
		
		return "ora_library";
	}

	public boolean isLeaf() {
		return true;
	}

}
