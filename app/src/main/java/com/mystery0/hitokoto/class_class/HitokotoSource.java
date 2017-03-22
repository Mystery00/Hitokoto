package com.mystery0.hitokoto.class_class;

import org.litepal.crud.DataSupport;

public class HitokotoSource extends DataSupport
{
    private String source;
    private String name;
    private String address;
    private String enable;
    private int method;

    public HitokotoSource(String source, String name, String address, String enable, int method)
    {
        this.source = source;
        this.name = name;
        this.address = address;
        this.enable = enable;
        this.method = method;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }

    public String getAddress()
    {
        return address;
    }

    public void setAddress(String address)
    {
        this.address = address;
    }

    public String getEnable()
    {
        return enable;
    }

    public void setEnable(String enable)
    {
        this.enable = enable;
    }

    public int getMethod()
    {
        return method;
    }

    public void setMethod(int method)
    {
        this.method = method;
    }
}
