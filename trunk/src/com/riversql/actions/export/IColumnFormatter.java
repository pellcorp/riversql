
package com.riversql.actions.export;

public interface IColumnFormatter {

	public Object format(Object obj);

	public int getAlign();

}
