
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;

import com.riversql.JSONAction;
import com.riversql.plugin.Plugin;
import com.riversql.plugin.PluginManager;




public class PluginAction implements JSONAction {

	String pluginName;
	String pluginAction;
	public void setPluginAction(String pluginAction) {
		this.pluginAction = pluginAction;
	}
	public void setPluginName(String pluginName) {
		this.pluginName = pluginName;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		Plugin plug=PluginManager.getInstance().getPluginByName(pluginName);
		if(plug!=null){
			JSONObject str=plug.executeAction(request,response, em,et);
			return str;
		}
		return null;
	}

}
