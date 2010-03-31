/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.actions;

import com.riversql.JSONAction;
import javax.persistence.EntityManager;
import javax.persistence.EntityTransaction;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.json.JSONObject;

/**
 *
 * @author river.liao
 */
public class Export implements JSONAction{

    public JSONObject execute(HttpServletRequest request, HttpServletResponse response, EntityManager em, EntityTransaction et) throws Exception {
        throw new UnsupportedOperationException("Not supported yet.");
    }


}
