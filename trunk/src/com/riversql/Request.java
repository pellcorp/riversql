
package com.riversql;

import java.io.PrintWriter;
import java.lang.reflect.InvocationTargetException;
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
        JSONDispatchAction jsonDispatchAction = (JSONDispatchAction)jasonActionClass.newInstance();

        JSONObject obj=new JSONObject();
        PrintWriter writer=resp.getWriter();
        try {
                JSONObject objsr = null;

                et=em.getTransaction();
                et.begin();
                objsr=jsonDispatchAction.dispatch(req, resp, em,et);
                
                if(et.isActive())
                        et.commit();
                resp.setHeader("Content-Type", "text/html;charset=ISO-8859-1");
                obj.put("success",true);
                if(objsr!=null)
                        obj.put("result",objsr);
        }
        catch (Exception e) {
            String errorMsg = "";
            if (e instanceof InvocationTargetException)
            {
                Throwable targetEx = ((InvocationTargetException)e).getTargetException();
                if (targetEx != null)
                {
                     errorMsg = targetEx.toString();
                }
            }
            else
            {
                errorMsg = e.toString();
            }


            if(et!=null && et.isActive())
                    et.rollback();
            try {

                    obj.put("success",false);
                    obj.put("error",errorMsg);
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
