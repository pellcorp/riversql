
package com.riversql.dao;

import java.util.Iterator;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.Query;

import com.riversql.entities.Driver;




public class DriversDAO {
	public static void deleteDriver(EntityManager em, String id) {
		Driver drv=em.find(Driver.class, Integer.valueOf(id));
		em.remove(drv);
		
	}
	@SuppressWarnings("unchecked")
	public  static  List<Driver> getDrivers(EntityManager em, String userName) {
		//Query q=em.createNamedQuery("Driver.selectAllByUser");
		Query q=em.createNamedQuery("Driver.selectAll");
		//q.setParameter(1,userName);
		List<Driver> ls=q.getResultList();
		return ls;
	}
	@SuppressWarnings("unchecked")
	public  static  List<Driver> getDrivers(EntityManager em) {
		Query q=em.createNamedQuery("Driver.selectAll");
		List<Driver> ls=q.getResultList();
		return ls;
	}
	public  static  Driver getDriver(EntityManager em,int id) {
		Driver driver=em.find(Driver.class, Integer.valueOf(id));
		return driver;
		
	}
	
	public static void updateDriver(EntityManager em, int driverid, String drivername, String driverclass,String exampleurl) {
		Driver drv=em.find(Driver.class, Integer.valueOf(driverid));
		drv.setDriverClassName(driverclass);
		drv.setDriverName(drivername);
		drv.setExampleUrl(exampleurl);
		em.merge(drv);
	}
	
	public static void initialize(EntityManager em){
		Driver drv1=new Driver();
		drv1.setDriverClassName("oracle.jdbc.OracleDriver");
		drv1.setDriverName("Oracle Thin Driver");
		drv1.setExampleUrl("jdbc:oracle:thin:@<server>[:<1521>]:<database_name>");
		drv1.setIconUrl("ico/oracle.ico");
		drv1.setUserName("admin");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("com.mysql.jdbc.Driver");
		drv1.setDriverName("MySQL Driver");
		drv1.setIconUrl("ico/mysql.ico");
		drv1.setExampleUrl("jdbc:mysql://<hostname>[,<failoverhost>][<:3306>]/<dbname>[?<param1>=<value1>][&<param2>=<value2>]");
		em.persist(drv1);
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.apache.derby.jdbc.ClientDriver");
		drv1.setDriverName("Apache Derby Client");
		drv1.setExampleUrl("jdbc:derby://<server>[:<port>]/<databaseName>[;<URL attribute>=<value>]");
		drv1.setIconUrl("ico/apache.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.apache.derby.jdbc.EmbeddedDriver");
		drv1.setDriverName("Apache Derby Embedded");
		drv1.setExampleUrl("jdbc:derby:<database>[;create=true]");
		drv1.setIconUrl("ico/apache.ico");
		em.persist(drv1);
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.axiondb.jdbc.AxionDriver");
		drv1.setDriverName("Axion");
		drv1.setExampleUrl("jdbc:axiondb:<database-name>[:<database-directory>]</");
		drv1.setIconUrl("ico/axion.ico");
		em.persist(drv1);
		
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.firebirdsql.jdbc.FBDriver");
		drv1.setDriverName("Firebird JayBird");
		drv1.setExampleUrl("jdbc:firebirdsql:[//host[:port]/]<database>");
		drv1.setIconUrl("ico/firebirdsql.jpg");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.h2.Driver");
		drv1.setDriverName("H2");
		drv1.setExampleUrl("jdbc:h2://<server>:<9092>/<db-name>");
		drv1.setIconUrl("ico/h2.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.h2.Driver");
		drv1.setDriverName("H2 Embedded");
		drv1.setExampleUrl("jdbc:h2://<db-name>");
		drv1.setIconUrl("ico/h2.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.h2.Driver");
		drv1.setDriverName("H2 In-Memory");
		drv1.setExampleUrl("jdbc:h2:mem:");
		drv1.setIconUrl("ico/h2.ico");
		em.persist(drv1);
		
		
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.hsqldb.jdbcDriver");
		drv1.setDriverName("HSQLDB In-Memory");
		drv1.setExampleUrl("jdbc:hsqldb:.");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.hsqldb.jdbcDriver");
		drv1.setDriverName("HSQLDB Server");
		drv1.setExampleUrl("jdbc:hsqldb:hsql://<server>[:<1476>]");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.hsqldb.jdbcDriver");
		drv1.setDriverName("HSQLDB Standalone");
		drv1.setExampleUrl("jdbc:hsqldb:<databaseName>");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.hsqldb.jdbcDriver");
		drv1.setDriverName("HSQLDB Web Server");
		drv1.setExampleUrl("jdbc:hsqldb:http://<server>[:<1476>]");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("COM.ibm.db2.jdbc.app.DB2Driver");
		drv1.setDriverName("IBM DB2 App Driver");
		drv1.setExampleUrl("jdbc:db2:<dbname>");
		drv1.setIconUrl("ico/ibm.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("com.ibm.db2.jcc.DB2Driver");
		drv1.setDriverName("IBM DB2 Net Driver");
		drv1.setExampleUrl("jdbc:db2://<server>:<6789>/<db-name>");
		drv1.setIconUrl("ico/ibm.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("com.informix.jdbc.IfxDriver");
		drv1.setDriverName("Informix");
		drv1.setExampleUrl("jdbc:informix-sqli://<host_name>:<port_number>/<database_name>:INFORMIXSERVER=<server_name>");
		
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.enhydra.instantdb.jdbc.idbDriver");
		drv1.setDriverName("InstantDB");
		drv1.setExampleUrl("jdbc:idb:<pathname>");
		
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("interbase.interclient.Driver");
		drv1.setDriverName("InterClient");
		drv1.setExampleUrl("jdbc:interbase://<server>/<full_db_path>");
		drv1.setIconUrl("ico/borland.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("com.intersys.jdbc.CacheDriver");
		drv1.setDriverName("Intersystems Cache");
		drv1.setExampleUrl("jdbc:Cache://<host>:1972/<database>");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("sun.jdbc.odbc.JdbcOdbcDriver");
		drv1.setDriverName("JDBC ODBC Bridge");
		drv1.setExampleUrl("jdbc:odbc:<alias>");
		drv1.setIconUrl("ico/sun.ico");
		em.persist(drv1);
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("com.internetcds.jdbc.tds.Driver");
		drv1.setDriverName("JTDS");
		drv1.setExampleUrl("jdbc:freetds:sqlserver://<hostname>[:<4100>]/<dbname>[;<property>=<value>[;...]]");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		drv1.setDriverName("JTDS Microsoft SQL");
		drv1.setExampleUrl("jdbc:jtds:sqlserver://<hostname>[:<1433>]/<dbname>[;<property>=<value>[;...]]");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("net.sourceforge.jtds.jdbc.Driver");
		drv1.setDriverName("JTDS Sybase");
		drv1.setExampleUrl("jdbc:jtds:sybase://<hostname>[:<4100>]/<dbname>[;<property>=<value>[;...]]");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("com.mckoi.JDBCDriver");
		drv1.setDriverName("Mckoi");
		drv1.setExampleUrl("jdbc:mckoi://<host>[:9157][/<schema>]/");
		drv1.setIconUrl("ico/mckoi.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
		drv1.setDriverName("Microsoft MSSQL Server JDBC Driver");
		drv1.setExampleUrl("jdbc:microsoft:sqlserver://<server_name>:<1433>");
		drv1.setIconUrl("ico/msdn.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("oracle.jdbc.OracleDriver");
		drv1.setDriverName("Oracle OCI Driver");
		drv1.setExampleUrl("jdbc:oracle:oci8:@<database_name>");
		drv1.setIconUrl("ico/oracle.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("openlink.jdbc3.Driver");
		drv1.setDriverName("Progress");
		drv1.setExampleUrl("");
		drv1.setIconUrl("ico/progress.ico");
		em.persist(drv1);
		
		drv1=new Driver();
		drv1.setUserName("admin");
		drv1.setDriverClassName("org.postgresql.Driver");
		drv1.setDriverName("PostgreSQL");
		drv1.setExampleUrl("jdbc:postgresql:[<//host>[:<5432>/]]<database>");
		drv1.setIconUrl("ico/postgresql.ico");
		em.persist(drv1);
	}
	public static void addDriver(EntityManager em, String drivername,
			String driverclass, String exampleurl, String userName) {
		Driver drv1 = new Driver();
		drv1.setDriverClassName(driverclass);
		drv1.setDriverName(drivername);
		drv1.setExampleUrl(exampleurl);
		drv1.setUserName(userName);
		drv1.setIconUrl("");
		em.persist(drv1);
		
	}
	public static void validateDrivers(EntityManager em) {
		List<Driver> drivers= getDrivers(em);
		for (Iterator<Driver> iterator = drivers.iterator(); iterator.hasNext();) {
			Driver driver =  iterator.next();
			driver.updateValidation(em);
		}
		
	}
	public static void deleteAll(EntityManager em) {
		List<Driver> drivers= getDrivers(em);
		for (Iterator<Driver> iterator = drivers.iterator(); iterator.hasNext();) {
			Driver driver =  iterator.next();
			em.remove(driver);
		}
		
	}
}
