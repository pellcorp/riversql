
package com.riversql.plugins.oracle;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

import com.riversql.sql.SQLConnection;
import com.riversql.dbtree.SchemaNode;
import com.riversql.plugin.BasePluginType;

public class ObjectTypeBodyNode extends BasePluginType {


	public ObjectTypeBodyNode(SchemaNode schemaNode,SQLConnection conn) {
		super("ObjectBodyType",schemaNode,conn);
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
			final String sql="SELECT  object_name,status, created,last_ddl_time,timestamp "+  
			" FROM sys.all_objects where owner=? "+  
			" and object_type='TYPE BODY' order by 1 asc";
			ps=conn.prepareStatement(sql);
			String owner=getOwner();
			ps.setString(1,owner);
			rs=ps.executeQuery();
			while(rs.next()){
				String name2=rs.getString(1);
				String status=rs.getString(2);
				ObjectBodyNode obNode=new ObjectBodyNode(this,name2,conn,status);
				list.add(obNode);
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
		// TODO Auto-generated method stub
		return "ora_bodytypes";
	}


	public boolean isLeaf() {
		
		return false;
	}

}
