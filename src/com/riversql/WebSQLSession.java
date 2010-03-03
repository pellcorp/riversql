

package com.riversql;


import java.io.Serializable;
import java.sql.SQLException;
import java.util.ArrayList;

import com.riversql.dbtree.SQLSession;
import com.riversql.utils.SQLExecutor;


@SuppressWarnings("serial")
public class WebSQLSession implements Serializable{
	
	
	private transient ArrayList<SQLSession> sqlsessions;
	private transient ArrayList<SQLExecutor> executors;
	private transient IDManager idmanager;
	private String username;
	public WebSQLSession(){
		this.idmanager=new IDManager();
		this.sqlsessions=new ArrayList<SQLSession>();
		executors=new ArrayList<SQLExecutor>();
	}
	public void closeSessions() {
		for (SQLExecutor exec : getExecutors()) {
			try {
				exec.close();
			} catch (Exception e) {
			}
		}
		for (SQLSession conn : getSqlsessions()) {
			try {
				conn.getConn().close();
			} catch (SQLException e) {
			}
		}
		
	}
	
	public ArrayList<SQLExecutor> getExecutors() {
		if( executors==null){
			executors=new ArrayList<SQLExecutor>();	
		}
		return executors;
		
	}
	public void setExecutors(ArrayList<SQLExecutor> executors) {
		this.executors = executors;
	}
	public void closeSession(SQLSession sqlsession) {
		try {
			sqlsession.getConn().close();
		} catch (SQLException e) {
		}
		getSqlsessions().remove(sqlsession);
		
	}
	public void setSqlsessions(ArrayList<SQLSession> sqlsessions) {
		this.sqlsessions = sqlsessions;
	}
	public ArrayList<SQLSession> getSqlsessions() {
		if(sqlsessions==null){
			sqlsessions=new ArrayList<SQLSession>();
		}
		return sqlsessions;
	}
	public IDManager getIDManager() {
		if(idmanager==null){
			idmanager=new IDManager();
		}
		return idmanager;
	}
	public void setUser(String username) {
		this.username=username;
		
	}
	public String getUser() {
		return username;
	}
}
