
package com.riversql.utils;

import java.math.BigDecimal;
import java.sql.Blob;
import java.sql.Clob;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;

/**
 * 
 * based on ResultSetReader of squirrel_sql...
 *
 */
public class ResultSetReader
{
	
	/** The <TT>ResultSet</TT> being read. */
	private final ResultSet _rs;

	/**
	 * The indices into the <TT>ResultSet that we want to read, starting from
	 * 1 (not 0). If this contains {1, 5, 6} then only columns 1, 5, and 6 will
	 * be read. If <TT>null or empty then all columns are read.
	 */
	private final int[] _columnIndices;


	/**
	 * The number of columns to read. This may or may not be the same as the
	 * number of columns in the <TT>ResultSet</TT>. @see _columnIndices.
	 */
	private int _columnCount;

	/** <TT>true</TT> if an error occured reading a column in th previous row. */
	private boolean _errorOccured = false;

	/** Metadata for the <TT>ResultSet</TT>. */
	private final ResultSetMetaData _rsmd;

  
	public ResultSetReader(ResultSet rs)
		throws SQLException
	{
		this(rs, null);
	}



	public ResultSetReader(ResultSet rs, int[] columnIndices) throws SQLException
	{
		super();
		if (rs == null)
		{
			throw new IllegalArgumentException("ResultSet == null");
		}

		_rs = rs;
		try{_rs.setFetchSize(500);}catch(Exception e){}
		
		if (columnIndices != null && columnIndices.length == 0)
		{
			columnIndices = null;
		}
		_columnIndices = columnIndices;

		_rsmd = rs.getMetaData();

		_columnCount = columnIndices != null ? columnIndices.length : _rsmd.getColumnCount();
	}

	/**
	 * Read the next row from the <TT>ResultSet</TT>. If no more rows then
	 * <TT>null</TT> will be returned, otherwise an <TT>Object[]</TT> will be
	 * returned where each element of the array is an object representing
	 * the contents of the column. These objects could be of type <TT>String</TT>,
	 * <TT>BigDecimal</TT> etc.
	 *
	 * <P>If an error occurs calling <TT>next()</TT> on the <TT>ResultSet</TT>
	 * then an <TT>SQLException will be thrown, however if an error occurs
	 * retrieving the data for a column an error msg will be placed in that
	 * element of the array, but no exception will be thrown. To see if an
	 * error occured retrieving column data you can call
	 * <TT>getColumnErrorInPreviousRow</TT> after the call to <TT>readRow()</TT>.
	 *
	 * @throws	SQLException	Error occured on <TT>ResultSet.next()</TT>.
	 */
	public Object[] readRow() throws SQLException
	{
		_errorOccured = false;
		if (_rs.next())
		{
			return doRead();
		}
		return null;
	}

	/**
	 * Read the next row from the <TT>ResultSet</TT> for use in the ContentTab.
	 * This is different from readRow() in that data is put into the Object array
	 * in a form controlled by the DataType objects, and may be used for editing
	 * the data and updating the DB. If no more rows then
	 * <TT>null</TT> will be returned, otherwise an <TT>Object[]</TT> will be
	 * returned where each element of the array is an object representing
	 * the contents of the column. These objects could be of type <TT>String</TT>,
	 * <TT>BigDecimal</TT> etc.
	 *
	 * <P>If an error occurs calling <TT>next()</TT> on the <TT>ResultSet</TT>
	 * then an <TT>SQLException will be thrown, however if an error occurs
	 * retrieving the data for a column an error msg will be placed in that
	 * element of the array, but no exception will be thrown. To see if an
	 * error occured retrieving column data you can call
	 * <TT>getColumnErrorInPreviousRow</TT> after the call to <TT>readRow()</TT>.
	 *
	 * @throws	SQLException	Error occured on <TT>ResultSet.next()</TT>.
	 */
//	public Object[] readRow(ColumnDisplayDefinition colDefs[]) throws SQLException
//	{
//		_errorOccured = false;
//		if (_rs.next())
//		{
//			return doContentTabRead(colDefs);
//		}
//		return null;
//	}

	/**
	 * Retrieve whether an error occured reading a column in the previous row.
	 *
	 * @return	<TT>true</TT> if error occured.
	 */
	public boolean getColumnErrorInPreviousRow()
	{
		return _errorOccured;
	}

	/**
	 * Method used to read data for all Tabs except the ContentsTab, where
	 * the data is used only for reading.
	 * The only data read in the non-ContentsTab tabs is Meta-data about the DB,
	 * which means that there should be no BLOBs, CLOBs, or unknown fields.
	 */
	private Object[] doRead()
	{
		final Object[] row = new Object[_columnCount];
		for (int i = 0; i < _columnCount ; ++i)
		{
			int idx = _columnIndices != null ? _columnIndices[i] : i + 1;
			try
			{
				final int columnType = _rsmd.getColumnType(idx);
				//final String columnClassName = _rsmd.getColumnClassName(idx);
				switch (columnType)
				{
					case Types.NULL:
						row[i] = null;
						break;

					case Types.BIT:
					case Types.BOOLEAN:
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof Boolean))
						{
							if (row[i] instanceof Number)
							{
								if (((Number) row[i]).intValue() == 0)
								{
									row[i] = Boolean.FALSE;
								}
								else
								{
									row[i] = Boolean.TRUE;
								}
							}
							else
							{
								row[i] = Boolean.valueOf(row[i].toString());
							}
						}
						break;

					case Types.TIME :
						row[i] = _rs.getTime(idx);
						break;

					case Types.DATE :
                        //if (DataTypeDate.getReadDateAsTimestamp()) {
                            row[i] = _rs.getDate(idx);
                        //} else {
                          //  row[i] = DataTypeDate.staticReadResultSet(_rs, idx, false);
                        //}
						break;

					case Types.TIMESTAMP :
                    case -101 : // Oracle's 'TIMESTAMP WITH TIME ZONE' == -101  
                    case -102 : // Oracle's 'TIMESTAMP WITH LOCAL TIME ZONE' == -102
						row[i] = _rs.getTimestamp(idx);
						break;

					case Types.BIGINT :
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof Long))
						{
							if (row[i] instanceof Number)
							{
								row[i] = new Long(((Number)row[i]).longValue());
							}
							else
							{
								row[i] = new Long(row[i].toString());
							}
						}
						break;

					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.REAL:
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof Double))
						{
							if (row[i] instanceof Number)
							{
								Number nbr = (Number)row[i];
								row[i] = new Double(nbr.doubleValue());
							}
							else
							{
								row[i] = new Double(row[i].toString());
							}
						}
						break;

					case Types.DECIMAL:
					case Types.NUMERIC:
						row[i] = _rs.getObject(idx);
						if (row[i] != null
							&& !(row[i] instanceof BigDecimal))
						{
							if (row[i] instanceof Number)
							{
								Number nbr = (Number)row[i];
								row[i] = new BigDecimal(nbr.doubleValue());
							}
							else
							{
								row[i] = new BigDecimal(row[i].toString());
							}
						}
						break;

					case Types.INTEGER:
					case Types.SMALLINT:
					case Types.TINYINT:
						row[i] = _rs.getObject(idx);
//						if (_rs.wasNull())
//						{
//							row[i] = null;
//						}
						if (row[i] != null
							&& !(row[i] instanceof Integer))
						{
							if (row[i] instanceof Number)
							{
								row[i] = new Integer(((Number)row[i]).intValue());
							}
							else
							{
								row[i] = new Integer(row[i].toString());
							}
						}
						break;

						// TODO: Hard coded -. JDBC/ODBC bridge JDK1.4
						// brings back -9 for nvarchar columns in
						// MS SQL Server tables.
						// -8 is ROWID in Oracle.
					case Types.CHAR:
					case Types.VARCHAR:
					case Types.LONGVARCHAR:
					case -9:
					case -8:
						row[i] = _rs.getString(idx);
						try{if (_rs.wasNull())
						{
							row[i] = null;
						}}catch(Throwable t){}
						break;

					case Types.BINARY:
					case Types.VARBINARY:
					case Types.LONGVARBINARY:
						row[i] = _rs.getString(idx);
						break;

					case Types.BLOB:
						Blob blob = _rs.getBlob(idx);
						try{if(_rs.wasNull())row[i]=null;break;}catch(Error e){}
						if(blob!=null ){
							
							byte[] b=blob.getBytes(1, (int)blob.length());
							row[i] = new String(b);
						}

						break;

					case Types.CLOB:
						Clob clob = _rs.getClob(idx);
						try{if(_rs.wasNull())row[i]=null;break;}catch(Error e){}
						if(clob!=null ){
							
							row[i]=clob.getSubString(1,(int)clob.length());
						}

						break;

						//Add begin
					case Types.JAVA_OBJECT:
					    row[i] = _rs.getObject(idx);
					    try{if (_rs.wasNull())
					    {
					        row[i] = null;
					    }}catch(Throwable t){}
					    break;
					    //Add end


					case Types.OTHER:
					    // Since we are reading Meta-data, there really should never be
					    // a field with SQL type Other (1111).
					    // If there is, we REALLY do not know how to handle it,
					    // so do not attempt to read.
//					    ??						if (_largeObjInfo.getReadSQLOther())
//					    ??						{
//					    ??							// Running getObject on a java class attempts
//					    ??							// to load the class in memory which we don't want.
//					    ??							// getString() just gets the value without loading
//					    ??							// the class (at least under PostgreSQL).
//					    ??							//row[i] = _rs.getObject(idx);
//					    ??							row[i] = _rs.getString(idx);
//					    ??						}
//					    ??						else
//					    ??						{
					    row[i] = "Other";
//					    ??						}
					    break;

					default:
						// Since we are reading Meta-data, there should never be a
						// field with an unknown data type.
						// If there is, then we REALLY do not know how to handle it,
						// so do not attempt to read.
//??						if (_largeObjInfo.getReadAllOther())
//??						{
//??							row[i] = _rs.getObject(idx);
//??						}
//??						else
//??						{
							row[i] = "Unknown";
	//??					}
				}
			}
			catch (Throwable th)
			{
                // Don't bother the user with details about where the result fetch
                // failed if they cancelled the query.
//                if (!_stopExecution) {
//                    _errorOccured = true;
//                    row[i] = "Error";
//                    StringBuffer msg = new StringBuffer("Error reading column data");
//                    msg.append(", column index = ").append(idx);
//                   // s_log.error(msg.toString(), th);
//                }
			}
		}

		return row;
	}
}
