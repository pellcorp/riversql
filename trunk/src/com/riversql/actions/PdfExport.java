
package com.riversql.actions;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;

import com.riversql.actions.export.impl.PDFTableExporter;

public class PdfExport extends ExportPage {

    public void execute(HttpServletRequest request, HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception {
        loadUploadParameter(request);

        String meta_ = parameterMap.get("meta");
        String data_ = parameterMap.get("data");

        JSONArray meta=new JSONArray(meta_);
        JSONArray data=new JSONArray(data_);
        //JSONArray info2=new JSONArray(info_);
        PDFTableExporter tableExporter=new PDFTableExporter(meta.length(),meta);

        for(int i=0;i<data.length();i++){
                JSONArray row=data.getJSONArray(i);
                tableExporter.newLine();
                for(int j=0;j<row.length();j++){
                    tableExporter.newCell(row.get(j));
                }
        }
        tableExporter.finish();

        response.setHeader("Pragma" ,"public");
        response.setHeader("Expires", "0"); // set expiration time
        response.setHeader("Cache-Control", "must-revalidate, post-check=0, pre-check=0");
        response.setContentType(tableExporter.getMimeType());
        response.setHeader("Content-Disposition","attachment;filename=export.pdf");
        response.setContentLength(tableExporter.getContentSize());
        ServletOutputStream os = response.getOutputStream();
        tableExporter.copyTo(os);
        os.flush();
    }

}
