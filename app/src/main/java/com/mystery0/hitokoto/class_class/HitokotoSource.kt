package com.mystery0.hitokoto.class_class

import org.litepal.annotation.Column
import org.litepal.crud.DataSupport

class HitokotoSource(var source: String?, @Column(defaultValue = "Custom") var name: String?, var address: String?, @Column(ignore = true) var enable: String?, var method: Int) : DataSupport()
{
    var content_key: String? = null
    var from_key: String? = null
}
