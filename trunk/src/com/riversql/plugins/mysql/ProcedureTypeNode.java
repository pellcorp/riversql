
package com.riversql.plugins.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.riversql.sql.SQLConnection;
import com.riversql.dbtree.CatalogNode;
import com.riversql.dbtree.IStructureNode;
import com.riversql.plugin.BasePluginType;

public class ProcedureTypeNode extends BasePluginType implements IStructureNode{

	@Override
	public void load(){	
		if(loaded )
			return;
		ResultSet rs=null;
		PreparedStatement ps=null;
		try{
			final String sql="select routine_name from information_schema.routines where routine_schema=? and routine_type='PROCEDURE' order by 1 asc";
			ps=conn.prepareStatement(sql);
			String owner=getOwner();
			ps.setString(1,owner);
			rs=ps.executeQuery();
			while(rs.next()){
				String oname=rs.getString(1);
				ProcedureNode procedureNode=new ProcedureNode(this,oname,conn);
				list.add(procedureNode);
			}
	
		}catch(Exception e){
			list.clear();
		}finally{
			try{if(rs!=null)
				rs.close();
			}catch(Exception e){
			}	
			try{
				if(ps!=null)
					ps.close();
			}catch(Exception e){
			}	
		}		
		loaded=true;
	}
	
	private String getOwner() {
		return parentNode.getName();
	}

	public ProcedureTypeNode(CatalogNode caNode,SQLConnection conn) {
		super("Procedure",caNode,conn);
	}

	public String getCls() {
		
		return "objs";
	}

	public String getType() {
		
		return "mysql_procs";
	}

	public boolean isLeaf() {
		
		return false;
	}

}
