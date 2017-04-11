package com.mystery0.hitokoto.class_class;

import org.litepal.crud.DataSupport;

public class HitokotoGroup extends DataSupport
{
    private String name;

    public HitokotoGroup(String name)
    {
        this.name = name;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
}
