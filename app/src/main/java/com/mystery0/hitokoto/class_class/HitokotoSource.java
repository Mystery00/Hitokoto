package com.mystery0.hitokoto.class_class;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

public class HitokotoSource extends DataSupport
{
    private String source;//源类型
    @Column(defaultValue = "Custom")
    private String name;//源名称
    private String address;//源地址
    @Column(ignore = true)
    private String enable;//是否可用，默认wait
    private String content_key;
    private String from_key;
    private int method;//请求方式,1-get,2-post

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

    public String getContent_key()
    {
        return content_key;
    }

    public void setContent_key(String content_key)
    {
        this.content_key = content_key;
    }

    public String getFrom_key()
    {
        return from_key;
    }

    public void setFrom_key(String from_key)
    {
        this.from_key = from_key;
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
