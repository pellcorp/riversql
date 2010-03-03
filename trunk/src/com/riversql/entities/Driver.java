
package com.riversql.entities;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EntityManager;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

@NamedQueries(value={
		@NamedQuery(name="Driver.selectAllByUser",query="SELECT p FROM Driver p where p.userName=?1 order by  p.driverName "),
		@NamedQuery(name="Driver.selectAll",query="SELECT p FROM Driver p order by  p.driverName")
})

@Entity
@Table(name = "DRIVER")
public class Driver {
	
	@Column(name = "USER_NAME",nullable=false,length=255)
	String userName;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	@Column(length=255,nullable=false)
	String driverName;
	
	@Column(length=600)
	String exampleUrl;
	
	@Column(length=600,nullable=false)
	String driverClassName;
	
	@Column(length=600)
	String iconUrl="ext/resources/images/default/s.gif";
	
	@Id
	@GeneratedValue
	@Column(name = "DRIVER_ID")
	int id;
	
	private boolean valid;
	public String getDriverName() {
		return driverName;
	}
	public void setDriverName(String driverName) {
		this.driverName = driverName;
	}
	public String getExampleUrl() {
		return exampleUrl;
	}
	public void setExampleUrl(String exampleUrl) {
		this.exampleUrl = exampleUrl;
	}
	public String getDriverClassName() {
		return driverClassName;
	}
	public void setDriverClassName(String driverClassName) {
		this.driverClassName = driverClassName;
		this.valid=validate();
	}
	public int getId() {
		return id;
	}
	public void setId(int id) {
		this.id = id;
	}
	private boolean validate(){
		try{
			Class<?> clazz=Class.forName(driverClassName);
			if(java.sql.Driver.class.isAssignableFrom(clazz))
				return true;
		}catch (Exception e) {
			return false;
		}
		return false;
		
	}
	public boolean isValid() {
		return valid;
	}
	public String getIconurl() {
		return iconUrl;
	}
	public void setIconUrl(String iconurl) {
		this.iconUrl = iconurl;
	}
	public void updateValidation(EntityManager em) {
		this.valid=validate();
		em.merge(this);
		
	}
}
