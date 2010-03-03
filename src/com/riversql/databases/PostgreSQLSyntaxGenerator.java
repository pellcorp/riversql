
package com.riversql.databases;

import java.util.List;

public class PostgreSQLSyntaxGenerator implements ISyntaxGenerator {

	
	public void changeColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName, String type, String size,
			String decDigits, String remarks, String defValue,
			boolean acceptNull, boolean acceptNullOldValue) {
		sb.append(generatePostgreSQLModifyColumn(tableQualifiedName,columnName,type,size, decDigits, acceptNull,defValue,acceptNullOldValue,remarks));
		
	}

	private Object generatePostgreSQLModifyColumn(String tableQualifiedName,String columnName,
			String type, String size, String decDigits, boolean acceptNull,
			String defValue, boolean acceptNullOldValue, String remarks) {
		StringBuilder result=new StringBuilder();
		result.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		result.append("ALTER COLUMN ");
		result.append(" "+columnName+" TYPE "+type);
		
		if (type.equals("DECIMAL")|| type.equals("NUMERIC")){
			if(size.length()>0){
				result.append("("+size);
				if(decDigits.length()>0)
					result.append(","+decDigits);
				result.append(") ");
			}
		}
		else if (type.equals("CHAR")||type.equals("CHARACTER")||type.equals("VARCHAR")||type.equals("CHARACTER VARYING")){//Size obbligatoria
			result.append("("+size+") ");
		}
		result.append(";").append(sep);
		
		
		if(!acceptNull && acceptNull!=acceptNullOldValue){
			result.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
			result.append("ALTER COLUMN ");
			result.append(columnName);
			result.append(" NOT NULL ");
			result.append(";").append(sep);
		}
		if(acceptNull && acceptNullOldValue==false){
			
			result.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
			result.append("ALTER COLUMN ");
			result.append(columnName);
			result.append(" DROP NOT NULL ");
			result.append(";").append(sep);
		}
		
		result.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		result.append("ALTER COLUMN ");
		result.append(columnName);
		if(defValue==null || defValue.trim().equals("")){
			result.append(" DROP DEFAULT");
		}else{
			result.append(" SET DEFAULT '"+defValue+"' ");
		}
		result.append(";").append(sep);
		result.append("COMMENT ON COLUMN ").append(tableQualifiedName).append(".").append(columnName);
		result.append(" IS ").append(escapeString(remarks));
		result.append(";").append(sep);
		return result.toString();
	}
	private String escapeString(String input){
		return "'"+input.replaceAll("'", "\\\\'")+"'";
	}

	
	public void dropColumn(StringBuilder sb, String tableQualifiedName,
			String droppedColumn) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(ISyntaxGenerator.sep);
		sb.append("DROP COLUMN ").append(droppedColumn).append(" CASCADE;").append(ISyntaxGenerator.sep);
		

	}

	
	public void dropIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName) {
		strbuilder.append("DROP INDEX ").append(indexName).append(" CASCADE;").append(sep);

	}

	
	public void dropPK(StringBuilder strbuilder, String qualifiedName,
			String droppedPK) {
		strbuilder.append("ALTER TABLE ").append(qualifiedName);
		strbuilder.append(" DROP CONSTRAINT ").append(droppedPK).append(";").append(sep);

	}

	
	public void newColumn(StringBuilder sb, String tableQualifiedName,
			String columnName, String type, String size, String decDigits,
			String remarks, String defValue, boolean acceptNull) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("ADD COLUMN ").append(generatePostgreSQLAddColumn(columnName, type,size,decDigits, acceptNull,defValue)).append(";").append(sep);
		sb.append("COMMENT ON COLUMN ").append(tableQualifiedName).append(".").append(columnName);
		sb.append(" IS ").append(escapeString(remarks)).append(";").append(sep);
		
	}

	private String generatePostgreSQLAddColumn(String columnName, String type,
			String size, String decDigits, boolean acceptNull, String defValue) {
		
		
		StringBuffer result= new StringBuffer(" "+columnName+ " "+type+" ");
		size=size.trim();
		String prec=decDigits.trim();
		defValue=defValue.trim();
		
		if (type.equals("DECIMAL")|| type.equals("NUMERIC")){
			if(size.length()>0){
				result.append("("+size);
				if(prec.length()>0)
					result.append(","+prec);
				result.append(") ");
			}
		}
		else if (type.equals("CHAR")||type.equals("CHARACTER")||type.equals("VARCHAR")||type.equals("CHARACTER VARYING")){//Size obbligatoria
			result.append("("+size+") ");
		}
		if(!acceptNull)
			result.append(" NOT NULL ");
		if(defValue.length()>0)
			result.append(" DEFAULT '"+defValue+"' ");
		
		
		return result.toString();
	}

	
	public void newIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName, boolean unique, List<String> cols) {
		strbuilder.append("CREATE ");
		if(unique)
			strbuilder.append("UNIQUE ");
		strbuilder.append("INDEX ");
		strbuilder.append(indexName);
		strbuilder.append(" ON ").append(qualifiedName);
		strbuilder.append(" (");
		int sz=cols.size();
		for(int j=0;j<sz;j++){
			String colName=cols.get(j);
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

	
	public void renameColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName) {
		sb.append("ALTER TABLE ").append(tableQualifiedName);
		sb.append(" RENAME COLUMN ").append(oldColumnName).append(" TO ");
		sb.append(columnName).append(";").append(sep);

	}

}
