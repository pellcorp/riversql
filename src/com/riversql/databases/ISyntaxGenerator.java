
package com.riversql.databases;

import java.util.List;

public interface ISyntaxGenerator {
	public String sep="\r\n";
	void dropColumn(StringBuilder sb,String tableQualifiedName, String droppedColumn);
	void renameColumn(StringBuilder sb,String tableQualifiedName, String oldColumnName, String columnName);
	void changeColumn(StringBuilder sb,String tableQualifiedName,String oldColumnName, String columnName,String type, String size, String decDigits,String remarks, String defValue,boolean acceptNull, boolean acceptNullOldValue);
	void newColumn(StringBuilder sb,String tableQualifiedName,String columnName,String type, String size, String decDigits,String remarks, String defValue,boolean acceptNull);
	void dropIndex(StringBuilder strbuilder, String qualifiedName,
			String indexName);
	void newIndex(StringBuilder strbuilder, String qualifiedName, String indexName,
			boolean unique, List<String> cols);
	void newPK(StringBuilder strbuilder, String qualifiedName, String pkname,
			List<String> lsCols);
	void dropPK(StringBuilder strbuilder, String qualifiedName, String droppedPK);
}
