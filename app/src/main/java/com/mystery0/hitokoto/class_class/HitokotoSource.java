package com.mystery0.hitokoto.class_class;

public class HitokotoSource
{
    private String name;
    private String address;
    private String enable;

    public HitokotoSource()
    {
    }

    public HitokotoSource(String name, String address, String enable)
    {
        this.name = name;
        this.address = address;
        this.enable = enable;
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
}
