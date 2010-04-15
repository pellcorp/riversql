
package com.riversql.plugins.mysql;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.riversql.sql.SQLConnection;
import com.riversql.dbtree.CatalogNode;
import com.riversql.dbtree.IStructureNode;
import com.riversql.plugin.BasePluginType;

public class FunctionTypeNode extends BasePluginType implements IStructureNode{

	@Override
	public void load(){	
		if(loaded )
			return;
		ResultSet rs=null;
		PreparedStatement ps=null;
		try{
			final String sql="select routine_name from information_schema.routines where routine_schema=? and routine_type='FUNCTION' order by 1 asc";
			ps=conn.prepareStatement(sql);
			String owner=getOwner();
			ps.setString(1,owner);
			rs=ps.executeQuery();
			while(rs.next()){
				String oname=rs.getString(1);
				FunctionNode functNode=new FunctionNode(this,oname,conn);
				list.add(functNode);
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

	public FunctionTypeNode(CatalogNode caNode,SQLConnection conn) {
		super("Function",caNode,conn);
	}

	public String getCls() {
		
		return "objs";
	}

	public String getType() {
		
		return "mysql_functs";
	}

	public boolean isLeaf() {
		
		return false;
	}

}
