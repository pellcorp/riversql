
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class ProcedureNode extends BasePluginType {

	
	//private String status;
	

	public ProcedureNode(ProcedureTypeNode procedureTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,procedureTypeNode,conn);
		
		//this.status=status;
		
	}
	
	 
	@Override
	public String getName() {
		return name;
	}
	

	 
	@Override
	public void load() {
	}
	public String getCls() {
	
		return "obj";
	}
	public String getType() {
		return "ora_proc";
	}
	public boolean isLeaf() {
		
		return true;
	}

}
