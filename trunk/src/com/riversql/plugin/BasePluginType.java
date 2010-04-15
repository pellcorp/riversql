
package com.riversql.plugin;

import java.util.ArrayList;
import java.util.List;

import com.riversql.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.dbtree.IStructureNode;




public abstract class BasePluginType implements IStructureNode {
	protected final String id;
	protected final SQLConnection conn;
	protected final List<IStructureNode>list=new ArrayList<IStructureNode>();
	protected boolean loaded;
	protected IStructureNode parentNode;
	protected String name;
	public IStructureNode getParent(){
		return parentNode;
	}
	public BasePluginType(String name,IStructureNode parentNode, SQLConnection conn){
		this.conn=conn;
		this.id=IDManager.get().nextID();
		IDManager.get().put(id,this);
		this.parentNode=parentNode;
		this.name=name;
	}
	final public String getId() {
		
		return id;
	}
	public JSONObject getChildrenToJSon() {
		load();
		
		
		JSONObject js = new JSONObject();
		try{
			JSONArray arr=new JSONArray();
			for(int i=0;i<list.size();i++){
				JSONObject obj=new JSONObject();
				IStructureNode is = list.get(i);
				obj.put("text", is.getName() );
				obj.put("id",is.getId());
				obj.put("leaf",is.isLeaf());
				obj.put("type",is.getType());
				obj.put("cls",is.getCls());
                                obj.put("qname",is.getQualifiedName());
				arr.put(obj);
			}
			js.put("nodes", arr);
		}
		catch(JSONException e){
			e.printStackTrace();
		}
		return js;
	}

        public List<IStructureNode> getChildren()
        {
            load();
            
            return list;
        }

	public abstract void load();
	
	public  String getName(){return name;}

	public  JSONObject toJSON() throws JSONException {
		JSONObject obj=new JSONObject();
		obj.put("text", getName());
		obj.put("id",getId());
		obj.put("leaf",isLeaf());
		obj.put("type",getType());
		obj.put("cls",getCls());
		return obj;
	}
	public void refresh() {
		loaded=false;
		list.clear();
	}
	public SQLConnection getConn() {
		return conn;
	}

        public String getQualifiedName() {
            if(isLeaf())
            {
		return "'"+((BasePluginType)parentNode).getParent().getName() + "'.'" + name + "'";
            }
            else
            {
                return null;
            }
	}
}
