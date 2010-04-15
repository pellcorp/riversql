
package com.riversql.plugins.oracle.actions;

import java.sql.Clob;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.riversql.sql.SQLConnection;
import com.riversql.IDManager;
import com.riversql.dbtree.IStructureNode;
import com.riversql.dbtree.TableNode;
import com.riversql.dbtree.TablesNode;
import com.riversql.plugin.BasePluginType;

public class ExtractDLL {

	private String nodeid;

	public ExtractDLL(String nodeid) {
		this.nodeid=nodeid;
	}

	public String execute() {
		IStructureNode nd=(IStructureNode)IDManager.get().get(nodeid);
		if(nd instanceof TableNode){
			TableNode tb=(TableNode)nd;
			final String sql="select DBMS_METADATA.GET_DDL(?,?,?) FROM dual";
			PreparedStatement stmt =null;
			ResultSet rs=null;
			try{
				SQLConnection conn=tb.getConn();
				stmt = conn.prepareStatement(sql);
				TablesNode tot=tb.getParent();
				String parent_type=tot.getTableType().toUpperCase();
				//if("PACKAGE BODY".equals(parent_type))
				//	parent_type="PACKAGE_BODY";
				
				stmt.setString(1, parent_type);
				stmt.setString(2, tb.getName());
				
				
				
				String owner=tot.getParent().getName();
				stmt.setString(3,owner);
				//StringBuffer result = new StringBuffer(1000);
				rs=stmt.executeQuery();
				String txt=null;
				if(rs.next())
				{
					Clob clob=rs.getClob(1);
					txt = clob.getSubString(1, (int)clob.length());
				}
		
				return txt;
			}catch(Exception e){
				
			}finally{
				try {if(rs!=null)
					rs.close();
				} catch (SQLException e) {
				}
				try {if(stmt!=null)
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}else if (nd instanceof BasePluginType){
			BasePluginType tb=(BasePluginType)nd;
			final String sql="select DBMS_METADATA.GET_DDL(?,?,?) FROM dual";
			PreparedStatement stmt =null;
			ResultSet rs=null;
			try{
				SQLConnection conn=tb.getConn();
				 stmt = conn.prepareStatement(sql);
				BasePluginType tot=(BasePluginType)tb.getParent();
				
				String parent_type=tot.getName().toUpperCase();
				if("PACKAGE BODY".equals(parent_type))
					parent_type="PACKAGE_BODY";
				
				stmt.setString(1, parent_type);
				
				//stmt.setString(1, tot.getName().toUpperCase());
				stmt.setString(2, tb.getName());
				
				String owner=tot.getParent().getName();
				stmt.setString(3,owner);
				//StringBuffer result = new StringBuffer(1000);
				 rs=stmt.executeQuery();
				String txt=null;
				if(rs.next())
				{
					Clob clob=rs.getClob(1);
					txt = clob.getSubString(1, (int)clob.length());
				}
		
				return txt;
			}catch(Exception e){
				
			}finally{
				try {if(rs!=null)
					rs.close();
				} catch (SQLException e) {
				}
				try {if(stmt!=null)
					stmt.close();
				} catch (SQLException e) {
				}
			}
		}
		return null;
	}

}
