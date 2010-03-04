/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package com.riversql.dbtree;

import java.sql.SQLException;
import net.sourceforge.squirrel_sql.fw.sql.SQLConnection;

/**
 *
 * @author river.liao
 */
public class UsersNode extends DBNode implements IStructureNode{

    public UsersNode(IStructureNode parent,String tableType, SQLConnection conn) {
        super(conn);
    }

    @Override
    protected void nodeLoad() throws SQLException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getName() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getType() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String getCls() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public boolean isLeaf() {
        throw new UnsupportedOperationException("Not supported yet.");
    }

}
