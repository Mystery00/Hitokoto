package com.mystery0.hitokoto.class_class;

import org.litepal.crud.DataSupport;

public class HitokotoLocal extends DataSupport
{
    private String content;
    private String source;
    private String date;

    public HitokotoLocal()
    {
    }

    public HitokotoLocal(String content, String source, String date)
    {
        this.content = content;
        this.source = source;
        this.date = date;
    }

    public String getContent()
    {
        return content;
    }

    public void setContent(String content)
    {
        this.content = content;
    }

    public String getSource()
    {
        return source;
    }

    public void setSource(String source)
    {
        this.source = source;
    }

    public String getDate()
    {
        return date;
    }

    public void setDate(String date)
    {
        this.date = date;
    }
}
