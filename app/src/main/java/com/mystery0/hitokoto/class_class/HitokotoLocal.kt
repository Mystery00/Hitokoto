package com.mystery0.hitokoto.class_class

import org.litepal.crud.DataSupport

class HitokotoLocal : DataSupport
{
    var content: String? = null
    var source: String? = null
    var date: String? = null
    var group: String? = null

    constructor()

    constructor(content: String, source: String, date: String)
    {
        this.content = content
        this.source = source
        this.date = date
    }
}