
package com.riversql.databases;

import java.util.List;


public class MSSQLSyntaxGenerator implements ISyntaxGenerator {

	public void changeColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName, String type, String size,
			String decDigits, String remarks, String defValue,
			boolean acceptNull, boolean acceptNullOldValue) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("ALTER COLUMN ").append(generateModifyColumn(columnName,oldColumnName,type,size,decDigits,remarks,defValue,acceptNull,acceptNullOldValue)).append(sep).append(GO).append(sep);

	}

	private Object generateModifyColumn(String columnName,
			@SuppressWarnings("unused") String oldColumnName, String type, String size, String decDigits,
			@SuppressWarnings("unused") String remarks, @SuppressWarnings("unused") String defValue, boolean acceptNull,
			boolean acceptNullOldValue) {
		StringBuffer result= new StringBuffer(" "+columnName+" "+type+"");
		size=size.trim();
		String prec=decDigits.trim();
		
		
		if(type.equals("NVARCHAR")      
				|| type.equals("NCHAR")
				|| type.equals("VARBINARY")
				|| type.equals("BINARY")
				|| type.equals("CHAR")
				|| type.equals("VARCHAR")
										){
			if(size.length()>0){
				result.append("("+size+") ");
			}
		}else if ((type.equals("NUMERIC"))||(type.equals("DECIMAL"))){
			if(size.length()>0){
				result.append("("+size);
				if(prec.length()>0)
					result.append(","+prec);
				result.append(") ");
			}
		}
		if(!acceptNull && acceptNull!=acceptNullOldValue)
			result.append(" NOT NULL ");
		else if (acceptNull && acceptNull!=acceptNullOldValue)
			result.append(" NULL ");
	
		return result.toString();
	}

	public void dropColumn(StringBuilder sb, String tableQualifiedName,
			String droppedColumn) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("DROP COLUMN ").append(droppedColumn).append(sep).append(GO).append(sep);

	}
	static final String GO="GO";
	
	public void dropIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName) {
		strbuilder.append(" DROP INDEX ").append(indexName).append(" ON ").append(qualifiedName).append(sep).append(GO).append(sep);

	}

	public void dropPK(StringBuilder strbuilder, String qualifiedName,
			String droppedPK) {
		strbuilder.append("ALTER TABLE ").append(qualifiedName);
		strbuilder.append(" DROP CONSTRAINT ").append(droppedPK).append(sep).append(GO).append(sep);

	}

	public void newColumn(StringBuilder sb, String tableQualifiedName,
			String columnName, String type, String size, String decDigits,
			String remarks, String defValue, boolean acceptNull) {
		sb.append("ALTER TABLE ").append(tableQualifiedName).append(sep);
		sb.append("ADD ").append(generateAddColumn(columnName,type,size,decDigits,defValue,acceptNull)).append(sep).append(GO).append(sep);

	}

	public void newIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName, boolean unique, List<String> cols) {
		strbuilder.append("CREATE ");
		if(unique)
			strbuilder.append("UNIQUE ");
		strbuilder.append("INDEX ");
		strbuilder.append(indexName);
		strbuilder.append(" ON ").append(qualifiedName);
		strbuilder.append("(");
		int sz=cols.size();
		for(int j=0;j<sz;j++){
			String nm=cols.get(j);
			strbuilder.append(nm);
			if(j<sz-1)
				strbuilder.append(",");
		}
		strbuilder.append(")").append(sep).append(GO).append(sep);

	}

	public void newPK(StringBuilder strbuilder, String qualifiedName,
			String pkname, List<String> lsCols) {
		if(pkname!=null && pkname.trim().length()>0){
			strbuilder.append("ALTER TABLE ").append(qualifiedName).append(" ADD ");
			strbuilder.append(" CONSTRAINT ").append(pkname.trim());
		}
		strbuilder.append(" PRIMARY KEY (");
		for(int i=0;i<lsCols.size();i++){
			strbuilder.append(lsCols.get(i));
			if(i<lsCols.size()-1)
				strbuilder.append(",");
		}
		strbuilder.append(")").append(sep).append(GO).append(sep);

	}

	public void renameColumn(StringBuilder sb, String tableQualifiedName,
			String oldColumnName, String columnName) {
		sb.append("EXEC sp_rename '").append(tableQualifiedName).append(".").append(oldColumnName).append("', '"+columnName+"', 'COLUMN'").append(sep).append(GO).append(sep);

	}
	private String generateAddColumn( String columnName, String type, String size, String prec, String defValue, boolean nullAccepted) {
		StringBuffer sb=new StringBuffer();
		sb.append(" "+columnName+ " "+type+" ");
		size=size.trim();
		prec=prec.trim();
		defValue=defValue.trim();
		
		//String comment=cm.comment.trim();
		
		if(type.equals("NVARCHAR")       
			|| type.equals("NCHAR")
			|| type.equals("VARBINARY")
			|| type.equals("BINARY")
			|| type.equals("CHAR")
			|| type.equals("VARCHAR")
									){
			if(size.length()>0){
				sb.append("("+size+") ");
			}
		}else if ((type.equals("NUMERIC"))||(type.equals("DECIMAL"))){
			if(size.length()>0){
				sb.append("("+size);
				if(prec.length()>0)
					sb.append(","+prec);
				sb.append(") ");
			}
		}
		else if (type.equals("")){//Size obbligatoria
			sb.append("("+size+") ");
		}
		if(!nullAccepted)
			sb.append(" NOT NULL ");
		if(defValue.length()>0)
			sb.append(" DEFAULT "+escapeString(type,defValue)+" ");
		return sb.toString();
		//if(comment.length()>0)
		//	result.append(" "+comment+" ");
	
		
	}

	private String escapeString(@SuppressWarnings("unused") String type, String defValue) {
		
		return "'"+defValue+"'";
	}
}
