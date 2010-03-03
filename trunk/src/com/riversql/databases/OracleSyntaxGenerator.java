
package com.riversql.databases;

import java.util.List;

public class OracleSyntaxGenerator implements ISyntaxGenerator {

	public void dropColumn(StringBuilder sb, String tableQualifiedName,
			String droppedColumn) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(ISyntaxGenerator.sep);
		sb.append("DROP COLUMN ").append(droppedColumn).append(";").append(ISyntaxGenerator.sep);
		
	}

	public void renameColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(" RENAME COLUMN "+oldColumnName+" TO "+columnName+";").append(sep);
	}

	public void changeColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName, String type, String size,
			String decDigits, String remarks, String defValue,
			boolean acceptNull, boolean acceptNullOldValue) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("MODIFY (").append(generateOracleModifyColumn(columnName,type,size, decDigits, acceptNull,defValue,acceptNullOldValue)).append(");").append(sep);
		sb.append("COMMENT ON COLUMN "+tableQualifiedName+"."+columnName+" IS '"+remarks+"';").append(sep);
		
	}
	private String generateOracleModifyColumn(String columnName, String type,
			String size, String prec, boolean acceptNull, 
			String def, boolean acceptNullOld) {
		StringBuffer result= new StringBuffer(" "+columnName+" "+type+"");
		
		//String comment=cm.comment.trim();
		
		if(type.equals("CHAR")||       
			type.equals("FLOAT")||
			type.equals("NCHAR")||
			type.equals("UROWID")
				){
			if(size.length()>0){
				result.append("("+size+") ");
			}
		}else if (type.equals("NUMBER")){
			if(size.length()>0){
				result.append("("+size);
				if(prec.length()>0)
					result.append(","+prec);
				result.append(") ");
			}
		}else if (type.equals("CHAR")||type.equals("VARCHAR2")||type.equals("RAW")||type.equals("NVARCHAR2")){//Size obbligatoria
			result.append("("+size+") ");
		}
		if(!acceptNull && acceptNull!=acceptNullOld)
			result.append(" NOT NULL ");
		
		if(def.length()>0)
			result.append(" DEFAULT '"+def+"' ");
		else
			result.append(" DEFAULT NULL ");
	
		return result.toString();
	}

	public void newColumn(StringBuilder sb, String tableQualifiedName,
			String columnName, String type, String size, String decDigits,
			String remarks, String defValue, boolean acceptNull) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("ADD (").append(generateOracleAddColumn(columnName, type,size,decDigits, acceptNull,defValue)).append(");").append(sep);
		sb.append("COMMENT ON COLUMN "+tableQualifiedName+"."+columnName+" IS '"+remarks+"';").append(sep);
		
	}

	private Object generateOracleAddColumn(String columnName, String type,
			String size, String decDigits, boolean acceptNull, String defValue) {
		StringBuffer result= new StringBuffer(" "+columnName+ " "+type+" ");
		size=size.trim();
		String prec=decDigits.trim();
		defValue=defValue.trim();
		
		if(type.equals("CHAR")||       
			type.equals("FLOAT")||
			type.equals("NCHAR")||
			type.equals("UROWID")
			){
			if(size.length()>0){
				result.append("("+size+") ");
			}
		}else if (type.equals("NUMBER")){
			if(size.length()>0){
				result.append("("+size);
				if(prec.length()>0)
					result.append(","+prec);
				result.append(") ");
			}
		}
		else if (type.equals("CHAR")||type.equals("VARCHAR")||type.equals("VARCHAR2")||type.equals("RAW")||type.equals("NVARCHAR2")){//Size obbligatoria
			result.append("("+size+") ");
		}
		if(!acceptNull)
			result.append(" NOT NULL ");
		if(defValue.length()>0)
			result.append(" DEFAULT '"+defValue+"' ");
		
	
		return result.toString();

	}

	public void dropIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName) {
		strbuilder.append("DROP INDEX ").append(indexName).append(";").append(sep);
		
	}

	public void newIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName, boolean unique, List<String> ls) {
		
		strbuilder.append("CREATE ");
		if(unique)
			strbuilder.append("UNIQUE ");
		strbuilder.append("INDEX ");
		strbuilder.append(indexName);
		strbuilder.append(" ON ").append(qualifiedName);
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
		strbuilder.append("ALTER TABLE").append(qualifiedName).append(" ADD ");
		if(pkname!=null && pkname.trim().length()>0){
			strbuilder.append(" CONSTRAINT ").append(pkname.trim());
		}
		strbuilder.append(" PRIMARY KEY (");
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
		strbuilder.append(" DROP PRIMARY KEY CASCADE;").append(sep);
		
	}

}
