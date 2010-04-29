package com.riversql.sql;


public class UDTInfo extends DatabaseObjectInfo implements IUDTInfo
{
	static final long serialVersionUID = 8215062701260471438L;

    /** Java class name. */
	private final String _javaClassName;

	/** UDT Data Type. */
	private final String _dataType;

	/** UDT remarks. */
	private final String _remarks;

	UDTInfo(String catalog, String schema, String simpleName, String javaClassName,
			String dataType, String remarks, SQLDatabaseMetaData md)
	{
		super(catalog, schema, simpleName, DatabaseObjectType.UDT, md);
		_javaClassName = javaClassName;
		_dataType = dataType;
		_remarks = remarks;
	}

	public String getJavaClassName()
	{
		return _javaClassName;
	}

	public String getDataType()
	{
		return _dataType;
	}

	public String getRemarks()
	{
		return _remarks;
	}

	public boolean equals(Object obj)
	{
		if (super.equals(obj) && obj instanceof UDTInfo)
		{
			UDTInfo info = (UDTInfo) obj;
			if ((info._dataType == null && _dataType == null)
				|| ((info._dataType != null && _dataType != null)
					&& info._dataType.equals(_dataType)))
			{
				if ((info._javaClassName == null && _javaClassName == null)
					|| ((info._javaClassName != null && _javaClassName != null)
						&& info._javaClassName.equals(_javaClassName)))
				{
					return (
						(info._remarks == null && _remarks == null)
							|| ((info._remarks != null && _remarks != null)
								&& info._remarks.equals(_remarks)));
				}
			}
		}
		return false;
	}
}
