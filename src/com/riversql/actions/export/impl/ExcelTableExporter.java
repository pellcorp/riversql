
package com.riversql.actions.export.impl;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.sql.ResultSetMetaData;
import java.sql.Time;
import java.sql.Timestamp;
import java.sql.Types;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFDataFormat;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.usermodel.HSSFRichTextString;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;

import com.itextpdf.text.Element;

import com.riversql.actions.export.IColumnFormatter;
import com.riversql.actions.export.ITableExporter;

public class ExcelTableExporter implements ITableExporter {

	private HSSFWorkbook workbook;
	private final String qualifiedName;
	private HSSFCellStyle dateCellStyle;
	private HSSFCellStyle bstyle;
	private HSSFSheet sheet;
	ByteArrayOutputStream baos=new ByteArrayOutputStream(1024*1024);

	private int row=1;
	private int column=0;
	private HSSFRow currentRow;
	IColumnFormatter formatters[];
	private HSSFCellStyle dateTimeCellStyle;
	private HSSFCellStyle timeCellStyle;
	public ExcelTableExporter(String qualifiedName) {
		this.qualifiedName = qualifiedName;
		workbook=new HSSFWorkbook();
		dateCellStyle = workbook.createCellStyle();
		dateCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy"));
		dateTimeCellStyle= workbook.createCellStyle();
		dateTimeCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("m/d/yy h:mm"));
		timeCellStyle= workbook.createCellStyle();
		timeCellStyle.setDataFormat(HSSFDataFormat.getBuiltinFormat("h:mm:ss"));
		HSSFFont fntbold = workbook.createFont();
		fntbold.setBoldweight(HSSFFont.BOLDWEIGHT_BOLD);
		bstyle = workbook.createCellStyle();
		bstyle.setFont(fntbold);
		sheet = workbook.createSheet("export");
	}

	public void configure(ResultSetMetaData rsmd) {
		try{
			int columnCount = rsmd.getColumnCount();
			currentRow=sheet.createRow(row++);
			for(int i=0;i<columnCount;i++){
				HSSFCell cell = currentRow.createCell(column++);
				
				cell.setCellValue(new HSSFRichTextString(rsmd.getColumnLabel(i+1)));
				
			}
			formatters=new IColumnFormatter[columnCount];
			for(int i=0;i<columnCount;i++){
				int type=rsmd.getColumnType(i+1);
				switch(type){
					case Types.DATE:
						formatters[i]=new IColumnFormatter(){
							public java.sql.Date format(Object obj) {
								
								return ((java.sql.Date)obj);
							}

							public int getAlign() {
								
								return Element.ALIGN_LEFT;
							}
							
						};
						break;
					case Types.TIME:
						formatters[i]=new IColumnFormatter(){
							
							public java.sql.Time format(Object obj) {
								
								return ((java.sql.Time)obj);
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
							
							public java.sql.Timestamp format(Object obj) {
								
								return ((java.sql.Timestamp)obj);
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
							
							public Double format(Object obj) {
								Double d=(Double)obj;
								return d;
							}

							public int getAlign() {
								
								return Element.ALIGN_RIGHT;
							}
							
						};
						break;
					
					case Types.DECIMAL:
					case Types.NUMERIC:
						formatters[i]=new IColumnFormatter(){
							
							public BigDecimal format(Object obj) {
								BigDecimal d=(BigDecimal)obj;
								return d;
							}

							public int getAlign() {
								
								return Element.ALIGN_RIGHT;
							}
							
						};
						break;
					case Types.BIGINT :
						formatters[i]=new IColumnFormatter(){

							public Long format(Object obj) {
								return (Long)obj;
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}};
						break;
					case Types.INTEGER:
					case Types.SMALLINT:
					case Types.TINYINT:
						formatters[i]=new IColumnFormatter(){

							public Integer format(Object obj) {
								return (Integer)obj;
							}

							public int getAlign() {
								return Element.ALIGN_RIGHT;
							}};
							break;
				}
			}
		}catch(Exception e){
			e.printStackTrace();
		}

	}

	public void newCell(Object obj) {
		HSSFCell cell = currentRow.createCell(column++);
		if(obj!=null){
			IColumnFormatter iformatter = formatters[column-1];
			if(iformatter!=null){
				Object robj = iformatter.format(obj);
				
				if (robj instanceof Double){
					cell.setCellValue( ((Double)robj).doubleValue());
				}else if( robj instanceof Long ){
					cell.setCellValue( ((Long)robj).doubleValue());
				}else if(robj instanceof Integer){
					cell.setCellValue( ((Integer)robj).doubleValue());
				}else if(robj instanceof BigInteger){
					cell.setCellValue( ((BigInteger)robj).doubleValue());
				}else if(robj instanceof Timestamp){
					cell.setCellValue( (Timestamp)robj);
					cell.setCellStyle(dateTimeCellStyle);
				}else if(robj instanceof Time){
					cell.setCellValue( (Time)robj);
					cell.setCellStyle(timeCellStyle);
				}
				else if(robj instanceof java.sql.Date){
					cell.setCellValue((java.util.Date)robj);
					cell.setCellStyle(dateCellStyle);
				}
				else cell.setCellValue(new HSSFRichTextString(obj.toString()));
			}else{
				cell.setCellValue(new HSSFRichTextString(obj.toString()));
			}
			
		}
	}

	public void newLine() {
		column=0;
		currentRow=sheet.createRow(row++);

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
			workbook.write(baos);
		} catch (IOException e) {
			
		}
	}

}
