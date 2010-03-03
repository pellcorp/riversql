
package com.riversql.databases;


import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

public class DBSyntaxGeneratorFactory {
	private DBSyntaxGeneratorFactory(){}
	public static ISyntaxGenerator getSyntaxGenerator( SQLConnection conn){
		if(DialectFactory.isOracle(conn)){
			return new OracleSyntaxGenerator();
		}
		else if(DialectFactory.isMySQL(conn)){
			return new MySqlSyntaxGenerator();
		}else if(DialectFactory.isMSSQL(conn)){
			return new MSSQLSyntaxGenerator();
		}else if(DialectFactory.isPostgreSQL(conn))
			return new PostgreSQLSyntaxGenerator();
		return null;
	}
}
