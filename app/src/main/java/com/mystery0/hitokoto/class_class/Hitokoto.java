package com.mystery0.hitokoto.class_class;

import java.io.Serializable;

public class Hitokoto implements Serializable
{
    private int id;
    private String hitokoto;
    private String type;
    private String from;
    private String creator;
    private String cearted_at;

    public Hitokoto(int id, String hitokoto, String type, String from, String creator, String cearted_at)
    {
        this.id = id;
        this.hitokoto = hitokoto;
        this.type = type;
        this.from = from;
        this.creator = creator;
        this.cearted_at = cearted_at;
    }

    public int getId()
    {
        return id;
    }

    public void setId(int id)
    {
        this.id = id;
    }

    public String getHitokoto()
    {
        return hitokoto;
    }

    public void setHitokoto(String hitokoto)
    {
        this.hitokoto = hitokoto;
    }

    public String getType()
    {
        return type;
    }

    public void setType(String type)
    {
        this.type = type;
    }

    public String getFrom()
    {
        return from;
    }

    public void setFrom(String from)
    {
        this.from = from;
    }

    public String getCreator()
    {
        return creator;
    }

    public void setCreator(String creator)
    {
        this.creator = creator;
    }

    public String getCearted_at()
    {
        return cearted_at;
    }

    public void setCearted_at(String cearted_at)
    {
        this.cearted_at = cearted_at;
    }
}
