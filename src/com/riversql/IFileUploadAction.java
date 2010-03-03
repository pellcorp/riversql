
package com.riversql;

import java.util.Map;

import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.fileupload.FileItem;

public interface IFileUploadAction {
	public void execute(Map<String, FileItem> fileMap, Map<String,String> parameterMap,HttpServletResponse response,  EntityManager em, EntityTransaction et) throws Exception;
}
