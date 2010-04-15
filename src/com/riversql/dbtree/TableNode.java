
package com.riversql.dbtree;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.util.ArrayList;
import java.util.List;

import com.riversql.sql.ITableInfo;
import com.riversql.sql.SQLConnection;
import com.riversql.sql.TableColumnInfo;

public class TableNode extends DBNode implements IStructureNode{

	private String text;
	private TablesNode parent;
	private ITableInfo iTableInfo;
	private String remarks;

	public TableNode(TablesNode tableObjectTypeNode,
			String simpleName, String remarks, ITableInfo tableInfo, SQLConnection conn) {
		super(conn);
		this.parent=tableObjectTypeNode;
		this.text=simpleName;
		this.remarks=remarks;
		this.conn=conn;
		this.iTableInfo=tableInfo;
	}
	
	 
	@Override
	public String getQualifiedName() {
		return iTableInfo.getQualifiedName();
	}
	
	public String getName() {
		return text;
	}
	public List<String> getColumnNames()throws SQLException{
		List<String> list=new ArrayList<String>();
		TableColumnInfo colsInfo[] = conn.getSQLMetaData().getColumnInfo(iTableInfo);
		
		for(int i=0;i<colsInfo.length;i++){
			list.add(colsInfo[i].getColumnName());
		}
		return list;
	}
	public List<String[]> getColumnNamesAndTypes()throws SQLException{
		List<String[]> list=new ArrayList<String[]>();
		TableColumnInfo colsInfo[] = conn.getSQLMetaData().getColumnInfo(iTableInfo);
		
		for(int i=0;i<colsInfo.length;i++){
			int dataType=colsInfo[i].getDataType();
			String tp=getType(dataType);
			list.add(new String[]{colsInfo[i].getColumnName(),tp});
		}
		return list;
	}
	private static String getType(int dataType) {
		switch(dataType){
			case Types.CHAR:
			case Types.VARCHAR:
			case Types.LONGNVARCHAR:
			case Types.LONGVARCHAR:
			case Types.NCHAR:
			case Types.NVARCHAR:
				return "char";
			case Types.BIGINT:
			case Types.DECIMAL:
			case Types.DOUBLE:
			case Types.FLOAT:
			case Types.INTEGER:
			case Types.NUMERIC:
			case Types.SMALLINT:
			case Types.REAL:
				return "number";
			case Types.DATE:
				return "date";
			case Types.TIME:
			case Types.TIMESTAMP:
				return "time";
			case Types.BINARY:
			case Types.BLOB:
			case Types.LONGVARBINARY:
			case Types.NCLOB:
			case Types.VARBINARY:
				return "binary";
		}
		return "other";
	}
	public List<ColumnModel> getColumns()throws SQLException{
		List<ColumnModel> list=new ArrayList<ColumnModel>();
		TableColumnInfo colsInfo[] = conn.getSQLMetaData().getColumnInfo(iTableInfo);
		
		for(int i=0;i<colsInfo.length;i++){
			ColumnModel cm=new ColumnModel(8);
			cm.setValue(0, colsInfo[i].getColumnName());
			cm.setValue(1, Integer.valueOf(colsInfo[i].getDataType()));
			cm.setValue(2, colsInfo[i].getTypeName());
			cm.setValue(3, Integer.valueOf(colsInfo[i].getColumnSize()));
			cm.setValue(4, Integer.valueOf(colsInfo[i].getDecimalDigits()));
			cm.setValue(5, colsInfo[i].getDefaultValue());
			cm.setValue(6, Integer.valueOf(colsInfo[i].isNullAllowed()));
			cm.setValue(7, colsInfo[i].getRemarks());
			list.add(cm);
		}
		return list;
	}
		
	public TablesNode getParent() {
		return parent;
	}
	public ITableInfo getITableInfo() {
		return iTableInfo;
	}
	public String getRemarks() {
		return remarks;
	}
	public ResultSet getIndexes() throws SQLException{
		return conn.getConnection().getMetaData().getIndexInfo(
				iTableInfo.getCatalogName(), iTableInfo.getSchemaName(),
				iTableInfo.getSimpleName(), false, true);
		
	}
	public ResultSet getPK() throws Exception{
		return conn.getConnection().getMetaData().getPrimaryKeys(
				iTableInfo.getCatalogName(), iTableInfo.getSchemaName(),
				iTableInfo.getSimpleName());
	
	}
	public ResultSet getFK()throws Exception {
		return conn.getConnection().getMetaData().getImportedKeys(
				iTableInfo.getCatalogName(), iTableInfo.getSchemaName(),
				iTableInfo.getSimpleName());
	}
	public ResultSet getGrants() throws Exception{
		return conn.getConnection().getMetaData().getTablePrivileges(
				iTableInfo.getCatalogName(), iTableInfo.getSchemaName(),
				iTableInfo.getSimpleName());
	}
	public ResultSet getExportedKeys()throws Exception {
		return conn.getConnection().getMetaData().getExportedKeys(
				iTableInfo.getCatalogName(), iTableInfo.getSchemaName(),
				iTableInfo.getSimpleName());
	}
	public String getPkName(){
		ResultSet rs=null;
		try{
			rs=getPK();
			String name="";
			if(rs!=null){
				
				if(rs.next()){
					name=rs.getString("PK_NAME");
				}
				return name;
			}
		}catch(Exception e){
		}finally{
			if(rs!=null){
				try {
					Statement st=rs.getStatement();
					rs.close();
					if(st!=null)
						st.close();
				} catch (SQLException e) {
				}
				
			}
		}
		return null;
	}
	public List<String>getPrimaryKeyColumns(){
		ArrayList<String> ls=new ArrayList<String>();
		ResultSet rs=null;
		try{
			rs=getPK();
			while(rs.next()){
				ls.add(rs.getString("COLUMN_NAME"));
			}
		}catch(Exception e){
			
		}finally{
			if(rs!=null){
				try {
					Statement st=rs.getStatement();
					rs.close();
					if(st!=null)
						st.close();
				} catch (SQLException e) {
				}
			}
		}
		
			
		return ls;
	}
	 
	@Override
	protected void nodeLoad() throws SQLException {
	}
	public String getCls() {
		if(parent.getName().toUpperCase().indexOf("VIEW")>-1){
			return "view";
		}else{
			return "table";
		}
	}
	public String getType() {
		
		if(parent.getName().toUpperCase().indexOf("VIEW")>-1){
			return "view";
		}else{
			return "tb";
		}
	}
	public boolean isLeaf() {
		return true;
	}
}
