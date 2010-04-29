package com.riversql.sql;

import java.sql.DatabaseMetaData;

import com.riversql.util.StringManager;
import com.riversql.util.StringManagerFactory;

public class ProcedureInfo extends DatabaseObjectInfo implements IProcedureInfo
{
    static final long serialVersionUID = -4111528608716386156L;
    
	/** Internationalized strings for this class. */
	private static final StringManager s_stringMgr =
		StringManagerFactory.getStringManager(ProcedureInfo.class);

	private interface IStrings
	{
		String DATABASE = s_stringMgr.getString("ProcedureInfo.database");
		String MAY_RETURN = s_stringMgr.getString("ProcedureInfo.mayreturn");
		String DOESNT_RETURN = s_stringMgr.getString("ProcedureInfo.doesntreturn");
		String DOES_RETURN = s_stringMgr.getString("ProcedureInfo.returns");
		String UNKNOWN = s_stringMgr.getString("ProcedureInfo.unknown");
	}

	/** Procedure Type. */
	private final int _procType;

	/** Procedure remarks. */
	private final String _remarks;

	public ProcedureInfo(String catalog, String schema, String simpleName,
							String remarks, int procType,
							SQLDatabaseMetaData md)
	{
		super(catalog, schema, simpleName, DatabaseObjectType.PROCEDURE, md);
		_remarks = remarks;
		_procType = procType;
	}

	public int getProcedureType()
	{
		return _procType;
	}

	public String getRemarks()
	{
		return _remarks;
	}

	public String getProcedureTypeDescription()
	{
		switch (_procType)
		{
			case DatabaseMetaData.procedureNoResult :
				return IStrings.DOESNT_RETURN;
			case DatabaseMetaData.procedureReturnsResult :
				return IStrings.DOES_RETURN;
			case DatabaseMetaData.procedureResultUnknown :
				return IStrings.MAY_RETURN;
			default :
				return IStrings.UNKNOWN;
		}
	}

	public boolean equals(Object obj)
	{
		if (super.equals(obj) && obj instanceof ProcedureInfo)
		{
			ProcedureInfo info = (ProcedureInfo) obj;
			if ((info._remarks == null && _remarks == null)
				|| ((info._remarks != null && _remarks != null)
					&& info._remarks.equals(_remarks)))
			{
				return info._procType == _procType;
			}
		}
		return false;
	}

}
