
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.riversql.IPageAction;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.FileItemFactory;
import org.apache.commons.fileupload.FileUploadException;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;

public abstract class ExportPage implements IPageAction {

    HashMap<String,String> parameterMap;
    HashMap<String, FileItem> fileMap;

    public void loadUploadParameter(HttpServletRequest request)
    {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> items;
        parameterMap=new HashMap<String,String>();
        fileMap=new HashMap<String, FileItem>();
        try {
                //items = (List<FileItem>)upload.parseRequest(request);
                items =upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while(iter.hasNext()){
                        FileItem item = iter.next();
                        if(item.isFormField()){
                                parameterMap.put(item.getFieldName(), item.getString());
                        }else{
                                fileMap.put(item.getFieldName(), item);
                        }
                }
        } catch (FileUploadException e) {
        }

    }

/*
    public void execute(HttpServletRequest request, HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception {
        FileItemFactory factory = new DiskFileItemFactory();
        ServletFileUpload upload = new ServletFileUpload(factory);

        List<FileItem> items;
        HashMap<String,String> parameterMap=new HashMap<String,String>();
        HashMap<String, FileItem> fileMap=new HashMap<String, FileItem>();
        try {
                //items = (List<FileItem>)upload.parseRequest(request);
                items =upload.parseRequest(request);
                Iterator<FileItem> iter = items.iterator();
                while(iter.hasNext()){
                        FileItem item = iter.next();
                        if(item.isFormField()){
                                parameterMap.put(item.getFieldName(), item.getString());
                        }else{
                                fileMap.put(item.getFieldName(), item);
                        }
                }
        } catch (FileUploadException e) {
        }
        String action=parameterMap.get("action");
    }
*/
}
