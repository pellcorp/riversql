package com.riversql.sql.dbobj;

import com.riversql.sql.DatabaseObjectInfo;
import com.riversql.sql.DatabaseObjectType;
import com.riversql.sql.SQLDatabaseMetaData;
/**
 * Describes one column in a set of columns that uniquely identifies a row in
 * a table.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class BestRowIdentifier extends DatabaseObjectInfo
{
	static final long serialVersionUID = 7587093034289367642L;
    
    final private int _scope;
	final private String _colName;
	final private short _sqlDataType;
	final private String _typeName;
	final private int _precision;
	final private short _scale;
	final private short _pseudoColumn;

	/**
	 * Ctor specifying attributes.
	 */
	public BestRowIdentifier(String catalog, String schema, String tableName,
			int scope, String colName, short sqlDataType, String typeName,
			int precision, short scale, short pseudoColumn,
			SQLDatabaseMetaData md)
	{
		super(catalog, schema, tableName, DatabaseObjectType.FOREIGN_KEY, md);

		_scope = scope;
		_colName = colName;
		_sqlDataType = sqlDataType;
		_typeName = typeName;
		_precision = precision;
		_scale = scale;
		_pseudoColumn = pseudoColumn;
	}

	public int getScope()
	{
		return _scope;
	}

	public String getColumnName()
	{
		return _colName;
	}

	public short getSQLDataType()
	{
		return _sqlDataType;
	}

	public String getTypeName()
	{
		return _typeName;
	}

	public int getPrecision()
	{
		return _precision;
	}

	public short getScale()
	{
		return _scale;
	}

	public short getPseudoColumn()
	{
		return _pseudoColumn;
	}
}
