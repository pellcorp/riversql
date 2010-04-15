
package com.riversql.plugins.oracle;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.sql.SQLConnection;

import org.json.JSONArray;
import org.json.JSONObject;

import com.riversql.databases.DialectFactory;
import com.riversql.dbtree.CatalogNode;
import com.riversql.dbtree.IStructureNode;
import com.riversql.dbtree.SchemaNode;
import com.riversql.plugin.Plugin;
import com.riversql.plugins.oracle.actions.DependentObjects;
import com.riversql.plugins.oracle.actions.Directories;
import com.riversql.plugins.oracle.actions.DirectoryInfo;
import com.riversql.plugins.oracle.actions.FunctInfo;
import com.riversql.plugins.oracle.actions.FunctionParametersDetail;
import com.riversql.plugins.oracle.actions.Functions;
import com.riversql.plugins.oracle.actions.JavaClassInfo;
import com.riversql.plugins.oracle.actions.JavaClasses;
import com.riversql.plugins.oracle.actions.JavaInfo;
import com.riversql.plugins.oracle.actions.JavaResInfo;
import com.riversql.plugins.oracle.actions.JavaResources;
import com.riversql.plugins.oracle.actions.JavaSources;
import com.riversql.plugins.oracle.actions.Libraries;
import com.riversql.plugins.oracle.actions.LibraryInfo;
import com.riversql.plugins.oracle.actions.ObjectBodyTypeInfo;
import com.riversql.plugins.oracle.actions.ObjectBodyTypes;
import com.riversql.plugins.oracle.actions.ObjectTypeInfo;
import com.riversql.plugins.oracle.actions.ObjectTypes;
import com.riversql.plugins.oracle.actions.PackageBodys;
import com.riversql.plugins.oracle.actions.Packages;
import com.riversql.plugins.oracle.actions.PkgBodyInfo;
import com.riversql.plugins.oracle.actions.PkgInfo;
import com.riversql.plugins.oracle.actions.ProcInfo;
import com.riversql.plugins.oracle.actions.ProcedureParametersDetail;
import com.riversql.plugins.oracle.actions.Procedures;
import com.riversql.plugins.oracle.actions.SeqDetail;
import com.riversql.plugins.oracle.actions.SeqInfo;
import com.riversql.plugins.oracle.actions.Sequences;
import com.riversql.plugins.oracle.actions.SystemOptions;
import com.riversql.plugins.oracle.actions.TriggerDetail;
import com.riversql.plugins.oracle.actions.TriggerInfo;
import com.riversql.plugins.oracle.actions.Triggers;





public class OraclePlugin implements Plugin {

	public List<IStructureNode> getSchemaAddedChildren(SchemaNode schemaNode,SQLConnection conn) {
		List<IStructureNode> added=new ArrayList<IStructureNode>();
		if(isOracle(conn)){
			
			added.add(new PackageTypeNode(schemaNode,conn));
			added.add(new PackageBodyTypeNode(schemaNode,conn));
			added.add(new SequenceTypeNode(schemaNode,conn));
			added.add(new FunctionTypeNode(schemaNode,conn));
			added.add(new ProcedureTypeNode(schemaNode,conn));
			added.add(new TriggerTypeNode(schemaNode,conn));
			added.add(new JavaTypeNode(schemaNode,conn));
			added.add(new JavaResourceTypeNode(schemaNode,conn));
			added.add(new JavaClassTypeNode(schemaNode,conn));
			added.add(new ObjectTypeNode(schemaNode,conn));
			added.add(new ObjectTypeBodyNode(schemaNode,conn));
			added.add(new LibraryTypeNode(schemaNode,conn));
			added.add(new DirectoryTypeNode(schemaNode,conn));
		}
		return added;
	}
	private boolean isOracle(SQLConnection conn)
	{
		return DialectFactory.isOracle(conn);
	}
	private boolean isUpperOracle9(SQLConnection conn)
	{
		final String ORACLE9 = "oracle9";
		final String ORACLE10 = "oracle database 10";
		String dbms = null;
		try
		{
			dbms = conn.getSQLMetaData().getDatabaseProductVersion();
			
		}
		catch (SQLException ex)
		{
		}
		return dbms != null && (dbms.toLowerCase().startsWith(ORACLE9) || dbms.toLowerCase().startsWith(ORACLE10));
	}
	public JSONArray[] getContextMenu(SQLConnection conn, String nodeType) {
		List<JSONArray> ls=new ArrayList<JSONArray>();
		if(isOracle(conn)){
			if("tb".equals(nodeType)
					||"ora_pkg".equals(nodeType)
					||"ora_seq".equals(nodeType)
					||"ora_trig".equals(nodeType)
					||"ora_funct".equals(nodeType)
					||"ora_proc".equals(nodeType)
					||"ora_pkgbody".equals(nodeType)
					){
				if(isUpperOracle9(conn)){
					JSONArray obj=new JSONArray();
					obj.put("Extract DDL");
					obj.put("icons/lightning.png");
					obj.put("getTextAndOpenEditor('do?action=pluginAction&pluginName=OraclePlugin&method=extractDDL&nodeid='+menuTreeC.nodeid.id)");
					ls.add(obj);
					
				}
			}
		}
		return ls.toArray(new JSONArray[0]);
	}
	public JSONObject executeAction(HttpServletRequest request,
			HttpServletResponse response, 
			EntityManager em, EntityTransaction et) throws Exception{
		String nodeid=request.getParameter("nodeid");
		String id=request.getParameter("id");
		String method=request.getParameter("method");
//		if("extractDDL".equals(method)){
//			return new ExtractDLL(nodeid).execute();
//		}
//		else 
		if("triggerInfo".equals(method)){
		
			return new TriggerInfo(id).execute();
		}
		else if("procInfo".equals(method)){
			return new ProcInfo(id).execute();
		}
		else if("procParameters".equals(method)){
			return new ProcedureParametersDetail(id).execute();
		}
		else if("functInfo".equals(method)){
			return new FunctInfo(id).execute();
		}
		else if("javaInfo".equals(method)){
			return new JavaInfo(id).execute();
		}
		else if("functParameters".equals(method)){
			return new FunctionParametersDetail(id).execute();
		}
		else if("pkgInfo".equals(method)){
			return new PkgInfo(id).execute();
		}
		else if("pkgBodyInfo".equals(method)){
			return new PkgBodyInfo(id).execute();
		}
		else if("seqInfo".equals(method)){
			return new SeqInfo(id).execute();
		}
		else if("seqDetail".equals(method)){
			return new SeqDetail(id).execute();
		}
		else if("triggerDetail".equals(method)){
			return new TriggerDetail(id).execute();
		}
		else if("triggers".equals(method)){
			return new Triggers(id).execute();
		}
		else if("procedures".equals(method)){
			return new Procedures(id).execute();
		}
		else if("functions".equals(method)){
			return new Functions(id).execute();
		}
		else if("packages".equals(method)){
			return new Packages(id).execute();
		}
		else if("packagebs".equals(method)){
			return new PackageBodys(id).execute();
		}
		else if("sequences".equals(method)){
			return new Sequences(id).execute();
		}
		else if("types".equals(method)){
			return new ObjectTypes(id).execute();
		}
		else if("bodytypes".equals(method)){
			return new ObjectBodyTypes(id).execute();
		}
		else if("javas".equals(method)){
			return new JavaSources(id).execute();
		}
		else if("systemOptions".equals(method)){
			return new SystemOptions(id).execute();
		}else if ("depObjs".equals(method)){
			return new DependentObjects(id).execute();
		}else if("objectTypeInfo".equals(method)){
			return new ObjectTypeInfo(id).execute();
		}else if("bodyTypeInfo".equals(method)){
			return new ObjectBodyTypeInfo(id).execute();
		}else if("javaress".equals(method)){
			return new JavaResources(id).execute();
		}else if("javaclasses".equals(method)){
			return new JavaClasses(id).execute();
		}
		else if("javaResInfo".equals(method)){
			return new JavaResInfo(id).execute();
		}
		else if("javaClassInfo".equals(method)){
			return new JavaClassInfo(id).execute();
		}else if("libraries".equals(method)){
			return new Libraries(id).execute();
		}else if("directories".equals(method)){
			return new Directories(id).execute();
		}else if("libraryInfo".equals(method)){
			return new LibraryInfo(id).execute();
		}else if("directoryInfo".equals(method)){
			return new DirectoryInfo(id).execute();
		}
		return new JSONObject();
	}
	public JSONArray[] getAddedTabs(SQLConnection conn, String nodeType) {
		if(isOracle(conn)){
			List<JSONArray> ls=new ArrayList<JSONArray>();
			if("tb".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Dependent Objects");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=depObjs");
				ls.add(record);	
			}
			if("dtbs".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("System Options");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=systemOptions");
				ls.add(record);
			}
			if("ora_trig".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Trigger Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=triggerInfo");
				ls.add(record);
				record=new JSONArray();
				record.put("Trigger Detail");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=triggerDetail");
				ls.add(record);
			}
			else if("ora_proc".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Procedure Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=procInfo");
				ls.add(record);
				record=new JSONArray();
				record.put("Procedure Parameters");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=procParameters");
				ls.add(record);
				record=new JSONArray();
				record.put("Dependent Objects");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=depObjs");
				ls.add(record);
			}
			else if("ora_funct".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Function Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=functInfo");
				ls.add(record);
				record=new JSONArray();
				record.put("Function Parameters");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=functParameters");
				ls.add(record);
				record=new JSONArray();
				record.put("Dependent Objects");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=depObjs");
				ls.add(record);
			}else if("ora_pkg".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Package Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=pkgInfo");
				ls.add(record);
				record=new JSONArray();
				record.put("Dependent Objects");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=depObjs");
				ls.add(record);
			}
			else if("ora_pkgbody".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Package Body Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=pkgBodyInfo");
				ls.add(record);
			}
			else if("ora_seq".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Sequence Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=seqInfo");
				ls.add(record);
				record=new JSONArray();
				record.put("Sequence Detail");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=seqDetail");
				ls.add(record);
			}
			else if("ora_trgs".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Triggers");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=triggers");
				ls.add(record);
			}
			else if("ora_procs".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Procedures");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=procedures");
				ls.add(record);
			}
			else if("ora_functs".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Functions");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=functions");
				ls.add(record);
			}
			else if("ora_pkgs".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Packages");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=packages");
				ls.add(record);
			}
			else if("ora_pkgbodys".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Package Body");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=packagebs");
				ls.add(record);
			}
			else if("ora_seqs".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Sequences");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=sequences");
				ls.add(record);
			}
			else if("ora_types".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Object Types");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=types");
				ls.add(record);
			}
			else if("ora_bodytypes".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Object Type Bodies");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=bodytypes");
				ls.add(record);
			}
			else if("ora_bodytype".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Object Type Body Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=bodyTypeInfo");
				ls.add(record);
			}
			else if("ora_type".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Object Type Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=objectTypeInfo");
				ls.add(record);
			}
			else if("ora_javas".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Java Sources");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=javas");
				ls.add(record);
			}else if ("ora_java".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Java Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=javaInfo");
				ls.add(record);
			}else if("ora_javaress".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Java Resources");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=javaress");
				ls.add(record);
			}
			else if("ora_javares".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Java Resource Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=javaResInfo");
				ls.add(record);
			}else if("ora_javaclasses".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Java Classes");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=javaclasses");
				ls.add(record);
			}else if("ora_libraries".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Libraries");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=libraries");
				ls.add(record);
			}else if("ora_directories".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Directories");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=directories");
				ls.add(record);
			}
			else if("ora_javaclass".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Java Class Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=javaClassInfo");
				ls.add(record);
			}else if("ora_library".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Java Class Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=libraryInfo");
				ls.add(record);
			}
			else if("ora_directory".equals(nodeType)){
				JSONArray record=new JSONArray();
				record.put("Directory Info");
				record.put("do?action=pluginAction&pluginName=OraclePlugin&method=directoryInfo");
				ls.add(record);
			}
			return ls.toArray(new JSONArray[0]);
		}
		return null;
	}
	
	public List<IStructureNode> getCatalogAddedChildren(CatalogNode c,
			SQLConnection conn) {
		// TODO Auto-generated method stub
		return null;
	}
	public JSONArray[] getDynamicPluginScripts(SQLConnection conn) {
		// TODO Auto-generated method stub
		return null;
	}
}
