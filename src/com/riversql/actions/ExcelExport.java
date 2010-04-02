
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletOutputStream;
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

public class ExcelExport extends ExportPage {

    String ex;

    public void setEx(String ex) {
        this.ex = ex;
    }
    public void execute(HttpServletRequest request, HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception {
        loadUploadParameter(request);

        ex=parameterMap.get("ex");

        response.setHeader("Pragma" ,"public");
        response.setHeader("Expires", "0"); // set expiration time
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setContentType("application/vnd.ms-excel");
        response.setHeader("Content-Disposition","attachment;filename=export.xls");
        response.setContentLength(ex.length());
        ServletOutputStream os = response.getOutputStream();
        os.write(ex.getBytes());
        os.flush();
    }

}
