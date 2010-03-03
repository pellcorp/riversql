
package com.riversql.plugins.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import com.riversql.dbtree.SchemaNode;
import com.riversql.plugin.BasePluginType;

public class PackageBodyTypeNode extends BasePluginType {


	public PackageBodyTypeNode(SchemaNode schemaNode,SQLConnection conn) {
		super("Package Body",schemaNode,conn);
	}

	private String getOwner() {
		return parentNode.getName();
	}
	
	 
	@Override
	public void load() {
		if(loaded )
			return;
		ResultSet rs=null;
		PreparedStatement ps=null;
		try{
			final  String sql="SELECT  object_name,status, created,last_ddl_time,timestamp "+  
			" FROM sys.all_objects where owner=? "+  
			" and object_type='PACKAGE BODY' order by 1 asc";
			ps=conn.prepareStatement(sql);
			String owner=getOwner();
			ps.setString(1,owner);
			rs=ps.executeQuery();
			while(rs.next()){
				String name2=rs.getString(1);
				String status=rs.getString(2);
				PackageBodyNode pkNode=new PackageBodyNode(this,name2,conn,status);
				list.add(pkNode);
			}
	
		}catch(Exception e){
			list.clear();
		}finally{
			try{if(rs!=null)
				rs.close();
			}catch(Exception e){
			}
			try{if(ps!=null)
				ps.close();
			}catch(Exception e){
			}	
		}		
		loaded=true;

	}

	public String getCls() {
		
		return "objs";
	}

	public String getType() {
		
		return "ora_pkgbodys";
	}

	public boolean isLeaf() {
		
		return false;
	}

}