/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.plugins.mysql.actions;

import com.riversql.plugin.BasePluginType;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;
import org.json.JSONArray;
import org.json.JSONObject;

/**
 *
 * @author river.liao
 */
public class GetUserInfo {
        private SQLConnection conn;
        private BasePluginType node;
	public GetUserInfo(SQLConnection conn, BasePluginType node) {
		this.conn=conn;
                this.node=node;
	}

        public final JSONObject execute() throws Exception{
                String userHost = node.getName();
                HashMap<String, JSONArray> schema=new HashMap<String, JSONArray>();

		JSONObject results=new JSONObject();
		JSONArray userinfos=new JSONArray();
                JSONArray schemaprivileges=new JSONArray();
                JSONArray userlimits=new JSONArray();

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
                            //userinfos.put(getUserName(node.getName()));
                            //userinfos.put(getHost(node.getName()));
                            //userinfos.put(rs.getString(1));
                            //userinfos.put(rs.getString(2));
                            //userinfos.put(rs.getString(3));
                    }

                    rs.close();
                    ps.close();

                    sql = "SHOW DATABASES;";
                    ps=(PreparedStatement) conn.prepareStatement(sql);
                    rs=ps.executeQuery();
                    while(rs.next()){
                            schema.put(rs.getString(1),new JSONArray());
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
                            String privilegetype = rs.getString(2);
                            System.out.println(tableschema+"==============="+privilegetype);
                            schema.get(tableschema).put(privilegetype);
                    }
                    for(Map.Entry <String, JSONArray> entry: schema.entrySet()){
                            JSONArray tempschemaprivileges=new JSONArray();
                            tempschemaprivileges.put(entry.getKey());
                            tempschemaprivileges.put(entry.getValue());
                            schemaprivileges.put(tempschemaprivileges);
                    }
                    rs.close();
                    ps.close();
                    
                    sql = "SELECT max_questions,max_updates,max_connections,max_user_connections FROM mysql.`user` where user= '"+getUserName(userHost)+"' and host='"+getHost(userHost)+"'";
                    ps=(PreparedStatement) conn.prepareStatement(sql);
                    rs=ps.executeQuery();
                    if(rs.next()){
                            userlimits.put(rs.getString(1));
                            userlimits.put(rs.getString(2));
                            userlimits.put(rs.getString(3));
                            userlimits.put(rs.getString(4));
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

		results.put("userinfos",userinfos);
                results.put("schemaprivileges",schemaprivileges);
		results.put("userlimits",userlimits);
		return results;
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
