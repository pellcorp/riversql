
package com.riversql;

import java.io.PrintWriter;
import java.lang.reflect.Method;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.beanutils.BeanUtils;
import org.json.JSONException;
import org.json.JSONObject;


@SuppressWarnings("serial")
public class Request extends DoServlet {

    @Override
    public void execute(HttpServletRequest req, HttpServletResponse resp, EntityManager em, EntityTransaction et) throws Exception {
        String className=req.getParameter("class");
        Class jasonActionClass = Class.forName("com.riversql.actions."+className);
        JSONAction jsonAction = (JSONAction)jasonActionClass.newInstance();


            
        JSONObject obj=new JSONObject();
        PrintWriter writer=resp.getWriter();
        try {
                BeanUtils.populate(jsonAction, req.getParameterMap());
                JSONObject objsr = null;

                et=em.getTransaction();
                et.begin();
                String methodName=req.getParameter("method");
                if(methodName == null)
                {
                    objsr=jsonAction.execute(req, resp, em,et);
                }
                else
                {
                    Class[] parameterTypeClass = new Class[4];
                    parameterTypeClass[0] = HttpServletRequest.class;
                    parameterTypeClass[1] = HttpServletResponse.class;
                    parameterTypeClass[2] = EntityManager.class;
                    parameterTypeClass[3] = EntityTransaction.class;

                    Object[] parameterClass = new Object[4];
                    parameterClass[0] = req;
                    parameterClass[1] = resp;
                    parameterClass[2] = em;
                    parameterClass[3] = et;

                    Method method = jasonActionClass.getMethod(methodName,parameterTypeClass);
                    objsr = (JSONObject)method.invoke(jsonAction,parameterClass);
                }
                
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
    }
}
