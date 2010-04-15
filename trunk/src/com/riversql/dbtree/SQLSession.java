
package com.riversql.dbtree;


import com.riversql.sql.SQLConnection;
import com.riversql.IDManager;

public class SQLSession {
	private String id;
	private SQLConnection conn;
	private String sessionName;
	DatabaseNode dn;
	private int sourceid;
	public SQLSession(int sourceid,String sessionName,SQLConnection conn){
		this.sourceid=sourceid;
		this.id=IDManager.get().nextID();
		IDManager.get().put(id,this);
		this.conn=conn;
		this.sessionName=sessionName;
		dn = new DatabaseNode(conn,sessionName);
	}
	public DatabaseNode getDatabaseNode(){
		return dn;
	}

	public int getSourceid() {
		return sourceid;
	}
	public SQLConnection getConn() {
		return conn;
	}

	public void setConn(SQLConnection conn) {
		this.conn = conn;
	}

	public String getId() {
		return id;
	}
	public String getSessionName() {
		return sessionName;
	}
	public void setSessionName(String sessionName) {
		this.sessionName = sessionName;
	}
}
