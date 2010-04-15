
package com.riversql.plugins.oracle;


import com.riversql.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class TriggerNode extends BasePluginType {
	
	//private String status;

	public TriggerNode(TriggerTypeNode triggerTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,triggerTypeNode,conn);
	//	this.status=status;
		
	}
	

	 
	@Override
	public void load() {
	}

	public String getCls() {
		return "trg";
	}

	public String getType() {
		return "ora_trig";
	}

	public boolean isLeaf() {
		return true;
	}

}
