package com.riversql.sql;

import java.util.SortedSet;
import java.util.TreeSet;

public class TableInfo extends DatabaseObjectInfo implements ITableInfo
{
    static final long serialVersionUID = -3184857504910012169L;

    /** Table Type. */
	private final String _tableType;

	/** Table remarks. */
	private final String _remarks;

	private SortedSet _childList; // build up datastructure.
	private ITableInfo[] _childs; // final cache.

    ForeignKeyInfo[] exportedKeys = null;
    ForeignKeyInfo[] importedKeys = null;
    
	public TableInfo(String catalog, String schema, String simpleName,
					 String tableType, String remarks,
					 SQLDatabaseMetaData md)
	{
		super(catalog, schema, simpleName, getTableType(tableType), md);
		_remarks = remarks;
		_tableType = tableType;
	}

   private static DatabaseObjectType getTableType(String tableType)
   {
      if(null == tableType)
      {
         return DatabaseObjectType.TABLE;
      }
      else if(false == tableType.equalsIgnoreCase("TABLE") && false == tableType.equalsIgnoreCase("VIEW"))
      {
         return DatabaseObjectType.TABLE;
      }
      else
      {
         return tableType.equalsIgnoreCase("VIEW") ? DatabaseObjectType.VIEW : DatabaseObjectType.TABLE;
      }
   }

   // TODO: Rename this to getTableType.
   public String getType()
   {
      return _tableType;
   }

	public String getRemarks()
	{
		return _remarks;
	}

	public boolean equals(Object obj)
	{
		if (super.equals(obj) && obj instanceof TableInfo)
		{
			TableInfo info = (TableInfo) obj;
			if ((info._tableType == null && _tableType == null)
				|| ((info._tableType != null && _tableType != null)
					&& info._tableType.equals(_tableType)))
			{
				return (
					(info._remarks == null && _remarks == null)
						|| ((info._remarks != null && _remarks != null)
							&& info._remarks.equals(_remarks)));
			}
		}
		return false;
	}

	void addChild(ITableInfo tab)
	{
		if (_childList == null)
		{
			_childList = new TreeSet();
		}
		_childList.add(tab);
	}

	public ITableInfo[] getChildTables()
	{
		if (_childs == null && _childList != null)
		{
			_childs = (ITableInfo[]) _childList.toArray(
								new ITableInfo[_childList.size()]);
			_childList = null;
		}
		return _childs;
	}


    /* (non-Javadoc)
     * @see com.riversql.sql.ITableInfo#getExportedKeys()
     */
    public ForeignKeyInfo[] getExportedKeys() {
        return exportedKeys;
    }

    public void setExportedKeys(ForeignKeyInfo[] foreignKeys) {
        exportedKeys = foreignKeys;
    }
    
    /* (non-Javadoc)
     * @see com.riversql.sql.ITableInfo#getImportedKeys()
     */
    public ForeignKeyInfo[] getImportedKeys() {
        return importedKeys;
    }

    public void setImportedKeys(ForeignKeyInfo[] foreignKeys) {
        importedKeys = foreignKeys;
    }
    


}
