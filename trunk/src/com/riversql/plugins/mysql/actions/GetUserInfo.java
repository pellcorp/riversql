/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.plugins.mysql.actions;

import com.riversql.IDManager;
import com.riversql.actions.GetJSONObjectBase;
import com.riversql.plugin.BasePluginType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import com.riversql.sql.SQLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author river.liao
 */
public class GetUserInfo extends GetJSONObjectBase{
        private SQLConnection conn;
        private BasePluginType node;
        private boolean isLoaded;
        private JSONArray userinfos;
        private JSONArray schemaprivileges;
        private JSONArray userlimits;
	
        public final JSONObject execute() throws Exception{
            node=(BasePluginType)IDManager.get().get(nodeId);
            conn=node.getConn();
            if(!isLoaded)
            {
                load();
            }
            String method = request.getParameter("method");
            JSONObject results=new JSONObject();
            if(method.equalsIgnoreCase("userInfo"))
            {
                results.put("userinfos",userinfos);
                return results;
            }else if(method.equalsIgnoreCase("schemaprivileges"))
            {
                results.put("schemaprivileges",schemaprivileges);
                return results;
            }else if(method.equalsIgnoreCase("userlimits"))
            {
                results.put("userlimits",userlimits);
                return results;
            }
            return results;
        }

        public final void load() throws Exception{
            
            String userHost = node.getName();
            HashMap<String, ArrayList<String>> schema=new HashMap<String, ArrayList<String>>();

            userinfos=new JSONArray();
            schemaprivileges=new JSONArray();
            userlimits=new JSONArray();

            PreparedStatement ps = null;
            ResultSet rs=null;
            try {
                String sql = "SELECT full_name,description,email FROM mysql.user_info where user= '"+getUserName(userHost)+"'";
                ps=(PreparedStatement) conn.prepareStatement(sql);
                rs=ps.executeQuery();
                if(rs.next()){

                    JSONObject userObject = new JSONObject();
                    userObject.put("username", getUserName(node.getName()));
                    userObject.put("host", getHost(node.getName()));
                    userObject.put("fullname", rs.getString(1));
                    userObject.put("description", rs.getString(2));
                    userObject.put("email", rs.getString(3));
                    userinfos.put(userObject);

                    /*
                    userinfos.put(getUserName(node.getName()));
                    userinfos.put(getHost(node.getName()));
                    userinfos.put(rs.getString(1));
                    userinfos.put(rs.getString(2));
                    userinfos.put(rs.getString(3));
                    */
                }

                rs.close();
                ps.close();

                sql = "SHOW DATABASES;";
                ps=(PreparedStatement) conn.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next()){
                        schema.put(rs.getString(1),new ArrayList());
                }
                rs.close();
                ps.close();

                ArrayList<String> privilegeList = new ArrayList<String>();
                sql = "SHOW PRIVILEGES;";
                ps=(PreparedStatement) conn.prepareStatement(sql);
                rs=ps.executeQuery();
                while(rs.next()){
                    privilegeList.add(rs.getString(1).toUpperCase());
                }
                rs.close();
                ps.close();

                sql="SELECT table_schema,privilege_type FROM information_schema.SCHEMA_PRIVILEGES where is_grantable='YES' and grantee=? order by table_schema,privilege_type";
                ps=conn.prepareStatement(sql);
                //userHost.replaceAll("'", "''")
                ps.setString(1,userHost);
                rs=ps.executeQuery();
                while(rs.next()){
                    String tableschema = rs.getString(1).replace('\\', ' ').replaceAll(" ", "");
                    String privilegetype = rs.getString(2).toUpperCase();
                    schema.get(tableschema).add(privilegetype);
                    //JSONArray privilege = new JSONArray();
                    //schema.get(tableschema).put(privilege.put(privilegetype));
                }
                for(Map.Entry <String, ArrayList<String>> entry: schema.entrySet()){
                    JSONObject tempschemaprivileges=new JSONObject();
                    tempschemaprivileges.put("databasename",entry.getKey());

                    ArrayList<String> allPrivilege = (ArrayList) privilegeList.clone();
                    JSONArray privilegeSelected = new JSONArray();
                    for(String privilege : entry.getValue())
                    {
                        JSONArray privilegeJSONObject = new JSONArray();
                        privilegeSelected.put(privilegeJSONObject.put(privilege));

                        allPrivilege.remove(privilege);
                    }

                    JSONArray privilegeAvailable = new JSONArray();
                    for(String privilege : allPrivilege)
                    {
                        JSONArray privilegeJSONObject = new JSONArray();
                        privilegeAvailable.put(privilegeJSONObject.put(privilege));
                    }

                    tempschemaprivileges.put("privilegeavailable", privilegeAvailable);
                    tempschemaprivileges.put("privilegeselected",privilegeSelected);
                    schemaprivileges.put(tempschemaprivileges);
                }
                rs.close();
                ps.close();

                sql = "SELECT max_questions,max_updates,max_connections,max_user_connections FROM mysql.`user` where user= '"+getUserName(userHost)+"' and host='"+getHost(userHost)+"'";
                ps=(PreparedStatement) conn.prepareStatement(sql);
                rs=ps.executeQuery();
                if(rs.next()){
                    JSONObject userLimitsObject = new JSONObject();
                    userLimitsObject.put("max_questions",rs.getString(1));
                    userLimitsObject.put("max_updates",rs.getString(2));
                    userLimitsObject.put("max_connections",rs.getString(3));
                    userLimitsObject.put("max_user_connections",rs.getString(4));
                    userlimits.put(userLimitsObject);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                //System.out.println(e.getMessage());
                //System.out.println(e.toString());
            }finally{
                try {if(rs!=null)
                        rs.close();
                } catch (SQLException e) {
                }
                try {
                        if(ps!=null)
                                ps.close();
                } catch (SQLException e) {
                }
            }

	}

        private String getUserName(String userHost)
        {
            String userName = "";
            userName = userHost.substring(1, userHost.indexOf("@")-1);
            return userName;
        }

        private String getHost(String userHost)
        {
            String host = "";
            host = userHost.substring(userHost.indexOf("@")+2, userHost.length()-1);
            return host;
        }
}
