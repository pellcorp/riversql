
package com.riversql.actions;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.RowIdLifetime;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.sql.SQLDatabaseMetaData;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.IDManager;
import com.riversql.JSONAction;
import com.riversql.dbtree.DatabaseNode;


public class GetDatabaseMetadata implements JSONAction {
	String id;
	public void setId(String id) {
		this.id = id;
	}
	public JSONObject execute(HttpServletRequest request,
			HttpServletResponse response, EntityManager em, EntityTransaction et)
			throws Exception {
		DatabaseNode dn=(DatabaseNode)IDManager.get().get(id);
		JSONObject results=new JSONObject();
		JSONArray meta=new JSONArray();
		JSONArray data=new JSONArray();
		
		String []strs={"Property","Value"};
		
		for(int i=0;i<strs.length;i++){
			meta.put(strs[i]);	
		}
		
		SQLDatabaseMetaData metaData=dn.getConn().getSQLMetaData();
		DatabaseMetaData jdbcmetadata=metaData.getJDBCMetaData();
		JSONArray record=new JSONArray();
		record.put("Database Product Name");
		try{
			record.put(metaData.getDatabaseProductName());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Database Product Version");
		try{
			record.put(metaData.getDatabaseProductVersion());
			
		}catch(Throwable e){record.put("Unsupported");}
		
		data.put(record);
		
		record=new JSONArray();
		record.put("Driver Major Version");
		try{
			record.put(jdbcmetadata.getDriverMajorVersion());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Driver Minor Version");
		try{
			record.put(jdbcmetadata.getDriverMinorVersion());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Driver Name");
		try{
			record.put(metaData.getDriverName());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Driver Version");
		try{
			record.put(jdbcmetadata.getDriverVersion());	
		}catch(Throwable e){record.put("Unsupported");}
		
		data.put(record);
		
		record=new JSONArray();
		record.put("Username");
		try{
			record.put(metaData.getUserName());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("URL");
		try{
			record.put(jdbcmetadata.getURL());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
//		record=new JSONArray();
//		record.put("Auto Commit");
//		try{
//			record.put(dn.getConn().getAutoCommit());
//			
//		}catch(Throwable e){record.put("Unsupported");}
//		data.put(record);
		
		record=new JSONArray();
		record.put("All Procedures Are Callable");
		try{
			record.put(jdbcmetadata.allProceduresAreCallable());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("All Tables Are Selectable");
		try{
			record.put(jdbcmetadata.allTablesAreSelectable());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Nulls are sorted High");
		try{
			record.put(jdbcmetadata.nullsAreSortedHigh());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Nulls are sorted Low");
		try{
			record.put(jdbcmetadata.nullsAreSortedLow());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Nulls are sorted at Start");
		try{
			record.put(jdbcmetadata.nullsAreSortedAtStart());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Nulls are sorted at End");
		try{
			record.put(jdbcmetadata.nullsAreSortedAtEnd());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Is Read Only");
		try{
			record.put(jdbcmetadata.isReadOnly());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Result Set Holdability");
		try{
			record.put(jdbcmetadata.getResultSetHoldability());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Uses Local Files");
		try{
			record.put(jdbcmetadata.usesLocalFiles());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Uses Local File per Table");

		try{
			record.put(jdbcmetadata.usesLocalFilePerTable());

		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);

		record=new JSONArray();
		record.put("Supports Mixed Case Identifiers");

		try{
			record.put(jdbcmetadata.supportsMixedCaseIdentifiers());

		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Stores Upper Case Identifiers");

		try{
			record.put(jdbcmetadata.storesUpperCaseIdentifiers());

		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Stores Lower Case Identifiers");		
		try{
			record.put(jdbcmetadata.storesLowerCaseIdentifiers());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Stores Mixed Case Identifiers");		
		try{
			record.put(jdbcmetadata.storesMixedCaseIdentifiers());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Mixed Case Quoted Identifiers");
		try{
			record.put(jdbcmetadata.supportsMixedCaseQuotedIdentifiers());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Stores Upper Case Quoted Identifiers");
		
		try{
			record.put(jdbcmetadata.storesUpperCaseQuotedIdentifiers());

		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Stores Lower Case Quoted Identifiers");

		try{
			record.put(jdbcmetadata.storesLowerCaseQuotedIdentifiers());

		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Stores Mixed Case Quoted Identifiers");
		try{
			record.put(jdbcmetadata.storesMixedCaseQuotedIdentifiers());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Identifier Quote");
		try{
			record.put(jdbcmetadata.getIdentifierQuoteString());
		}catch(Throwable e){
		}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Search String Escape");
		try{
			record.put(jdbcmetadata.getSearchStringEscape());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Extra Name Characters");
		try{
			record.put(jdbcmetadata.getExtraNameCharacters());
			
		}catch(Throwable e){record.put("Unsupported");}
		
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Alter Table With Add Column");
		try{	
			record.put(jdbcmetadata.supportsAlterTableWithAddColumn());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Alter Table With Drop Column");
		try{
			
			record.put(jdbcmetadata.supportsAlterTableWithDropColumn());
			
		}catch(Throwable e){record.put("Unsupported");}
		
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Column Aliasing");
		try{
			
			record.put(jdbcmetadata.supportsColumnAliasing());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		
		record=new JSONArray();
		record.put("Null Plus Non Null Is Null");
		try{
			
			record.put(jdbcmetadata.nullPlusNonNullIsNull());
			
		}catch(Throwable e){record.put("Unsupported");}
		
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Convert");
		try{
			
			record.put(jdbcmetadata.supportsConvert());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Table Correlation Names");
		try{
			
			record.put(jdbcmetadata.supportsTableCorrelationNames());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Expressions in Order By");
		try{
			
			record.put(jdbcmetadata.supportsExpressionsInOrderBy());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Order By Unrelated");
		try{
			
			record.put(jdbcmetadata.supportsOrderByUnrelated());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Group By");
		try{
			
			record.put(jdbcmetadata.supportsGroupBy());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Group By Unrelated");
		try{
			
			record.put(jdbcmetadata.supportsGroupByUnrelated());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Group By Beyond Select");
		try{
			record.put(jdbcmetadata.supportsGroupByBeyondSelect());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Like Escape Clause");
		try{
			record.put(jdbcmetadata.supportsLikeEscapeClause());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Multiple Result Sets");
		try{
			record.put(jdbcmetadata.supportsMultipleResultSets());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Multiple Open Results");
		try{
			record.put(jdbcmetadata.supportsMultipleOpenResults());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Multiple Transactions");
		try{
			
			record.put(jdbcmetadata.supportsMultipleTransactions());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Non Nullable Columns");
		try{
			record.put(jdbcmetadata.supportsNonNullableColumns());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Minimum SQL Grammar");
		try{
			record.put(jdbcmetadata.supportsMinimumSQLGrammar());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Core SQL Grammar");
		try{
			record.put(jdbcmetadata.supportsCoreSQLGrammar());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);		
		
		
		record=new JSONArray();
		record.put("Supports Extended SQL Grammar");
		try{
			
			record.put(jdbcmetadata.supportsExtendedSQLGrammar());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports ANSI92 Entry Level SQL");
		try{
			record.put(jdbcmetadata.supportsANSI92EntryLevelSQL());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports ANSI92 Intermediate SQL");
		try{
			
			record.put(jdbcmetadata.supportsANSI92IntermediateSQL());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports ANSI92 Full SQL");
		try{
			
			record.put(jdbcmetadata.supportsANSI92FullSQL());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Integrity Enhancement Facility");
		try{
			
			record.put(jdbcmetadata.supportsIntegrityEnhancementFacility());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Outer Joins");
		try{
			
			record.put(jdbcmetadata.supportsOuterJoins());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Full Outer Joins");
		try{
			
			record.put(jdbcmetadata.supportsFullOuterJoins());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Limited Outer Joins");
		try{
			record.put(jdbcmetadata.supportsLimitedOuterJoins());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Schema Term");
		try{
			record.put(jdbcmetadata.getSchemaTerm());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Procedure Term");
		try{
			record.put(jdbcmetadata.getProcedureTerm());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Catalog Term");
		try{
			record.put(jdbcmetadata.getCatalogTerm());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Is Catalog at Start");
		try{
			record.put(jdbcmetadata.isCatalogAtStart());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Catalog Separator");		
		try{
			record.put(jdbcmetadata.getCatalogSeparator());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Schemas In Data Manipulation");
		try{
			record.put(jdbcmetadata.supportsSchemasInDataManipulation());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Schemas In Procedure Calls");
		try{
			record.put(jdbcmetadata.supportsSchemasInProcedureCalls());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Schemas In Table Definitions");
		try{
			record.put(jdbcmetadata.supportsSchemasInTableDefinitions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Schemas In Index Definitions");
		try{	
			record.put(jdbcmetadata.supportsSchemasInIndexDefinitions());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Schemas In Privilege Definitions");
		try{
			record.put(jdbcmetadata.supportsSchemasInPrivilegeDefinitions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Catalogs In Data Manipulation");
		try{
			record.put(jdbcmetadata.supportsCatalogsInDataManipulation());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Catalogs In Procedure Calls");
		try{
			record.put(jdbcmetadata.supportsCatalogsInProcedureCalls());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Catalogs In Table Definitions");
		try{
			record.put(jdbcmetadata.supportsCatalogsInTableDefinitions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Catalogs In Index Definitions");
		try{
			record.put(jdbcmetadata.supportsCatalogsInIndexDefinitions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Catalogs In Privilege Definitions");
		try{	
			record.put(jdbcmetadata.supportsCatalogsInPrivilegeDefinitions());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Positioned Delete");
		try{
			record.put(jdbcmetadata.supportsPositionedDelete());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Positioned Update");
		try{
			record.put(jdbcmetadata.supportsPositionedUpdate());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Select For Update");
		try{
			record.put(jdbcmetadata.supportsSelectForUpdate());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);		
		
		record=new JSONArray();
		record.put("Supports Stored Procedures");
		try{
			record.put(jdbcmetadata.supportsStoredProcedures());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Subqueries In Comparisons");

		try{
			record.put(jdbcmetadata.supportsSubqueriesInComparisons());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Subqueries In Exists");
		try{
			record.put(jdbcmetadata.supportsSubqueriesInExists());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Subqueries in IN Statements");
		try{
			record.put(jdbcmetadata.supportsSubqueriesInIns());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Correlated Subqueries");
		try{
			record.put(jdbcmetadata.supportsCorrelatedSubqueries());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Union");
		try{
			record.put(jdbcmetadata.supportsUnion());

		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);		
		
		record=new JSONArray();
		record.put("Supports Union All");
		try{
			record.put(jdbcmetadata.supportsUnionAll());
		
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Open Cursors Across Commit");
		try{
			record.put(jdbcmetadata.supportsOpenCursorsAcrossCommit());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Open Cursors Across Rollback");
		try{
			record.put(jdbcmetadata.supportsOpenCursorsAcrossRollback());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Open Statements Across Commit");
		try{
			record.put(jdbcmetadata.supportsOpenStatementsAcrossCommit());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Open Statements Across Rollback");		
		try{
			record.put(jdbcmetadata.supportsOpenStatementsAcrossRollback());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Binary Literal Length");
		try{
			record.put(jdbcmetadata.getMaxBinaryLiteralLength());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Char Literal Length");
		try{
			record.put(jdbcmetadata.getMaxCharLiteralLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Column Name Length");
		try{
			record.put(jdbcmetadata.getMaxColumnNameLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Columns In Group By");
		try{
			record.put(jdbcmetadata.getMaxColumnsInGroupBy());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Columns In Index");
		try{
			record.put(jdbcmetadata.getMaxColumnsInIndex());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Columns In Order By");
		try{
			record.put(jdbcmetadata.getMaxColumnsInOrderBy());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Columns In Select");
		try{
			
			record.put(jdbcmetadata.getMaxColumnsInSelect());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Columns In Table");
		try{
			
			record.put(jdbcmetadata.getMaxColumnsInTable());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Connections");
		try{
			record.put(jdbcmetadata.getMaxConnections());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Cursor Name Length");
		try{
			record.put(jdbcmetadata.getMaxCursorNameLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Index Length");
		try{
			record.put(jdbcmetadata.getMaxIndexLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Schema Name Length");
		try{
			record.put(jdbcmetadata.getMaxSchemaNameLength());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Procedure Name Length");
		try{
			
			record.put(jdbcmetadata.getMaxProcedureNameLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Catalog Name Length");
		try{
			
			record.put(jdbcmetadata.getMaxCatalogNameLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Row Size");
		try{
			
			record.put(jdbcmetadata.getMaxRowSize());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Row Size Include Blobs");
		try{
			
			record.put(jdbcmetadata.doesMaxRowSizeIncludeBlobs());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Statement Length");
		try{
			
			record.put(jdbcmetadata.getMaxStatementLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Statements");
		try{
			record.put(jdbcmetadata.getMaxStatements());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max Table Name Length");
		try{
			record.put(jdbcmetadata.getMaxTableNameLength());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Max Tables In Select");
		try{
			record.put(jdbcmetadata.getMaxTablesInSelect());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Max User Name Length");
		try{
			record.put(jdbcmetadata.getMaxUserNameLength());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Default Transaction Isolation");
		try{
			
			int isol=jdbcmetadata.getDefaultTransactionIsolation();
			String is=null;
			switch (isol){
				case java.sql.Connection.TRANSACTION_NONE :
					{
						is = "TRANSACTION_NONE";
						break;
					}
				case java.sql.Connection.TRANSACTION_READ_COMMITTED :
					{
						is = "TRANSACTION_READ_COMMITTED";
						break;
					}
				case java.sql.Connection.TRANSACTION_READ_UNCOMMITTED :
					{
						is = "TRANSACTION_READ_UNCOMMITTED";
						break;
					}
				case java.sql.Connection.TRANSACTION_REPEATABLE_READ :
					{
						is = "TRANSACTION_REPEATABLE_READ";
						break;
					}
				case java.sql.Connection.TRANSACTION_SERIALIZABLE :
					{
						is = "TRANSACTION_SERIALIZABLE";
						break;
					}
				default :
				{
					is = "";
					break;
				}
			}
			record.put(is);
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Transactions");
		try{
			record.put(jdbcmetadata.supportsTransactions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Data Definition and Data Manipulation Transactions");
		try{
			record.put(jdbcmetadata.supportsDataDefinitionAndDataManipulationTransactions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Different Table Correlation Names");
		try{
			record.put(jdbcmetadata.supportsDifferentTableCorrelationNames());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Data Manipulation Transactions Only");
		try{
			record.put(jdbcmetadata.supportsDataManipulationTransactionsOnly());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Data Definition Causes Transaction Commit");
		try{
			record.put(jdbcmetadata.dataDefinitionCausesTransactionCommit());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Data Definition Ignored in Transactions");
		try{
			record.put(jdbcmetadata.dataDefinitionIgnoredInTransactions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Batch Updates");
		try{
			record.put(jdbcmetadata.supportsBatchUpdates());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Savepoints");
		try{
			record.put(jdbcmetadata.supportsSavepoints());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Named Parameters");
		try{
			record.put(jdbcmetadata.supportsNamedParameters());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Get Generated Keys");
		try{
			record.put(jdbcmetadata.supportsGetGeneratedKeys());	
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Database Major Version");
		try{
			record.put(jdbcmetadata.getDatabaseMajorVersion());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Database Minor Version");
		try{
			record.put(jdbcmetadata.getDatabaseMinorVersion());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("JDBC Minor Version");
		try{
			record.put(jdbcmetadata.getJDBCMinorVersion());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("JDBC Major Version");
		try{
			record.put(jdbcmetadata.getJDBCMajorVersion());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("SQL State Type");
		try{
			int sqlStateType=jdbcmetadata.getSQLStateType();
			String is=null;
			switch(sqlStateType){
				case DatabaseMetaData.sqlStateXOpen:{
					is="sqlStateXOpen";
					break;
				}
				case DatabaseMetaData.sqlStateSQL:{
					is="sqlStateSQL";
					break;
				}
				default:
					is="";
			}
			record.put(is);
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Locators Update Copy");
		try{
			record.put(jdbcmetadata.locatorsUpdateCopy());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Statement Pooling");
		try{
			record.put(jdbcmetadata.supportsStatementPooling());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("SQL Keywords");
		try{
			
			record.put(jdbcmetadata.getSQLKeywords());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Numeric Functions");
		try{
			record.put(jdbcmetadata.getNumericFunctions());
			
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("String Functions");
		try{
			record.put(jdbcmetadata.getStringFunctions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("System Functions");
		try{
			record.put(jdbcmetadata.getSystemFunctions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Time and Date Functions");
		try{
			record.put(jdbcmetadata.getTimeDateFunctions());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Auto Commit Failure Closes All ResultSets");
		try{
			record.put(jdbcmetadata.autoCommitFailureClosesAllResultSets());
		}catch(Throwable e){record.put("Unsupported");}
		
		data.put(record);
		
		
		record=new JSONArray();
		record.put("Supports Stored Functions Using Call Syntax");
		try{
			record.put(jdbcmetadata.supportsStoredFunctionsUsingCallSyntax());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Supports Subqueries in Quantified Expressions");
		try{
			record.put(jdbcmetadata.supportsSubqueriesInQuantifieds());
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("Client Info Properties");
		try{
			ResultSet rs=jdbcmetadata.getClientInfoProperties();
			while(rs.next()){
				rs.getString("NAME");
			}
			rs.close();
		}catch(Throwable e){record.put("Unsupported");}
		data.put(record);
		
		record=new JSONArray();
		record.put("ROWID Lifetime");
		try{
			RowIdLifetime rid=jdbcmetadata.getRowIdLifetime();
			if(rid.equals(RowIdLifetime.ROWID_UNSUPPORTED))
				record.put("ROWID_UNSUPPORTED");
			else if(rid.equals(RowIdLifetime.ROWID_VALID_FOREVER))
				record.put("ROWID_VALID_FOREVER");
			else if(rid.equals(RowIdLifetime.ROWID_VALID_OTHER))
				record.put("ROWID_VALID_OTHER");
			else if(rid.equals(RowIdLifetime.ROWID_VALID_SESSION))
				record.put("ROWID_VALID_SESSION");
			else if(rid.equals(RowIdLifetime.ROWID_VALID_TRANSACTION))
				record.put("ROWID_VALID_TRANSACTION");
			else record.put("");
		}catch(Throwable e){record.put("Unsupported");}
		
		data.put(record);
		
		
		results.put("meta",meta);
		results.put("data",data);
		return results;
		
	}

}
