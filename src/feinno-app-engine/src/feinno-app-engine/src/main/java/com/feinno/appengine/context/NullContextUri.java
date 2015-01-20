package com.feinno.appengine.context;

import com.feinno.appengine.ContextUri;


public class NullContextUri extends ContextUri {

    @Override
    public String getProtocol() {
        // TODO Auto-generated method stub
        return "id";
    }

    @Override
    public String getValue() {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setValue(String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public String getParameter(String p) {
        // TODO Auto-generated method stub
        return null;
    }

    @Override
    protected void setParameter(String p, String value) {
        // TODO Auto-generated method stub

    }

    @Override
    public String toString() {
        // TODO Auto-generated method stub
        return "id";
    }

    @Override
    public int getRouteHash() {
        // TODO Auto-generated method stub
        return 0;
    }

}
