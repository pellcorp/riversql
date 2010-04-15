
package com.riversql.plugin;

import java.util.ArrayList;
import java.util.List;

import com.riversql.sql.SQLConnection;

import org.json.JSONArray;

import com.riversql.IDManager;
import com.riversql.dbtree.CatalogNode;
import com.riversql.dbtree.IStructureNode;
import com.riversql.dbtree.SQLSession;
import com.riversql.dbtree.SchemaNode;
import com.riversql.plugins.mysql.MySQLPlugin;
import com.riversql.plugins.oracle.OraclePlugin;




public class PluginManager {
	private static PluginManager instance=new PluginManager();
	private PluginManager(){
		plugins.add(new OraclePlugin());
//		plugins.add(new ScriptPlugin());
		plugins.add(new MySQLPlugin());
//		plugins.add(new PostgresPlugin());
//		plugins.add(new SQLServerPlugin());
	}
	public static PluginManager getInstance(){
		return instance;
	}
	
	private ArrayList<Plugin> plugins=new ArrayList<Plugin>();
	
	public void loadSchemaChildren(SchemaNode schemaNode, List<IStructureNode> children, SQLConnection conn) {
		for (Plugin plugin : plugins) {
			try {
				List<IStructureNode> extnodes=plugin.getSchemaAddedChildren(schemaNode,conn);
				if(extnodes!=null && extnodes.size()>0){
					children.addAll(extnodes);
				}
			} catch (Exception e) {
			}
		}
		
	}
	
	public void loadCatalogChildren(CatalogNode catalogNode, List<IStructureNode> children, SQLConnection conn) {
		for (Plugin plugin : plugins) {
			try {
				List<IStructureNode> extnodes=plugin.getCatalogAddedChildren(catalogNode,conn);
				if(extnodes!=null && extnodes.size()>0){
					children.addAll(extnodes);
				}
			} catch (Exception e) {
			}
		}
		
	}
	
	public void dynamicPluginScripts(JSONArray arr,SQLConnection conn){
		for (Plugin plugin : plugins) {
			try{JSONArray[] objs = plugin.getDynamicPluginScripts(conn);
			if(objs!=null ){
				for(int i=0;i<objs.length;i++){
					arr.put(objs[i]);
				}
			}
			}catch(Exception e){}
		}
	}
	public void loadAddedMenu(JSONArray arr, String sessionid, String nodeType) {
		SQLSession sqlsession=(SQLSession)IDManager.get().get(sessionid);
		SQLConnection conn;
		if(sqlsession!=null){
			conn=sqlsession.getConn();
			for (Plugin plugin : plugins) {
				try {
					JSONArray [] objs=plugin.getContextMenu(conn,nodeType);
					if(objs!=null){
						for(int i=0;i<objs.length;i++){
							arr.put(objs[i]);
						}
					}
				} catch (Exception e) {
				}
			}
			
		}
		
	}
	public Plugin getPluginByName(String pluginName) {
		for (Plugin plugin : plugins) {
			if(plugin.getClass().getSimpleName().equals(pluginName)){
				return plugin;
			}
		}
		return null;
	}
	public void loadAddedDetails(JSONArray array, String sessionid,
			String nodeType) {
		SQLSession sqlsession=(SQLSession)IDManager.get().get(sessionid);
		SQLConnection conn;
		if(sqlsession!=null){
			conn=sqlsession.getConn();
			for (Plugin plugin : plugins) {
				try {
					JSONArray [] objs=plugin.getAddedTabs(conn,nodeType);
					if(objs!=null){
						for(int i=0;i<objs.length;i++){
							array.put(objs[i]);
						}
					}
				} catch (Exception e) {
				}
			}
		}
		
	}
}
