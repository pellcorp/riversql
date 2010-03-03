
package com.riversql.databases;

import java.util.List;


public class MySqlSyntaxGenerator implements ISyntaxGenerator {

	public void newColumn(StringBuilder sb, String tableQualifiedName,
			String columnName, String type, String size, String decDigits,
			String remarks, String defValue, boolean acceptNull) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("ADD COLUMN "+columnName+ " "+type+" ");
		size=size.trim();
		decDigits=decDigits.trim();
		defValue=defValue.trim();
		remarks=remarks.trim();
		
		if(type.equals("TINYINT")||       
			type.equals("SMALLINT")||
			type.equals("MEDIUMINT")||
			type.equals("INT")||
			type.equals("INTEGER")||
			type.equals("BIGINT")
				){
			if(size.length()>0){
				sb.append("("+size+") ");
			}
		}else if (type.equals("REAL")||
			type.equals("DOUBLE")||
			type.equals("FLOAT")
			){
			if(size.length()>0){
				sb.append("("+size+","+decDigits+") ");
			}
		}else if (type.equals("DECIMAL")|| type.equals("NUMERIC")){
			sb.append("("+size+","+decDigits+") ");
		}else if (type.equals("CHAR")||type.equals("VARCHAR")){
			sb.append("("+size+") ");
		}
		if(!acceptNull)
			sb.append(" NOT NULL ");
		if(defValue.length()>0)
			sb.append(" DEFAULT "+escapeString(type,defValue)+" ");
		if(remarks.length()>0)
			sb.append(" "+remarks+" ");
		sb.append(";").append(sep);
	}

	private String escapeString(@SuppressWarnings("unused") String type, String defValue) {
		return "'"+defValue.replaceAll("'","''")+"'";
	}

	public void changeColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName, String type, String size,
			String decDigits, String remarks, String defValue,
			boolean acceptNull, boolean acceptNullOldValue) {
		
		//Example: ALTER TABLE `cat`.`albums`
		// CHANGE COLUMN candelete candelete2 INT (10) DEFAULT '4' comment 'my remarks'
		
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("CHANGE COLUMN "+oldColumnName+" "+columnName+ " "+type+" ");
		size=size.trim();
		decDigits=decDigits.trim();
		defValue=defValue.trim();
		remarks=remarks.trim();
		
		if(type.equals("TINYINT")||       
			type.equals("SMALLINT")||
			type.equals("MEDIUMINT")||
			type.equals("INT")||
			type.equals("INTEGER")||
			type.equals("BIGINT")
				){
			if(size.trim().length()>0){
				sb.append("("+size+") ");
			}
		}else if (type.equals("REAL")||
			type.equals("DOUBLE")||
			type.equals("FLOAT")
			){
			if(size.length()>0){
				sb.append("("+size+","+decDigits+") ");
			}
		}else if (type.equals("DECIMAL")|| type.equals("NUMERIC")){
			sb.append("("+size+","+decDigits+") ");
		}else if (type.equals("CHAR")||type.equals("VARCHAR")){
			sb.append("("+size+") ");
		}
		if(!acceptNull)
			sb.append(" NOT NULL ");
		
		if(defValue.length()>0)
			sb.append(" DEFAULT "+escapeString(type,defValue)+" ");
		
		if(remarks.length()>0)
			sb.append(" COMMENT "+escapeString(type,remarks)+" ");
	
		sb.append(";").append(sep);

	}

	public void dropColumn(StringBuilder sb, String tableQualifiedName,
			String droppedColumn) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(ISyntaxGenerator.sep);
		sb.append("DROP ").append(droppedColumn).append(";").append(ISyntaxGenerator.sep);

	}

	public void renameColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName) {
		//No effect. It's managed by changeColumn

	}

	public void dropIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName) {
		//TODO 
		//It doesn't work if the index is a foreign key!!!
		strbuilder.append("ALTER TABLE ").append(qualifiedName);
		strbuilder.append(" DROP INDEX ").append(indexName).append(" ;").append(sep);
		
	}

	public void newIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName, boolean unique,  List<String> ls) {
		strbuilder.append("ALTER TABLE ").append(qualifiedName).append(" ADD ");
		if(!unique)
			strbuilder.append("INDEX ");
		else
			strbuilder.append("UNIQUE ");
		strbuilder.append(indexName);
		
		strbuilder.append(" (");
		int sz=ls.size();
		for(int j=0;j<sz;j++){
			String colName=ls.get(j);
			strbuilder.append(colName);
			if(j<sz-1)
				strbuilder.append(",");
		}
		strbuilder.append(");").append(sep);
		
		
	}

	public void newPK(StringBuilder strbuilder, String qualifiedName,
			String pkname, List<String> lsCols) {
		strbuilder.append("ALTER TABLE ").append(qualifiedName).append(" ADD PRIMARY KEY (");
		for(int i=0;i<lsCols.size();i++){
			strbuilder.append(lsCols.get(i));
			if(i<lsCols.size()-1)
				strbuilder.append(",");
		}
		strbuilder.append(");").append(sep);
		
	}

	public void dropPK(StringBuilder strbuilder, String qualifiedName,
			String droppedPK) {
		strbuilder.append("ALTER TABLE ").append(qualifiedName);
		strbuilder.append(" DROP PRIMARY KEY;").append(sep);
		
	}

}
