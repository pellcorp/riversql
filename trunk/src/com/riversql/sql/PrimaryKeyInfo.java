package com.riversql.sql;

/**
 * This represents the primary key definition for a table.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class PrimaryKeyInfo extends DatabaseObjectInfo
{
    static final long serialVersionUID = 4785889679696720264L;

    /** 
     * the name of the column which belongs to a list of columns that form a 
     * unique key for a table
     */
    private String columnName = null;
    
    /** sequence number within primary key */
    private short keySequence;
    
    /**
     * The table that has this primary key constraint
     */
    private String tableName = null;
    
    /**
     * @deprecated use the version of the constructor that accepts args to 
     *             provide complete information about this key.
     */
    PrimaryKeyInfo() {
        super(null, null, null, null, null);
    }
    
    /**
     * Create a new PrimaryKeyInfo object.
     * 
     * @param catalog catalog name
     * @param schema schema name
     * @param aColumnName the name of the column that either by itself or along
     *                    with others form(s) a unique index value for a single
     *                    row in a table. 
     * @param aKeySequence sequence number within primary key
     * @param aPrimaryKeyName the name of the primary key
     * @param md
     */
	PrimaryKeyInfo(String catalog, 
                   String schema,
                   String aTableName,
                   String aColumnName, 
                   short aKeySequence, 
                   String aPrimaryKeyName,
                   SQLDatabaseMetaData md)
	{
		super(catalog, schema, aPrimaryKeyName, DatabaseObjectType.PRIMARY_KEY, md);
        columnName = aColumnName;
        tableName = aTableName;
        keySequence = aKeySequence;
	}

    /**
     * @param columnName The columnName to set.
     */
    public void setColumnName(String columnName) {
        this.columnName = columnName;
    }

    /**
     * @return Returns the columnName.
     */
    public String getColumnName() {
        return columnName;
    }

    /**
     * @param keySequence The keySequence to set.
     */
    public void setKeySequence(short keySequence) {
        this.keySequence = keySequence;
    }

    /**
     * @return Returns the keySequence.
     */
    public short getKeySequence() {
        return keySequence;
    }

    /**
     * @param tableName the tableName to set
     */
    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    /**
     * @return the tableName
     */
    public String getTableName() {
        return tableName;
    }

    public String getQualifiedColumnName() {
        if (tableName != null && !"".equals(tableName)) {
            return tableName + "." + columnName;
        }
        return columnName;
    }
}
