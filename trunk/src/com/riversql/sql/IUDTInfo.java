package com.riversql.sql;

public interface IUDTInfo extends IDatabaseObjectInfo
{
	String getJavaClassName();
	String getDataType();
	String getRemarks();
}
