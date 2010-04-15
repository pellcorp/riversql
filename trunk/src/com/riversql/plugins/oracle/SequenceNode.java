
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class SequenceNode extends BasePluginType {

	
	//private String status;
	

	public SequenceNode(SequenceTypeNode sequenceTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,sequenceTypeNode,conn);
		//this.status=status;
	}
	

	 
	@Override
	public void load() {
	}
	public String getCls() {
		return "obj";
	}
	public String getType() {
		return "ora_seq";
	}
	public boolean isLeaf() {
		return true;
	}

}
