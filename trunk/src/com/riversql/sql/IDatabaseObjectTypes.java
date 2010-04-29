package com.riversql.sql;

/**
 * 
 * Defines the different types of database objects.
 *
 * @author  <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public interface IDatabaseObjectTypes
{
	int GENERIC_LEAF = 0;
	int GENERIC_FOLDER = 1;
	int DATABASE = 2;
	int SCHEMA = 3;
	int CATALOG = 4;
	int TABLE = 5;
	int PROCEDURE = 6;
	int UDT = 7;
	int INDEX = 8;

	/**
	 * An object that generates uniques IDs for primary keys. E.G. an Oracle
	 * sequence.
	 */
	int SEQUENCE = 8;

	/**
	 * This isn't an object type but rather is a guarantee that no value in this
	 * interface will ever be greater than <TT>LAST_USED</TT>.
	 */
	int LAST_USED_OBJECT_TYPE = 9999;
}
