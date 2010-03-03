
package com.riversql.dbtree;

public class ColumnModel {
	Object []el;
	public ColumnModel(int size){el=new Object[size];}
	public ColumnModel(Object[] obj) {
		el= new Object[8];
		el[0]=obj[3];//("COLUMN_NAME"); 
		el[1]=obj[4];//("COLUMN_NAME");
		el[2]= obj[5];//set.getString("TYPE_NAME");//map.get(new Integer(set.getShort("DATA_TYPE")));				
		//el[2]=new Integer(obj[6].toString());//new Integer(set.getInt("COLUMN_SIZE")); 
		el[3]=obj[6];//new Integer(set.getInt("COLUMN_SIZE"));
		//el[3]=new Integer(obj[8].toString());//new Integer(set.getInt("DECIMAL_DIGITS"));
		el[4]=obj[8];//new Integer(set.getInt("DECIMAL_DIGITS")); 
		el[7]=obj[11];//set.getString("REMARKS"); 
		el[5]=obj[12];//set.getString("COLUMN_DEF"); 
		el[6]=obj[17];
	}
	public Object getValue(int k){
		return el[k];
	}
	public void setValue(int k, Object value){
		el[k]=value;
	}
}
