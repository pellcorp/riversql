/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.actions;

import com.riversql.JSONAction;
import java.io.File;
import java.util.Iterator;
import java.util.List;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.fileupload.FileItem;
import org.apache.commons.fileupload.disk.DiskFileItemFactory;
import org.apache.commons.fileupload.servlet.ServletFileUpload;
import org.json.JSONObject;

/**
 *
 * @author river.liao
 */
public class Import implements JSONAction {
    final long MAX_SIZE = 3 * 1024 * 1024;
    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception {
        
        String file_name = "";
        response.setContentType("text/html");
        response.setCharacterEncoding("UTF-8");

        DiskFileItemFactory dfif = new DiskFileItemFactory();
        dfif.setSizeThreshold(1024*100);
        dfif.setRepository(new File(request.getRealPath("/") + "/WEB-INF/upload"));

        ServletFileUpload sfu = new ServletFileUpload(dfif);
        sfu.setSizeMax(MAX_SIZE);
        List fileList = null;
        try {
            fileList = sfu.parseRequest(request);
        } catch (Exception e) {
            e.printStackTrace();
        }
        Iterator fileItr = fileList.iterator();
        while (fileItr.hasNext()) {
            FileItem fileItem = null;
            String path = null;
            long size = 0;
            fileItem = (FileItem) fileItr.next();
            //ignore <input type="text" />
            if (fileItem == null || fileItem.isFormField()) {
                continue;
            }

            path = fileItem.getName();
            size = fileItem.getSize();
            String t_name = path.substring(path.lastIndexOf("\\") + 1);
            String t_ext = t_name.substring(t_name.lastIndexOf(".") + 1);
            long now = System.currentTimeMillis();
            String prefix = String.valueOf(now);
            file_name = prefix + "." + t_ext;
            String u_name = request.getRealPath("/")+"/WEB-INF/upload/" + prefix + "." + t_ext;
            try {
                fileItem.write(new File(u_name));
                System.out.println("Upload succeed with name: " + prefix + "." + t_ext
                    + " &nbsp;&nbsp;fielzone: " + size + "<p />");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        JSONObject results=new JSONObject();
        results.put("filename",file_name);
        return results;
    }
    
}
