
package com.riversql.actions.export.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.sql.ResultSetMetaData;
import java.sql.Types;


import com.itextpdf.text.Element;

import com.riversql.actions.export.IColumnFormatter;
import com.riversql.actions.export.ITableExporter;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.util.Locale;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CSVTableExporter implements ITableExporter {

	private final String qualifiedName;
	ByteArrayOutputStream baos=new ByteArrayOutputStream(1024*1024);

	private int row=1;
	private int column=0;
        private int columnCount=0;
	IColumnFormatter formatters[];

        private StringBuffer sb = null;
        private char separator = ',';
	public CSVTableExporter(String qualifiedName) {
		this.qualifiedName = qualifiedName;
		this.sb = new StringBuffer();
	}

        public CSVTableExporter(int columnCount, JSONArray meta){
		this("");
		formatters=new IColumnFormatter[columnCount];
		for(int i=0;i<columnCount;i++){
			try {
				JSONObject row = meta.getJSONObject(i);
				String label=row.getString("l");
                                System.out.println(label+"=======================label");
				sb.append(label);

				final String align=row.getString("al");
				formatters[i]=new IColumnFormatter(){

					public Object format(Object obj) {
						if(obj!=null)
							return obj.toString();
						return null;
					}

					public int getAlign() {
						if("right".equals(align))
							return Element.ALIGN_RIGHT;
						return Element.ALIGN_LEFT;
					}

				};
			} catch (JSONException e) {
			}
                        if(i==columnCount-1)
                        {
                            sb.append("\n");
                        }
                        else
                        {
                            sb.append(separator);
                        }
		}
	}

	public void configure(ResultSetMetaData rsmd)  {
		try {
			columnCount=rsmd.getColumnCount();
			formatters=new IColumnFormatter[columnCount];
			for(int i=0;i<columnCount;i++){
				int type=rsmd.getColumnType(i+1);
				switch(type){
					case Types.DATE:
						formatters[i]=new IColumnFormatter(){
							DateFormat sdfdate = DateFormat.getDateInstance(DateFormat.MEDIUM, Locale.ENGLISH);
							public String format(Object obj) {
								return sdfdate.format((java.sql.Date)obj);
							}

							public int getAlign() {
								return Element.ALIGN_LEFT;
							}

						};
						break;
					case Types.TIME:
						formatters[i]=new IColumnFormatter(){
							DateFormat sdfdate = DateFormat.getTimeInstance(DateFormat.MEDIUM, Locale.ENGLISH);
							public String format(Object obj) {
								return sdfdate.format((java.sql.Time)obj);
							}

							public int getAlign() {
								return Element.ALIGN_LEFT;
							}

						};
						break;
					case Types.TIMESTAMP :
                    case -101 : // Oracle's 'TIMESTAMP WITH TIME ZONE' == -101
                    case -102 :
                    	formatters[i]=new IColumnFormatter(){
							DateFormat sdfdate = DateFormat.getDateTimeInstance(DateFormat.MEDIUM, DateFormat.MEDIUM,Locale.ENGLISH);
							public String format(Object obj) {
								return sdfdate.format((java.sql.Timestamp)obj);
							}

							public int getAlign() {

								return Element.ALIGN_LEFT;
							}

						};
						break;

					case Types.DOUBLE:
					case Types.FLOAT:
					case Types.REAL:
						formatters[i]=new IColumnFormatter(){
							DecimalFormat df=new DecimalFormat("#####################0.00",new DecimalFormatSymbols(Locale.ENGLISH));
							public String format(Object obj) {
								Double d=(Double)obj;
								return df.format(d);
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}

						};
						break;

					case Types.DECIMAL:
					case Types.NUMERIC:
						formatters[i]=new IColumnFormatter(){
							DecimalFormat df=new DecimalFormat("#####################0.00",new DecimalFormatSymbols(Locale.ENGLISH));
							public String format(Object obj) {
								BigDecimal d=(BigDecimal)obj;
								return df.format(d);
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}

						};
						break;
					case Types.BIGINT :
						formatters[i]=new IColumnFormatter(){

							public String format(Object obj) {
								return obj.toString();
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}};
						break;
					case Types.INTEGER:
					case Types.SMALLINT:
					case Types.TINYINT:
						formatters[i]=new IColumnFormatter(){

							public String format(Object obj) {
								return obj.toString();
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}};
							break;
				}

                                sb.append(rsmd.getColumnLabel(i+1));
                                if(i==columnCount-1)
                                {
                                    sb.append("\n");
                                }
                                else
                                {
                                    sb.append(separator);
                                }
			}
                       
		} catch (Exception e) {
		}

	}

	public void newCell(Object obj) {
                column++;
		if(obj!=null){
			IColumnFormatter iformatter = formatters[column-1];
			sb.append(obj.toString());
		}
		else
			sb.append("null");
                if(column!=columnCount)
                {
                    sb.append(separator);
                }
	}

	public void newLine() {
                if(row!=1)
                {
                    sb.append("\n");
                }
                column=0;
                row++;
	}

	public void copyTo(OutputStream os) {
		try {
			baos.writeTo(os);
		} catch (IOException e) {

		}

	}

	public int getContentSize() {
		return baos.size();
	}

	public String getMimeType() {
		return "application/vnd.ms-excel";
	}

	public void finish() {
            try {
                baos.write(sb.toString().getBytes());
            } catch (IOException ex) {
                //Logger.getLogger(CSVTableExporter.class.getName()).log(Level.SEVERE, null, ex);
            }
            sb=null;
	}

}
