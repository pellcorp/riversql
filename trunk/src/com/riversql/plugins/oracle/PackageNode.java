
package com.riversql.plugins.oracle;


import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import com.riversql.plugin.BasePluginType;

public class PackageNode extends BasePluginType {

	
	//private String status;

	public PackageNode(PackageTypeNode packageTypeNode, String name,
			SQLConnection conn, String status) {
		super(name,packageTypeNode,conn);
		
		//this.status=status;
		
	}


	 
	@Override
	public void load() {
	}

	public String getCls() {
		
		return "package";
	}

	public String getType() {
		
		return "ora_pkg";
	}

	public boolean isLeaf() {
		
		return true;
	}

}
