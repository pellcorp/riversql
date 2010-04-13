

package com.riversql;

import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.beanutils.BeanUtils;

import org.json.JSONObject;

public class JSONDispatchAction implements JSONAction{
    public JSONObject dispatch(HttpServletRequest request, HttpServletResponse response,  EntityManager em, EntityTransaction et) throws Exception
    {
        BeanUtils.populate(this, request.getParameterMap());
        JSONObject objsr = null;

        String methodName=request.getParameter("method");
        if(methodName == null)
        {
            execute(request, response, em, et);
        }
        else if(methodName.equalsIgnoreCase("dispatch"))
        {
            throw new UnsupportedOperationException("No this method or not support this method");
        }
        else
        {
            Class[] parameterTypeClass = {HttpServletRequest.class, HttpServletResponse.class, EntityManager.class, EntityTransaction.class};
            Object[] parameterClass = {request, response, em, et};

            Method method = this.getClass().getDeclaredMethod(methodName, parameterTypeClass);
            objsr = (JSONObject)method.invoke(this,parameterClass);
        }
        return objsr;
    }

    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception
    {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
