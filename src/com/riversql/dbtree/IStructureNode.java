
package com.riversql.dbtree;


import org.json.JSONObject;

public interface IStructureNode {
//	JSONObject toJSON() throws JSONException;
	public JSONObject getChildrenToJSon();
	String getName();
	String getId();
	String getType();
	String getCls();
	boolean isLeaf();

	void refresh();
	public String getQualifiedName();

}
