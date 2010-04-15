
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class JavaClassNode extends BasePluginType {

	
	//private String status;

	public JavaClassNode(JavaClassTypeNode javaclassTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,javaclassTypeNode,conn);
		//this.status=status;
		
	}

	 
	@Override
	public void load() {
	}

	public String getCls() {
		return "obj";
	}

	public String getType() {
		
		return "ora_javaclass";
	}

	public boolean isLeaf() {
		return true;
	}

}
