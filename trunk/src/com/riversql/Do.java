
package com.riversql;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.json.JSONObject;

import com.riversql.actions.AboutPage;
import com.riversql.actions.ChangeCatalog;
import com.riversql.actions.CloseConnection;
import com.riversql.actions.CloseResultSet;
import com.riversql.actions.CommitConnection;
import com.riversql.actions.ConfigPage;
import com.riversql.actions.Connect;
import com.riversql.actions.CreateSource;
import com.riversql.actions.CsvExport;
import com.riversql.actions.DeleteDriver;
import com.riversql.actions.DeleteSource;
import com.riversql.actions.DoExport;
import com.riversql.actions.ExcelExport;
import com.riversql.actions.ExecuteSQL;
import com.riversql.actions.Export;
import com.riversql.actions.ExportTablePage;
import com.riversql.actions.GenerateDDLAlterTable;
import com.riversql.actions.GenerateDDLCreateIndex;
import com.riversql.actions.GenerateDDLCreateTable;
import com.riversql.actions.GetAdditionalData;
import com.riversql.actions.GetColumnsForViewer;
import com.riversql.actions.GetConnectionStatus;
import com.riversql.actions.GetDatabaseMetadata;
import com.riversql.actions.GetDatabases;
import com.riversql.actions.GetDetails;
import com.riversql.actions.GetDrivers;
import com.riversql.actions.GetExportedKeys;
import com.riversql.actions.GetFK;
import com.riversql.actions.GetGrants;
import com.riversql.actions.GetIndexes;
import com.riversql.actions.GetMenu;
import com.riversql.actions.GetMeta;
import com.riversql.actions.GetPK;
import com.riversql.actions.GetPKForAlterTable;
import com.riversql.actions.GetSources;
import com.riversql.actions.GetTableColumns;
import com.riversql.actions.GetTableColumnsForAlterTable;
import com.riversql.actions.GetTableIndexesForAlterTable;
import com.riversql.actions.GetTree;
import com.riversql.actions.GetTypesAlterTable;
import com.riversql.actions.Import;
import com.riversql.actions.PdfExport;
import com.riversql.actions.Ping;
import com.riversql.actions.PluginAction;
import com.riversql.actions.Preview;
import com.riversql.actions.RedoQuery;
import com.riversql.actions.RollbackConnection;
import com.riversql.actions.SourcesPage;
import com.riversql.actions.TestSourceConnection;
import com.riversql.actions.UpdateDriver;
import com.riversql.actions.UpdateSource;




@SuppressWarnings("serial")
public class Do extends DoServlet {
	
    Map<String, Class<? extends JSONAction>> jsonActionMap;
    Map<String, Class<? extends IPageAction>> pageActionMap;
    //Map<String, Class<? extends IFileUploadAction>> fileUploadActionMap;

    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);

        HashMap<String, Class<? extends JSONAction>> tmp = new HashMap<String, Class<? extends JSONAction>>();
        tmp.put("generateDDLAlterTable",GenerateDDLAlterTable.class);
        tmp.put("generateDDLCreateTable",GenerateDDLCreateTable.class);
        tmp.put("generateDDLCreateIndex",GenerateDDLCreateIndex.class);
        tmp.put("getTableColumns",GetTableColumns.class);
        tmp.put("getTableIndexesAlterTable",GetTableIndexesForAlterTable.class);
        tmp.put("getTableColumnsAlterTable",GetTableColumnsForAlterTable.class);
        tmp.put("getPKAlterTable",GetPKForAlterTable.class);
        tmp.put("getTypesAlterTable",GetTypesAlterTable.class);
        tmp.put("getMenu",GetMenu.class);
        tmp.put("pluginAction",PluginAction.class);
        tmp.put("getPK",GetPK.class);
        tmp.put("getFK",GetFK.class);
        tmp.put("getMeta",GetMeta.class);
        tmp.put("getExportedKeys",GetExportedKeys.class);
        tmp.put("getAdditionalData",GetAdditionalData.class);
        tmp.put("redoQuery", RedoQuery.class);
        tmp.put("changeCatalog",ChangeCatalog.class);
        tmp.put("getColumnsForViewer",GetColumnsForViewer.class);
        tmp.put("getSources",GetSources.class);
        tmp.put("getIndexes",GetIndexes.class);
        tmp.put("getDatabaseMetadata",GetDatabaseMetadata.class);
        tmp.put("getConnectionStatus",GetConnectionStatus.class);
        tmp.put("getGrants",GetGrants.class);
        tmp.put("closeResultSet", CloseResultSet.class);
        tmp.put("commitConnection",CommitConnection.class);
        tmp.put("closeConnection",CloseConnection.class);
        tmp.put("rollbackConnection", RollbackConnection.class);
        tmp.put("getDrivers",GetDrivers.class);
        tmp.put("getDetails",GetDetails.class);
        tmp.put("deleteDriver",DeleteDriver.class);
        tmp.put("updateDriver",UpdateDriver.class);
        tmp.put("preview",Preview.class);
        tmp.put("ping", Ping.class);

        tmp.put("updateSource",UpdateSource.class);

        tmp.put("createSource",CreateSource.class);


        tmp.put("deleteSource",DeleteSource.class);


        tmp.put("getTree",GetTree.class);
        tmp.put("getDatabases",GetDatabases.class);

        tmp.put("testSourceConnection",TestSourceConnection.class);
        tmp.put("connect",Connect.class);

        tmp.put("execute",ExecuteSQL.class);
        tmp.put("export",Export.class);
        tmp.put("import",Import.class);
        jsonActionMap=Collections.unmodifiableMap(tmp);

        HashMap<String, Class<? extends IPageAction>> tmp2 = new HashMap<String, Class<? extends IPageAction>>();


        //tmp2.put("selectTable",SelectTable.class);

        tmp2.put("about",AboutPage.class);
        tmp2.put("config",ConfigPage.class);

        tmp2.put("sourcesPage",SourcesPage.class);
        tmp2.put("exportTablePage", ExportTablePage.class);
        tmp2.put("doExport",DoExport.class);

        tmp2.put("excelExport",ExcelExport.class );
        tmp2.put("pdfExport", PdfExport.class);
        tmp2.put("csvExport", CsvExport.class);

        pageActionMap=Collections.unmodifiableMap(tmp2);

        /*
        HashMap<String, Class<? extends IFileUploadAction>> tmp3 = new HashMap<String, Class<? extends IFileUploadAction>>();
        tmp3.put("excelExport",ExcelExport.class );
        tmp3.put("pdfExport", PdfExport.class);
        tmp3.put("csvExport", CsvExport.class);
        fileUploadActionMap=Collections.unmodifiableMap(tmp3);

        */
    }

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, EntityManager em, EntityTransaction et) throws Exception {
        String action=req.getParameter("action");
        Class<? extends JSONAction> iactionclass=jsonActionMap.get(action);
        if(iactionclass!=null){
                JSONObject obj=new JSONObject();
                PrintWriter writer=resp.getWriter();
                try {
                        JSONAction iaction=iactionclass.newInstance();
                        BeanUtils.populate(iaction, req.getParameterMap());

                        et=em.getTransaction();
                        et.begin();
                        JSONObject objsr=iaction.execute(req, resp, em,et);
                        if(et.isActive())
                                et.commit();
                        resp.setHeader("Content-Type", "text/html;charset=ISO-8859-1");
                        obj.put("success",true);
                        if(objsr!=null)
                                obj.put("result",objsr);
                }
                catch (Exception e) {
                        //e.printStackTrace();
                        if(et!=null && et.isActive())
                                et.rollback();
                        try {

                                obj.put("success",false);
                                obj.put("error",e.toString());
                        } catch (JSONException e1) {
                        }

                }finally{
                        IDManager.set(null);
                        if(em!=null)
                                em.close();
                }
                writer.write(obj.toString());
        }else{
                Class<? extends IPageAction> iPageActionclass=pageActionMap.get(action);
                if(iPageActionclass!=null){
                        try{
                                IPageAction iPageAction=iPageActionclass.newInstance();
                                BeanUtils.populate(iPageAction, req.getParameterMap());
                                et=em.getTransaction();
                                et.begin();
                                iPageAction.execute(req, resp, em,et);
                                et.commit();
                        }catch(Exception e){
                                //e.printStackTrace();
                                //TODO return page with error
                                if(et!=null && et.isActive())
                                        et.rollback();
                                try{
                                        req.setAttribute("pageid", req.getParameter("pageid"));
                                        req.setAttribute("emsg",e.getMessage());
                                        StringWriter sw = new StringWriter();
                                        PrintWriter pw = new PrintWriter(sw);
                                        e.printStackTrace(pw);
                                        pw.close();sw.close();

                                        req.setAttribute("error",sw.toString());
                                        req.getRequestDispatcher("error.jsp").forward(req, resp);
                                }catch(Exception e1){}
                        }finally{
                                IDManager.set(null);
                                if(em!=null)
                                        em.close();
                        }
                }else
                        if(em!=null)
                                em.close();
        }
    }
}
