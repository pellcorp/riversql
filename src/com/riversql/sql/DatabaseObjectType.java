package com.riversql.sql;

import java.io.Serializable;

import com.riversql.id.IHasIdentifier;
import com.riversql.id.IIdentifier;
import com.riversql.id.IntegerIdentifierFactory;
import com.riversql.util.StringManager;
import com.riversql.util.StringManagerFactory;

/**
 *
 * Defines the different types of database objects.
 *
 * @author <A HREF="mailto:colbell@users.sourceforge.net">Colin Bell</A>
 */
public class DatabaseObjectType implements IHasIdentifier, Serializable
{
   static final long serialVersionUID = 2325635336825122256L;
   
   /** Internationalized strings for this class. */
   private static final StringManager s_stringMgr =
      StringManagerFactory.getStringManager(DatabaseObjectType.class);

   /** Factory to generate unique IDs for these objects. */
   private final static IntegerIdentifierFactory s_idFactory = new IntegerIdentifierFactory();

   /** Other - general purpose. */
   public final static DatabaseObjectType OTHER = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.other"));

   /** Catalog. */
   public final static DatabaseObjectType BEST_ROW_ID = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.bestRowID"));

   /** Catalog. */
   public final static DatabaseObjectType CATALOG = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.catalog"));

   /** Column. */
   public final static DatabaseObjectType COLUMN = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.column"));

   /** Database. */
   public final static DatabaseObjectType SESSION = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.database"));


   /**
    * Datbase object type for a "Database" node in the object tree.  There is onle one
    * node of this type in the object tree and it indicates the alias of the database.
    */
   public final static DatabaseObjectType DATABASE_TYPE_DBO = DatabaseObjectType.createNewDatabaseObjectType("Database Type");


   /** Standard datatype. */
   public final static DatabaseObjectType DATATYPE = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.datatype"));

    /** Unique Key for a table. */
    public final static DatabaseObjectType PRIMARY_KEY = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.primarykey"));

   /** Foreign Key relationship. */
   public final static DatabaseObjectType FOREIGN_KEY = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.foreignkey"));

   /** Function. */
   public final static DatabaseObjectType FUNCTION = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.function"));

   /** Index. */
   public final static DatabaseObjectType INDEX = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.index"));

   /** Stored procedure. */
   public final static DatabaseObjectType PROCEDURE = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.storproc"));

   /**
    * Database object type for a "Procedure Type" node in the object tree. There is
    * only one node of this type in the object tree and it says "PROCEDURE".
    */
   public final static DatabaseObjectType PROC_TYPE_DBO = DatabaseObjectType.createNewDatabaseObjectType("Stored Procedure Type");



   /** Schema. */
   public final static DatabaseObjectType SCHEMA = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.schema"));

   /**
    * An object that generates uniques IDs for primary keys. E.G. an Oracle
    * sequence.
    */
   public final static DatabaseObjectType SEQUENCE = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.sequence"));

   /** TABLE. */
   public final static DatabaseObjectType TABLE = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.table"));

   /**
    * Database object type for a "Table Type" node in the object tree. Some examples
    * are "TABLE", "SYSTEM TABLE", "VIEW" etc.
    */
   public final static DatabaseObjectType TABLE_TYPE_DBO = DatabaseObjectType.createNewDatabaseObjectType("Table Type");



   public static final DatabaseObjectType VIEW = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.view"));

   /** Trigger. */
   public final static DatabaseObjectType TRIGGER = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.catalog"));

   /** User defined type. */
   public final static DatabaseObjectType UDT = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.udt"));

   /**
    * Database object type for a "UDT Type" node in the object tree. There is only one
    * node of this type in the object tree and it says "UDT".
    */
   public final static DatabaseObjectType UDT_TYPE_DBO = DatabaseObjectType.createNewDatabaseObjectType("UDT Type");




   /** A database user. */
   public final static DatabaseObjectType USER = createNewDatabaseObjectType(s_stringMgr.getString("DatabaseObjectType.user"));

   /** Uniquely identifies this Object. */
   private final IIdentifier _id;

   /** Describes this object type. */
   private final String _name;

   /**
    * Default ctor.
    */
   private DatabaseObjectType(String name)
   {
      super();
      _id = s_idFactory.createIdentifier();
      _name = name != null ? name : _id.toString();
   }

   /**
    * Return the object that uniquely identifies this object.
    *
    * @return	Unique ID.
    */
   public IIdentifier getIdentifier()
   {
      return _id;
   }

   /**
    * Retrieve the descriptive name of this object.
    *
    * @return	The descriptive name of this object.
    */
   public String getName()
   {
      return _name;
   }

   public String toString()
   {
      return getName();
   }

   public static DatabaseObjectType createNewDatabaseObjectType(String name)
   {
      return new DatabaseObjectType(name);
   }
}
