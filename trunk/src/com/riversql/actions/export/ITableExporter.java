
package com.riversql.actions.export;

import java.io.IOException;
import java.io.OutputStream;
import java.sql.ResultSetMetaData;

public interface ITableExporter {
	void configure(ResultSetMetaData rsmd);
	void newLine();
	void newCell(Object obj);
	String getMimeType();
	int getContentSize();
	void finish();
	void copyTo(OutputStream os) throws IOException;
}
