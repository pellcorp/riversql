
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;

public class JavaNode extends com.riversql.plugin.BasePluginType {

	
	//private String status;

	public JavaNode(JavaTypeNode javaTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,javaTypeNode,conn);
		//this.status=status;
		
	}

	 
	@Override
	public void load() {
	}

	public String getCls() {
		
		return "obj";
	}

	public String getType() {
		
		return "ora_java";
	}

	public boolean isLeaf() {
		return true;
	}

}
